package com.paddypal.azir.paddypal;

public class LoginResponse {
    int statusId;
    String status;
    LoginObject loginObject;

    public int getStatusId() {
        return statusId;
    }

    public String getStatus() {
        return status;
    }

    public LoginObject getLoginObject() {
        return loginObject;
    }
}
