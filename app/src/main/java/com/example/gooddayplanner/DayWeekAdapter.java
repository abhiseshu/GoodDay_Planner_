package com.example.gooddayplanner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.GregorianCalendar;
import java.util.List;


public class DayWeekAdapter extends ArrayAdapter {

    DatabaseHandler db;
    public DayWeekAdapter(@NonNull Context context, int resource, @NonNull List<DayWeek> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        db = DatabaseHandler.getInstance(getContext());

        View GridItemView = convertView;
        if (GridItemView == null) {
            GridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.weekdayitem, parent, false);
        }

        DayWeek dayWeek = (DayWeek) getItem(position);

        TextView mday = (TextView) GridItemView.findViewById(R.id.weekdateid);
        mday.setText(dayWeek.getmDay());

        ImageView mimageView = (ImageView) GridItemView.findViewById(R.id.mood_pic_weather_id);

        TextView event_text1 = (TextView) GridItemView.findViewById(R.id.event_week1);
        TextView event_text2 = (TextView) GridItemView.findViewById(R.id.event_week2);
        TextView event_text3 = (TextView) GridItemView.findViewById(R.id.event_week3);
        TextView event_text4 = (TextView) GridItemView.findViewById(R.id.event_week4);

        GregorianCalendar gregorianCalendar = new GregorianCalendar(Integer.parseInt(dayWeek.getmCurYear()), Integer.parseInt(dayWeek.getmCurMonth()) -1, Integer.parseInt(dayWeek.getmCurDay()));
        Log.i("DayWeekAdapter",Integer.parseInt(dayWeek.getmCurDay()) + " "+ (Integer.parseInt(dayWeek.getmCurMonth()) -1) +" "+dayWeek.getmCurYear() +" "+ gregorianCalendar.getTimeInMillis());

        event_text1.setText("");
        event_text2.setText("");
        event_text3.setText("");
        event_text4.setText("");

        Event obj[] = db.getEventsForDay(gregorianCalendar.getTimeInMillis());
        for(int i=0;i<obj.length;i++){
            Log.i("EVENT INFO",obj.length +" "+obj[i].getName() + " " + obj[i].getStart() + " " + obj[i].getEnd() + " " + obj[i].getDate()+" "+ obj[i].getLocation() +" "+ obj[i].getDescription() +" " + obj[i].getID());

            if (i == 0)
                event_text1.setText(obj[i].getName());
            if (i == 1)
                event_text2.setText(obj[i].getName());
            if (i == 2)
                event_text3.setText(obj[i].getName());
            if (i == 3){
                event_text4.setText("More..!!");
                break;
            }


        }


        return GridItemView;

    }
}
