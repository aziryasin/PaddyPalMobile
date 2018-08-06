package com.paddypal.azir.paddypal.getGuidance;

import java.util.List;

public class GuidanceResponse {
    int statusId;
    String status;
    List<Guidance> object;

    public int getStatusId() {
        return statusId;
    }

    public String getStatus() {
        return status;
    }

    public List<Guidance> getObject() {
        return object;
    }
}
