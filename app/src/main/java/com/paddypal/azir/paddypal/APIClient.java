package com.paddypal.azir.paddypal;

import com.paddypal.azir.paddypal.getGuidance.GuidanceData;
import com.paddypal.azir.paddypal.getGuidance.GuidanceResponse;
import com.paddypal.azir.paddypal.login.LoginData;
import com.paddypal.azir.paddypal.login.LoginResponse;
import com.paddypal.azir.paddypal.register.CreateData;
import com.paddypal.azir.paddypal.register.CreateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIClient {
    @POST("keycloak/login")
    Call<LoginResponse> login(@Header("Content-Type") String content_type, @Body LoginData loginData);

    @POST("keycloak/create")
    Call<CreateResponse> create(@Header("Content-Type") String content_type, @Body CreateData createData);

    @POST("farmer/guidance")
    Call<GuidanceResponse> getGuidance(@Header("Content-Type") String content_type, @Header("Authorization") String token, @Body GuidanceData guidanceData);
}

