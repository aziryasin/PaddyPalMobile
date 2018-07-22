package com.paddypal.azir.paddypal;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageProperties;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class DetectActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String CLOUD_VISION_API_KEY="AIzaSyCtcfBvVdVxQw6OwqeyZvANk1QoElJnISo";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1200;
    private static final String TAG = MainActivity.class.getSimpleName();
    CardView capture,detect;
    ImageView preview;
    String ImageURL;
    Button btnCrop;
    String jsonString;
    int red,green,blue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);
        preview=findViewById(R.id.preview);
        capture=findViewById(R.id.BtnCapture);
        detect=findViewById(R.id.BtnDetect);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView disp=findViewById(R.id.textView5);
                jsonString=disp.getText().toString();
                try {
                    JSONObject colors = new JSONObject(jsonString);
                    JSONObject color=colors.getJSONObject("color");
                    red=color.getInt("red");
                    green=color.getInt("green");
                    blue=color.getInt("blue");

                    Toast.makeText(DetectActivity.this,"Dominant Color consist of \n red="+red+"; green="+green+"; blue="+blue,Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e){
                    Log.d(TAG,e.getMessage());
                }

            }
        });


    }





    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageURL=saveToInternalStorage(imageBitmap);
            preview.setImageBitmap(imageBitmap);
            detect();
        }
    }




    private Bitmap loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "paddy.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

    }


    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/paddypal/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"paddy.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    private void detect(){
        uploadImage(ImageURL);
    }

    private Vision.Images.Annotate prepareAnnotationRequest(final Bitmap bitmap) throws IOException{
        HttpTransport httpTransport= AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory= GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer=
                new VisionRequestInitializer(CLOUD_VISION_API_KEY){
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest) throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName=getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER,packageName);

                        String sig= PackageManagerUtils.getSignature(getPackageManager(),packageName);
                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER,sig);

                    }
                };

        Vision.Builder builder=new Vision.Builder(httpTransport,jsonFactory,null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision=builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest=
                new BatchAnnotateImagesRequest();

        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>(){{
            AnnotateImageRequest annotateImageRequest=new AnnotateImageRequest();

            //Add Image

            Image base64EncodedImage=new Image();

            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
            byte[] imageBytes=byteArrayOutputStream.toByteArray();


            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);


            annotateImageRequest.setFeatures(new ArrayList<Feature>(){{
                Feature imageproperties=new Feature();
                imageproperties.setType("IMAGE_PROPERTIES");
                imageproperties.setMaxResults(MAX_LABEL_RESULTS);
                add(imageproperties);
            }});

            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest=
                vision.images().annotate(batchAnnotateImagesRequest);

        annotateRequest.setDisableGZipContent(true);
        Log.d(TAG,"created cloud vision request object, sending request");

        return annotateRequest;
    }


    private static class ImagePropertiesTask extends AsyncTask<Object,Void,String> {
        private final WeakReference<DetectActivity> detectActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        ImagePropertiesTask(DetectActivity activity,Vision.Images.Annotate annotate){
            detectActivityWeakReference=new WeakReference<>(activity);
            mRequest=annotate;
        }

        @Override
        protected void onPostExecute(String result) {
            DetectActivity activity = detectActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                TextView resultText=activity.findViewById(R.id.textView5);
                resultText.setText(result);
                Toast.makeText(activity,result,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG,"created cloud vision request object, sending requests..");
                BatchAnnotateImagesResponse response=mRequest.execute();
                return convertResponseToString(response);
            }catch (GoogleJsonResponseException e){
                Log.d(TAG, "failed to make API request because " + e.getContent());
            }catch (IOException e){
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";

        }
    }


    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder();

        ImageProperties property = response.getResponses().get(0).getImagePropertiesAnnotation();
        if (property != null) {
            message.append(property.getDominantColors().getColors());

        } else {
            message.append("}");
        }
        Log.d(TAG,message.toString().substring(1,message.toString().length()-1));
        return message.toString().substring(1,message.toString().length()-1);
    }

    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
        Toast.makeText(this,"Loading",Toast.LENGTH_LONG).show();

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> imagePropertiesTask = new ImagePropertiesTask(this, prepareAnnotationRequest(bitmap));
            imagePropertiesTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    public void uploadImage(String imageURL){
        if(imageURL != null){
            Bitmap bitmap=scaleBitmapDown(loadImageFromStorage(imageURL),MAX_DIMENSION);

            callCloudVision(bitmap);

        }else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, "Null Image", Toast.LENGTH_LONG).show();
        }
    }

}
