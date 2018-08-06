package com.paddypal.azir.paddypal.login;

public class LoginResponse {
    int statusId;
    String status;
    LoginObject object;

    public int getStatusId() {
        return statusId;
    }

    public String getStatus() {
        return status;
    }

    public LoginObject getObject() {
        return object;
    }
}
