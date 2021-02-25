package com.example.gooddayplanner;

public class DayMonth {
    private String mDay;
    private String mTemp;
    private String mWeather_mood;
    private boolean isEvent;

    public DayMonth(String mDay){
        this.mDay = mDay;
        this.mTemp = null;
        this.mWeather_mood = null;
        this.isEvent = false;
    }
    public DayMonth(String mDay,String mTemp,String mImgURL){
        this.mDay = mDay;
        this.mTemp = mTemp;
        this.mWeather_mood = mImgURL;
        this.isEvent = false;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

    public void setmWeather_mood(String mImgURL) {
        this.mWeather_mood = mImgURL;
    }

    public void setmTemp(String mTemp) {
        this.mTemp = mTemp;
    }

    public void setEvent(boolean event) {
        isEvent = event;
    }

    public String getmDay() {
        return mDay;
    }

    public String getmWeather_mood() {
        return mWeather_mood;
    }

    public String getmTemp() {
        return mTemp;
    }

    public boolean isEvent() {
        return isEvent;
    }

}
