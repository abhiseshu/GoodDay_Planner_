package com.example.gooddayplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DaysViewActivity extends AppCompatActivity {

    Calendar calendar;
    SimpleDateFormat format;
    Intent intent;
    ListView eventDayListView;
    DayEventsAdapter dayEventsAdapter;
    ArrayList<Event> arrayList;
    TextView dayText;
    DatabaseHandler db;
    String curDay;
    String curMonth;
    String curYear;
    SimpleDateFormat dayFormat;
    SimpleDateFormat monthFormat;
    SimpleDateFormat yearFormat;
    long mdate;
    TextView empty_text;

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_view);

        db = DatabaseHandler.getInstance(getApplicationContext());

        intent = getIntent();
        curDay = intent.getStringExtra("day");
        curMonth = intent.getStringExtra("month");
        curYear = intent.getStringExtra("year");

        empty_text = (TextView) findViewById(R.id.empty_text);

        arrayList = new ArrayList<Event>();

        dayText = (TextView) findViewById(R.id.day_name_id);

        eventDayListView = (ListView) findViewById(R.id.eventdaylistview);


        dayFormat = new SimpleDateFormat("dd");

        monthFormat = new SimpleDateFormat("MM");

        yearFormat = new SimpleDateFormat("yyyy");

        calendar = Calendar.getInstance();

        format = new SimpleDateFormat("MMM dd, yyyy");
        calendar.set(Calendar.YEAR, Integer.parseInt(curYear));
        calendar.set(Calendar.MONTH, Integer.parseInt(curMonth) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(curDay));


        GregorianCalendar gregorianCalendar = new GregorianCalendar(Integer.parseInt(curYear), Integer.parseInt(curMonth) -1, Integer.parseInt(curDay));
        Log.i("DAY VIEW ACTIVITY"," NO: "+ curDay+" "+curMonth+" "+curYear +" "+ gregorianCalendar.getTimeInMillis());
        mdate = gregorianCalendar.getTimeInMillis();

        try {
            Event obj[] = db.getEventsForDay(gregorianCalendar.getTimeInMillis());
            for(int i=0;i<obj.length;i++){
                Log.i("IN DAY VIEW,EVENT INFO",obj[i].getName() + " " + obj[i].getStart() + " " + obj[i].getEnd() + " " + obj[i].getDate()+" "+ obj[i].getLocation() +" "+ obj[i].getDescription() +" " + obj[i].getID());
                arrayList.add(obj[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.i("DayView", format.format(calendar.getTime()));

        dayText.setText(format.format(calendar.getTime()));

        if (arrayList.isEmpty()){
            empty_text.setVisibility(View.VISIBLE);
        }else{
            empty_text.setVisibility(View.GONE);
        }

        dayEventsAdapter = new DayEventsAdapter(getApplicationContext(), R.layout.event_view_layout, arrayList);

        eventDayListView.setAdapter(dayEventsAdapter);


        Button leftbn = (Button) findViewById(R.id.left_day_button_id);

        leftbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    calendar.add(Calendar.DAY_OF_MONTH,-1);

                Log.i("DayView", format.format(calendar.getTime()));

                updateEvents();
            }
        });

        Button rightbn = (Button) findViewById(R.id.right_day_button_id);

        rightbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DAY_OF_MONTH,1);

                Log.i("DayView", format.format(calendar.getTime()));

                updateEvents();

            }
        });

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                updateEvents();
            }
        }, delay);


        eventDayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("DAYS VIEW ACTIVITY CLICK ITEM", "POS IS" + arrayList.get(i).getID());

                long realStartTime  = arrayList.get(i).getStart() - mdate;
                double mStartTime = (double) realStartTime/3600000;


                int mStartHour = (int) mStartTime;
                double mtempRatio = mStartTime - (double) mStartHour;
                int mStartMinutes = (int) (mtempRatio * 60);

                String mRealStartTime;
                String mDateString = curDay + "/" + curMonth + "-" + curYear;

                if (mStartMinutes < 10)
                    mRealStartTime = mStartHour +":0"+ mStartMinutes;
                else
                    mRealStartTime = mStartHour +":"+ mStartMinutes;

                long realEndTime  = arrayList.get(i).getEnd() - mdate;
                double mEndTime = (double) realEndTime/3600000;


                int mEndHour = (int) mEndTime;
                double mEndRatio = mEndTime - (double) mEndHour;
                int mEndMinutes = (int) (mEndRatio * 60);

                String mRealEndTime;

                if (mEndMinutes < 10)
                    mRealEndTime = mEndHour +":0"+ mEndMinutes;
                else
                    mRealEndTime = mEndHour +":"+ mEndMinutes;


                Log.i("MATH CHECk", mdate+" " + arrayList.get(i).getStart() +" "+ mStartTime + " " +mRealStartTime +" "+mtempRatio +" "+ mStartMinutes);

                Intent dayIntent = new Intent(getApplicationContext(),EventEditActivity.class);
                dayIntent.putExtra("ID",arrayList.get(i).getID());
                dayIntent.putExtra("NAME",arrayList.get(i).getName());
                dayIntent.putExtra("DATE",arrayList.get(i).getDate());
                dayIntent.putExtra("START",arrayList.get(i).getStart());
                dayIntent.putExtra("END",arrayList.get(i).getEnd());
                dayIntent.putExtra("LOCATION",arrayList.get(i).getLocation());
                dayIntent.putExtra("DESCRIPTION",arrayList.get(i).getDescription());
                dayIntent.putExtra("EXTRA_START",mRealStartTime);
                dayIntent.putExtra("EXTRA_END",mRealEndTime);

                startActivity(dayIntent);

            }
        });


        //7,52,40,000

        FloatingActionButton fab = findViewById(R.id.fabIdDayView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent createEventIntent = new Intent(getApplicationContext(), EventCreateActivity.class);
                startActivity(createEventIntent);
            }
        });
    }

    void updateEvents(){
        curDay = dayFormat.format(calendar.getTime());
        curMonth = monthFormat.format(calendar.getTime());
        curYear = yearFormat.format(calendar.getTime());

        arrayList.clear();

        GregorianCalendar gregorianCalendar = new GregorianCalendar(Integer.parseInt(curYear), Integer.parseInt(curMonth) -1, Integer.parseInt(curDay));
        Log.i("DAY update VIEW ACTIVITY"," NO: "+ curDay+" "+curMonth+" "+curYear +" "+ gregorianCalendar.getTimeInMillis());

        mdate = gregorianCalendar.getTimeInMillis();

        try {
            Event obj[] = db.getEventsForDay(gregorianCalendar.getTimeInMillis());
            for(int i=0;i<obj.length;i++){
                Log.i("IN DAY VIEW,EVENT INFO",obj[i].getName() + " " + obj[i].getStart() + " " + obj[i].getEnd() + " " + obj[i].getDate()+" "+ obj[i].getLocation() +" "+ obj[i].getDescription() +" " + obj[i].getID());
                arrayList.add(obj[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        if (arrayList.isEmpty()){
            empty_text.setVisibility(View.VISIBLE);
        }else if(!arrayList.isEmpty()){
            empty_text.setVisibility(View.GONE);
        }



        dayText.setText(format.format(calendar.getTime()));

        dayEventsAdapter.notifyDataSetChanged();
    }


}