package com.paddypal.azir.paddypal;

public class TokenObj {
    String access_token,refresh_token,token_type,session_state;
    int refresh_expires_in,expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getSession_state() {
        return session_state;
    }

    public int getRefresh_expires_in() {
        return refresh_expires_in;
    }

    public int getExpires_in() {
        return expires_in;
    }
}
