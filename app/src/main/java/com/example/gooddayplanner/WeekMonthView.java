package com.example.gooddayplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class WeekMonthView extends AppCompatActivity {

    String[] weekdays;
    Calendar now;
    SimpleDateFormat format;
    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList arrayList;
    int curWeekPos = 0;
    boolean isRight;
    TextView weekName;
    String weekNameStr=" ";
    SimpleDateFormat weeknameformat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_month_view);

        weekdays =  new String[7];
        now = Calendar.getInstance();
        format = new SimpleDateFormat("MMM, dd yyyy");
        weeknameformat = new SimpleDateFormat("MMM, dd");

        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2;
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0;i<7;i++){
            if(i==0){
                now.add(Calendar.DAY_OF_MONTH, curWeekPos);
                weekNameStr = weeknameformat.format(now.getTime())+"-";
            }
            weekdays[i] = format.format(now.getTime());
            if(i==6) {
                weekNameStr += weeknameformat.format(now.getTime());
                Log.d("TAG",weekNameStr +" "+weeknameformat.format(now.getTime()));
            }
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        arrayList = new ArrayList(Arrays.asList(weekdays));

        weekName = (TextView) findViewById(R.id.weeknameid);

        listView = (ListView) findViewById(R.id.weeklistview);

        arrayAdapter = new ArrayAdapter(this,R.layout.weekdayitem,R.id.weekdateid,arrayList);

        listView.setAdapter(arrayAdapter);

        weekName.setText(weekNameStr);

        Button leftarrow = (Button) findViewById(R.id.button2);
        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                curWeekPos -= 7;
                if(isRight)
                    now.add(Calendar.DAY_OF_MONTH,-14);
                else
                    now.add(Calendar.DAY_OF_MONTH,-7);
                UpdateWeek();
            }
        });

        Button rightarrow = (Button) findViewById(R.id.button3);
        rightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //curWeekPos +=7;
                isRight =  true;
                UpdateWeek();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabIdWeekView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    //update the week list when week is changed
    void UpdateWeek(){
        weekNameStr="";
        arrayList.clear();
        for (int i = 0;i<7;i++){
//            if(i==0) {
//                now.add(Calendar.DAY_OF_MONTH, curWeekPos);
//                Log.d("mylog::: ", String.valueOf(curWeekPos));
//            }
            weekdays[i] = format.format(now.getTime());
            arrayList.add(weekdays[i]);
            if(i==0)
                weekNameStr = weeknameformat.format(now.getTime()) + " - ";
            else if(i==6)
                weekNameStr += weeknameformat.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        weekName.setText(weekNameStr);
        Log.d("mylog",weekdays[0]);
        arrayAdapter.notifyDataSetChanged();
    }

    //right swipe to Calendar view
    float x1, x2, y1, y2;
    static int MIN_DISTANCE = 150;

    public boolean onTouchEvent (MotionEvent touchEvent){
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

                float valueX =  x2-x1;

                float valueY = y2-y1;

                if(Math.abs(valueX)>MIN_DISTANCE){
                    if (x1 > x2) {

                    }else{
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                }
                break;
        }
        return false;
    }

}