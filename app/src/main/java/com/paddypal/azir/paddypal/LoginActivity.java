package com.paddypal.azir.paddypal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    CardView login,register;
    TextView username,password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.BtnLogin);
        register=findViewById(R.id.BtnRegister);
        username=findViewById(R.id.txtUserNameLog);
        password=findViewById(R.id.txtPasswordLog);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(username.getText())||TextUtils.isEmpty(password.getText())){
                    Toast.makeText(LoginActivity.this,"Please fill both the fields!",Toast.LENGTH_LONG).show();
                }
                else {
                    if(isNetworkConnected()){
                        login(username.getText().toString(),password.getText().toString());
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Please connect to internet",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
    }

    public void login(String username,String password){


        LoginData loginData=new LoginData(username,password);

        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("http://173.82.212.198:8095/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit=builder.build();

        APIClient apiClient=retrofit.create(APIClient.class);

        Call<LoginResponse> tokenCall=apiClient.login("application/json",loginData);
        tokenCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    Toast.makeText(LoginActivity.this,response.body().getLoginObject().getToken().getAccess_token(),Toast.LENGTH_LONG).show();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"error",Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
