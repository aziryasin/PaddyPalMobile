package com.paddypal.azir.paddypal.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.paddypal.azir.paddypal.APIClient;
import com.paddypal.azir.paddypal.R;
import com.paddypal.azir.paddypal.RetrofitClient;
import com.paddypal.azir.paddypal.login.LoginActivity;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private AVLoadingIndicatorView avi;
    private CardView registerBtn;
    CreateData createData;
    private EditText firstname,lastname,nic,username,password,password2,phoneNumber,email,location,fieldSize;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        avi=findViewById(R.id.avi);
        avi.hide();
        firstname=findViewById(R.id.regFname);
        lastname=findViewById(R.id.regLname);
        nic=findViewById(R.id.regNIC);
        username=findViewById(R.id.regUsername);
        password=findViewById(R.id.regPasswd);
        password2=findViewById(R.id.regPasswd2);
        phoneNumber=findViewById(R.id.regPhone);
        email=findViewById(R.id.regEmail);
        location=findViewById(R.id.regLocation);
        fieldSize=findViewById(R.id.regFieldSize);


        registerBtn=findViewById(R.id.BtnRegister);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createData=new CreateData(
                        nic.getText().toString(),
                        firstname.getText().toString(),
                        lastname.getText().toString(),
                        username.getText().toString(),
                        password.getText().toString(),
                        email.getText().toString(),
                        "farmer",
                        "Northern",
                        "Jaffna",
                        location.getText().toString(),
                        Float.parseFloat(fieldSize.getText().toString()));
                register(createData);
            }
        });
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter.Builder filterBuilder = new AutocompleteFilter.Builder();
        filterBuilder.setCountry("LK");
        filterBuilder.setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES);
        autocompleteFragment.setFilter(filterBuilder.build());
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("result", "Place: " + place.getName());
                Toast.makeText(RegisterActivity.this,place.getName(),Toast.LENGTH_LONG).show();
                location.setText(place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.i("error is", "An error occurred: " + status);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
    }


    public void register(CreateData createData){

        avi.show();


        APIClient apiClient= RetrofitClient.getClient().create(APIClient.class);

        Call<CreateResponse> createResponseCall=apiClient.create("application/json",createData);
        createResponseCall.enqueue(new Callback<CreateResponse>() {
            @Override
            public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                try {
                    if(response.body().statusId==200 || response.body().statusId==201){
                        //sharedPreferences=getApplicationContext().getSharedPreferences("")
                        Log.d("Success",response.body().toString());
                        Toast.makeText(RegisterActivity.this,response.body().getStatus(),Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        avi.hide();
                        Log.d("Failed",response.body().toString());
                        Toast.makeText(RegisterActivity.this,response.body().getStatus(),Toast.LENGTH_LONG).show();
                    }

                }catch (NullPointerException e){
                    avi.hide();
                    Toast.makeText(RegisterActivity.this,"error",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<CreateResponse> call, Throwable t) {
                avi.hide();
                Toast.makeText(RegisterActivity.this,"error failed",Toast.LENGTH_LONG).show();
            }
        });

    }
}
