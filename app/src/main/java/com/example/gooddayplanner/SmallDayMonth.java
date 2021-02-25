package com.example.gooddayplanner;

public class SmallDayMonth {

    private String main_temp;
    private String text_name;

    public SmallDayMonth(String par1,String par2){
        main_temp = par1;
        text_name = par2;
    }

    public void setMain_temp(String main_temp) {
        this.main_temp = main_temp;
    }

    public void setText_name(String text_name) {
        this.text_name = text_name;
    }

    public String getMain_temp() {
        return main_temp;
    }

    public String getText_name() {
        return text_name;
    }
}
