package com.paddypal.azir.paddypal.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import com.paddypal.azir.paddypal.APIClient;
import com.paddypal.azir.paddypal.HomeActivity;
import com.paddypal.azir.paddypal.R;
import com.paddypal.azir.paddypal.RetrofitClient;
import com.paddypal.azir.paddypal.register.RegisterActivity;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    CardView login,register;
    CheckBox rememberMe;
    TextView username,password;
    private AVLoadingIndicatorView avi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String indicator=getIntent().getStringExtra("indicator");
        avi=findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.hide();
        login=findViewById(R.id.BtnLogin);
        register=findViewById(R.id.BtnRegister);
        username=findViewById(R.id.txtUserNameLog);
        password=findViewById(R.id.txtPasswordLog);
        rememberMe=findViewById(R.id.cboxRemember);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(username.getText())||TextUtils.isEmpty(password.getText())){
                    Toast.makeText(LoginActivity.this,"Please fill both the fields!",Toast.LENGTH_LONG).show();
                }
                else {
                    if(isNetworkConnected()){
//                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
//                        finish();
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

        avi.show();
        LoginData loginData=new LoginData(username,password,"farmer");

        APIClient apiClient= RetrofitClient.getClient().create(APIClient.class);

        Call<LoginResponse> tokenCall=apiClient.login("application/json",loginData);
        tokenCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    if(response.body().statusId==200 && response.body().getObject().getToken().getAccess_token()!= null){
                        //sharedPreferences=getApplicationContext().getSharedPreferences("")
                        Log.d("Success",response.body().toString());
                        //Toast.makeText(LoginActivity.this,response.body().getObject().getToken().getAccess_token(),Toast.LENGTH_LONG).show();
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("id",response.body().getObject().getId());
                        editor.putString("token",response.body().getObject().getToken().getAccess_token());
                        editor.apply();

                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    }
                    else {
                        avi.hide();
                        Log.d("Failed",response.body().toString());
                        Toast.makeText(LoginActivity.this,response.body().getStatus(),Toast.LENGTH_LONG).show();
                    }

                }catch (NullPointerException e){
                    avi.hide();
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                avi.hide();
                Toast.makeText(LoginActivity.this,"error",Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
