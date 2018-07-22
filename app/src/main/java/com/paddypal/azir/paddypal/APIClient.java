package com.paddypal.azir.paddypal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIClient {
    @POST("keycloak/login")
    Call<LoginResponse> login(@Header("Content-Type") String content_type, @Body LoginData loginData);
}

