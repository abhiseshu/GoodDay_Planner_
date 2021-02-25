  package com.example.gooddayplanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DayMonthAdapter extends ArrayAdapter {
    public DayMonthAdapter(@NonNull Context context, int resource, @NonNull List<DayMonth> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View GridItemView = convertView;
        if (GridItemView == null) {
            GridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.monthdayitem, parent, false);
        }

        DayMonth month = (DayMonth) getItem(position);

        TextView mday = (TextView) GridItemView.findViewById(R.id.dateidmonth);
        mday.setText(month.getmDay());

        ImageView mimageView = (ImageView) GridItemView.findViewById(R.id.weatherimg);

        if (month.getmWeather_mood()!=null){
            if (month.getmWeather_mood().equals("Rain")){
                Log.i("Weather mood","Rain"+ " " + month.getmWeather_mood());
                mimageView.setImageResource(R.drawable.rain);
            }
            else if(month.getmWeather_mood().equals("Clear")){
                mimageView.setImageResource(R.drawable.sun);
            }
            else if (month.getmWeather_mood().equals("Snow")){
                mimageView.setImageResource(R.drawable.snow);
            }else if(month.getmWeather_mood().equals("Clouds")){
                mimageView.setImageResource(R.drawable.cloud);
            }
        }else{
            mimageView.setImageResource(0);
        }


        TextView mtemp = (TextView) GridItemView.findViewById(R.id.winfoid);
        if(month.getmTemp()!=""){
            mtemp.setText((int)(Double.parseDouble(month.getmTemp()) - 273.15) + " C");
        }else{
            mtemp.setText(month.getmTemp());
        }

        ImageView imgIndicator = (ImageView) GridItemView.findViewById(R.id.indicatorImg);
        if (month.isEvent()){
            imgIndicator.setImageResource(R.drawable.iridescent);
        }else {
            imgIndicator.setImageResource(0);
        }

        return GridItemView;

    }
}
