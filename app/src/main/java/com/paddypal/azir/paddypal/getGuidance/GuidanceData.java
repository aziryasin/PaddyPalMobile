package com.paddypal.azir.paddypal.getGuidance;

import java.util.Date;

public class GuidanceData {
    String id,startDate;
    Float fieldSize;

    public GuidanceData(String id, String startDate,Float fieldSize) {
        this.id = id;
        this.startDate = startDate;
        this.fieldSize = fieldSize;
    }
}
