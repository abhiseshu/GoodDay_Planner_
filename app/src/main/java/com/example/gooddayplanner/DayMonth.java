package com.example.gooddayplanner;

public class DayMonth {
    private String mDay;
    private String mTemp;
    private String mImgURL;

    public DayMonth(String mDay){
        this.mDay = mDay;
        this.mTemp = null;
        this.mImgURL = null;
    }
    public DayMonth(String mDay,String mTemp,String mImgURL){
        this.mDay = mDay;
        this.mTemp = mTemp;
        this.mImgURL = mImgURL;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

    public void setmImgURL(String mImgURL) {
        this.mImgURL = mImgURL;
    }

    public void setmTemp(String mTemp) {
        this.mTemp = mTemp;
    }

    public String getmDay() {
        return mDay;
    }

    public String getmImgURL() {
        return mImgURL;
    }

    public String getmTemp() {
        return mTemp;
    }

}
