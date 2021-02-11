package com.example.gooddayplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    String[] monthDays;
    GridView gridView;
    Calendar cal;
    SimpleDateFormat format;
    TextView monthText;
    int countday;
    int myMonth;
    int curMonthDays;
    ArrayList<DayMonth> arrayList;
    DayMonthAdapter dayMonthAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        gridView = (GridView) findViewById(R.id.girdviewid);

        format = new SimpleDateFormat("dd");

        arrayList = new ArrayList<DayMonth>();

        monthText = (TextView) findViewById(R.id.monthyearid);

        monthText.setText(new SimpleDateFormat("MMM, yyyy").format(cal.getTime()));

        countday = 0;
        monthDays = new String[37];
        for(int i=0; i<37; i++){
            monthDays[i] = "";
            arrayList.add(i,new DayMonth(monthDays[i]));
        }

        //gives the position of first day
        if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
            countday = cal.get(Calendar.DAY_OF_WEEK)  - 2;
            Log.d("countday is",":" + countday + "and"+ cal.get(Calendar.DAY_OF_WEEK));
        }else{
            countday = 6;
        }
        //setting the current month first day in the calendar
        cal.set(Calendar.DAY_OF_MONTH, 1);
        myMonth = cal.get(Calendar.MONTH);

        while(myMonth == cal.get(Calendar.MONTH)){
            monthDays[countday] = format.format(cal.getTime());
            arrayList.get(countday).setmDay(monthDays[countday]);
            Log.d("tag",String.valueOf(cal.getTime()) + "day count is" + countday);
            cal.add(Calendar.DAY_OF_MONTH,1);
            countday += 1;
            curMonthDays +=1;
        }

        dayMonthAdapter = new DayMonthAdapter(getApplicationContext(), R.layout.monthdayitem, arrayList);

        gridView.setAdapter(dayMonthAdapter);

        //left arrow change month
        Button leftMonthButton = (Button) findViewById(R.id.leftmonthid);

        leftMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.add(Calendar.DAY_OF_MONTH, -curMonthDays);
                cal.add(Calendar.MONTH, -1);
                updateMonth();
            }
        });


        //Right arrow change month
        Button RightMonthButton = (Button) findViewById(R.id.rightmonthid);

        RightMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.add(Calendar.DAY_OF_MONTH, -curMonthDays);
                cal.add(Calendar.MONTH, 1);
                updateMonth();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabIdMonthView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }
    //left swipe feature
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
                        Intent i = new Intent(getApplicationContext(), WeekMonthView.class);
                        startActivity(i);
                    }
                }
                break;
        }
        return false;
    }
    void updateMonth(){
        countday = 0;
        curMonthDays = 0;

        monthText.setText(new SimpleDateFormat("MMM, yyyy").format(cal.getTime()));
        for(int i = 0; i<37;i++){
            monthDays[i] = " ";
            arrayList.get(i).setmDay(monthDays[i]);
        }

        if(cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
            countday = cal.get(Calendar.DAY_OF_WEEK) - 2;
            Log.d("tag", String.valueOf(countday));
        }else
            countday = 6;

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int mNewMonth = cal.get(Calendar.MONTH);

        while(mNewMonth == cal.get(Calendar.MONTH)){
            monthDays[countday] = format.format(cal.getTime());
            arrayList.get(countday).setmDay(monthDays[countday]);

            Log.d("tag", String.valueOf(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, 1);
            countday +=1;
            curMonthDays += 1;
        }

        dayMonthAdapter.notifyDataSetChanged();

    }


}