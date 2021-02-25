package com.example.gooddayplanner;

public class SmallDayWeek {

    private String main_temp;
    private String text_name;
    private String feel_temp;

    public SmallDayWeek(String par1,String par2,String par3){
        main_temp = par1;
        text_name = par2;
        feel_temp = par3;
    }

    public void setFeel_temp(String feel_temp) {
        this.feel_temp = feel_temp;
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

    public String getFeel_temp() {
        return feel_temp;
    }
}
