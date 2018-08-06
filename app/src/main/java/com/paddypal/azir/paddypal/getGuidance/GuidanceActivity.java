package com.paddypal.azir.paddypal.getGuidance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.paddypal.azir.paddypal.APIClient;
import com.paddypal.azir.paddypal.R;
import com.paddypal.azir.paddypal.RetrofitClient;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GuidanceActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    AVLoadingIndicatorView avi;
    private List<Guidance> guidanceList=new ArrayList<>();
    private List<Guidance> guidanceListResponse=new ArrayList<>();
    private RecyclerView recyclerView;
    private GuidanceAdapter guidanceAdapter;
    private CardView calender;
    private String token,ageGroup,startDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token=sharedPreferences.getString("token","");

        ageGroup=getIntent().getStringExtra("ageGroup");
        startDate=getIntent().getStringExtra("startDate");

        //Toast.makeText(getApplicationContext(),ageGroup+"  "+ startDate,Toast.LENGTH_SHORT).show();

        avi=findViewById(R.id.aviGuide);
        avi.show();

        calender=findViewById(R.id.BtnViewCalendar);
        recyclerView=findViewById(R.id.GuidanceRecyclerView);
        guidanceAdapter=new GuidanceAdapter(guidanceList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(guidanceAdapter);


        getGuide("Bearer "+token,ageGroup,startDate);

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuidanceActivity.this,CalendarActivity.class));
            }
        });
    }


    public void getGuide(String token, String ageGroup ,String startDate){
        Log.d("guidance","entered Guidance");
        GuidanceData guidanceData=new GuidanceData(ageGroup,startDate,1.2f);
        avi.show();


        APIClient apiClient= RetrofitClient.getClient().create(APIClient.class);

        Call<GuidanceResponse> guidanceCall=apiClient.getGuidance("application/json",token,guidanceData);
        guidanceCall.enqueue(new Callback<GuidanceResponse>() {
            @Override
            public void onResponse(Call<GuidanceResponse> call, Response<GuidanceResponse> response) {
                try {
                    if(response.body().statusId==200){
                        avi.hide();
                        Log.d("response ; ",Integer.toString(response.body().getObject().get(2).getDay()));
                        guidanceListResponse=response.body().getObject();
                        for(Guidance guidance : guidanceListResponse){
                            guidanceList.add(guidance);
                        }
                        guidanceAdapter.notifyDataSetChanged();
                        Gson gson=new Gson();
                        String json=gson.toJson(response.body());
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("guidance",json);
                        editor.apply();
                    }
                    else {
                        avi.hide();
                        Log.d("Failed",response.body().toString());
                        Toast.makeText(GuidanceActivity.this,response.body().getStatus(),Toast.LENGTH_LONG).show();
                    }

                }catch (NullPointerException e){
                    avi.hide();
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<GuidanceResponse> call, Throwable t) {
                avi.hide();
                Toast.makeText(GuidanceActivity.this,"error",Toast.LENGTH_LONG).show();
            }
        });
    }
}
