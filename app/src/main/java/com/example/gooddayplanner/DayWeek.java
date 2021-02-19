package com.example.gooddayplanner;

public class DayWeek {
    private String mDay;
    private String mCondition;
    private String mImg;
    private String mMain_temp;
    private String mFeel_temp;
    private int count;
    private String mCurDay;
    private String mCurYear;
    private String mCurMonth;

    public DayWeek(String tDay,String tCurDay, String tCurMonth, String tCurYear){
        mDay = tDay;
        mCondition = null;
        mImg = null;
        mMain_temp = null;
        mFeel_temp = null;
        count = 0;
        mCurDay = tCurDay;
        mCurMonth = tCurMonth;
        mCurYear = tCurYear;
    }

    public DayWeek(String tday, int tcount){
        mDay = tday;
        mCondition = null;
        mImg = null;
        mMain_temp = null;
        mFeel_temp = null;
        count = tcount;
    }

    public void setmCurDay(String mCurDay) {
        this.mCurDay = mCurDay;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setmCondition(String mCondition) {
        this.mCondition = mCondition;
    }

    public void setmFeel_temp(String mFeel_temp) {
        this.mFeel_temp = mFeel_temp;
    }

    public void setmImg(String mImg) {
        this.mImg = mImg;
    }

    public void setmMain_temp(String mMain_temp) {
        this.mMain_temp = mMain_temp;
    }

    public void setmCurMonth(String mCurMonth) {
        this.mCurMonth = mCurMonth;
    }

    public void setmCurYear(String mCurYear) {
        this.mCurYear = mCurYear;
    }

    public String getmCurDay() {
        return mCurDay;
    }

    public String getmCurMonth() {
        return mCurMonth;
    }

    public String getmCurYear() {
        return mCurYear;
    }

    public String getmDay() {
        return mDay;
    }

    public int getCount() {
        return count;
    }

    public String getmCondition() {
        return mCondition;
    }

    public String getmFeel_temp() {
        return mFeel_temp;
    }

    public String getmImg() {
        return mImg;
    }

    public String getmMain_temp() {
        return mMain_temp;
    }
}
