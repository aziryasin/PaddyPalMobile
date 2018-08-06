package com.paddypal.azir.paddypal.getGuidance;

public class Guidance {
    int step,weekDiff,day,month,year;
    float urea,tsp,mop,zinc;
    String description;

    public Guidance() {
    }

    public Guidance(int step, int weekDiff, int day, int month, int year, float urea, float tsp, float mop, float zinc, String description) {
        this.step = step;
        this.weekDiff = weekDiff;
        this.day = day;
        this.month = month;
        this.year = year;
        this.urea = urea;
        this.tsp = tsp;
        this.mop = mop;
        this.zinc = zinc;
        this.description = description;
    }

    public int getStep() {
        return step;
    }

    public int getWeekDiff() {
        return weekDiff;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public float getUrea() {
        return urea;
    }

    public float getTsp() {
        return tsp;
    }

    public float getMop() {
        return mop;
    }

    public float getZinc() {
        return zinc;
    }

    public String getDescription() {
        return description;
    }

    public String getDate(){
        String date=String.format("%d-%d-%d",year,month,day);
        return date;
    }

    public String getDetailedDescription(){
        String desc=description+"\nAdd "+urea+"kg of Urea and \n"+mop+"kg of MOP";
        return desc;
    }
}
