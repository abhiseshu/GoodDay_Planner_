package com.example.gooddayplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class WeekMonthView extends AppCompatActivity {

    String[] weekdays;
    Calendar now;
    SimpleDateFormat format;
    ListView listView;
    DayWeekAdapter arrayAdapter;
    ArrayList<DayWeek> arrayList;
    int curWeekPos = 0;
    boolean isRight;
    TextView weekName;
    String weekNameStr=" ";
    SimpleDateFormat weeknameformat;
    DatabaseHandler db;
    String curDay;
    String curMonth;
    String curYear;
    SimpleDateFormat dayFormat;
    SimpleDateFormat monthFormat;
    SimpleDateFormat yearFormat;
    String cons_cur_day;
    int cur_day_pos;
    Button locationBn;
    String slat;
    String slog;
    String geoURLFirst = "https://api.opencagedata.com/geocode/v1/json?q=";
    String geoURLLast = "&key=8bf8429dedde457cb534f43c1fafb7e1";
    String weatherStartURL = "https://api.openweathermap.org/data/2.5/onecall?lat=";
    String weatherENDURL = "&exclude=hourly,minutely&appid=bb9604dfab472b092e402372cddf4413";
    int flag_count;
    ArrayList<SmallDayWeek> arrayListLarge;
    ArrayList<SmallDayWeek> arrayListSmall;
    int cur_week;
    int cur_next_week;
    Calendar another_cal;
    boolean pressed = false;
    int temp_cur_next_week;

    //GPS
    private Button gpsButton;
    private LocationManager locationManager;
    private LocationListener locationListener;
    double gpslat;
    double gpslog;
    EditText locationtext;

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 10){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,locationListener);
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_month_view);

        db = DatabaseHandler.getInstance(this);

        locationtext = (EditText) findViewById(R.id.location_test_week_id);

        dayFormat = new SimpleDateFormat("dd");

        monthFormat = new SimpleDateFormat("MM");

        yearFormat = new SimpleDateFormat("yyyy");

        arrayList = new ArrayList<DayWeek>();

        weekdays =  new String[7];
        now = Calendar.getInstance();
        //
        another_cal = Calendar.getInstance();

        cons_cur_day = dayFormat.format(now.getTime());
        format = new SimpleDateFormat("MMM, dd yyyy");
        weeknameformat = new SimpleDateFormat("MMM, dd");


        arrayListLarge = new ArrayList<SmallDayWeek>();
        arrayListSmall = new ArrayList<SmallDayWeek>();

        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2;
        now.add(Calendar.DAY_OF_MONTH, delta);
        another_cal.add(Calendar.DAY_OF_MONTH, delta + 7);
        cur_next_week = Integer.parseInt(dayFormat.format(another_cal.getTime()) + monthFormat.format(another_cal.getTime()));

        for (int i = 0;i<7;i++){
            if(i==0){
                now.add(Calendar.DAY_OF_MONTH, curWeekPos);
                weekNameStr = weeknameformat.format(now.getTime())+"-";
                cur_week = Integer.parseInt(dayFormat.format(now.getTime()) + monthFormat.format(now.getTime()));
                temp_cur_next_week = Integer.parseInt(dayFormat.format(now.getTime()) + monthFormat.format(now.getTime()));

            }
            weekdays[i] = format.format(now.getTime());
            curDay = dayFormat.format(now.getTime());
            curMonth = monthFormat.format(now.getTime());
            curYear = yearFormat.format(now.getTime());
            if (cons_cur_day.equals(curDay)){
                cur_day_pos = i;
            }
            arrayList.add(new DayWeek(weekdays[i],curDay,curMonth,curYear));
            if(i==6) {
                weekNameStr += weeknameformat.format(now.getTime());
                Log.d("TAG",weekNameStr +" "+weeknameformat.format(now.getTime()));
            }
            now.add(Calendar.DAY_OF_MONTH, 1);
        }


//        arrayList = new ArrayList(Arrays.asList(weekdays));

        weekName = (TextView) findViewById(R.id.day_name_id);

        listView = (ListView) findViewById(R.id.eventdaylistview);

        arrayAdapter = new DayWeekAdapter(getApplicationContext(),R.layout.weekdayitem,arrayList);

        listView.setAdapter(arrayAdapter);

        weekName.setText(weekNameStr);

        ///GPS BUTTON CODE
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.i("LOC", "location" + location.toString());
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };
        gpsButton = (Button) findViewById(R.id.gps_location_week_id);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < 23) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }else{
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        ActivityCompat.requestPermissions(WeekMonthView.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},10);

                    }else{
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,50000,0,locationListener);

                       try {
                           Location xlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                           Log.i("LOC SECOND", "lat"+ xlocation.getLatitude() + "log"+xlocation.getLongitude());

                           gpslat = xlocation.getLatitude();
                           gpslog = xlocation.getLongitude();

                       }catch (Exception e){
                           e.printStackTrace();
                       }

                        locationtext.setText("Set To Current Location");

                    }
                }
            }
        });


        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                arrayAdapter.notifyDataSetChanged();
            }
        }, delay);


        Button leftarrow = (Button) findViewById(R.id.left_day_button_id);
        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                curWeekPos -= 7;
                if(isRight)
                    now.add(Calendar.DAY_OF_MONTH,-14);
                else
                    now.add(Calendar.DAY_OF_MONTH,-14);
                UpdateWeek();
            }
        });

        Button rightarrow = (Button) findViewById(R.id.right_day_button_id);
        rightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //curWeekPos +=7;
                isRight =  true;
                UpdateWeek();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("CLICK POS", "POS IS" + arrayList.get(i).getmDay());

                Intent dayIntent = new Intent(getApplicationContext(),DaysViewActivity.class);
                dayIntent.putExtra("day",arrayList.get(i).getmCurDay());
                dayIntent.putExtra("month",arrayList.get(i).getmCurMonth());
                dayIntent.putExtra("year",arrayList.get(i).getmCurYear());
                startActivity(dayIntent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabIdWeekView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent createEventIntent = new Intent(getApplicationContext(), EventCreateActivity.class);
                startActivity(createEventIntent);
            }
        });

        //Location api working
        locationBn = (Button) findViewById(R.id.locationMonthBnWeekid);
        locationBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAP","TAPPED");

                ///
                ///
                ///
                if (locationtext.getText().toString() == null || locationtext.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter the location or set to current location",Toast.LENGTH_LONG).show();
                }else if(cur_week != temp_cur_next_week ){
                    Toast.makeText(getApplicationContext(),"Click from the current week to get the forecast",Toast.LENGTH_LONG).show();
                }


                if (!locationtext.getText().toString().equals("Set To Current Location")){
                    String geoURL = geoURLFirst + locationtext.getText().toString() + geoURLLast;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, geoURL, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("JSON","JSON: "+ response);

                                    try {
                                        String sResultArray = response.getString("results");

                                        JSONArray resultsArray = new JSONArray(sResultArray);

//                                    Log.i("JSON ARRAY","JSON ARRAY"+ resultsArray.getJSONObject(0));

                                        String sGeoStats = resultsArray.getJSONObject(0).getString("geometry");

                                        JSONObject latobj = new JSONObject(sGeoStats);
                                        slat = latobj.getString("lat");
                                        slog = latobj.getString("lng");

                                        Log.i("JSON Lat&Log","Lat"+ slat + " Log "+slog);


                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }
                    );

                    MySingleton.getInstance(WeekMonthView.this).addToRequestQue(jsonObjectRequest);
                }



                String fullweatherURL;
                if (locationtext.getText().toString().equals("Set To Current Location")){
                    fullweatherURL = weatherStartURL + gpslat +"&lon="+gpslog+weatherENDURL;

                }else{
                    fullweatherURL = weatherStartURL + slat +"&lon="+slog+weatherENDURL;
                }
                if(cur_week == temp_cur_next_week ){
                    JsonObjectRequest jsonWeatherObjectRequest =  new JsonObjectRequest(Request.Method.GET, fullweatherURL, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
//                                    Log.i("NEW JSON","JSON: "+ response);

                                    try {
                                        String sDailyArray = response.getString("daily");

                                        JSONArray dailyArray = new JSONArray(sDailyArray);

                                        int thisCount = 0;
                                        int mCur_pos = cur_day_pos;
                                        arrayListLarge.clear();
                                        for (int i=0;i<dailyArray.length();i++){
                                            String arrtemp = dailyArray.getJSONObject(i).getString("temp");
                                            JSONObject tempObj = new JSONObject(arrtemp);
                                            String main_temp = tempObj.getString("day");

                                            String arrWeather = dailyArray.getJSONObject(i).getString("weather");
                                            JSONArray weatherArray = new JSONArray(arrWeather);
                                            String weather_mood = weatherArray.getJSONObject(0).getString("main");

                                            String arrfeel_temp = dailyArray.getJSONObject(i).getString("feels_like");
                                            JSONObject feel_tempObj = new JSONObject(arrfeel_temp);
                                            String feel_temp = feel_tempObj.getString("day");

                                            Log.i("WEATHER DATA","main temp:"+ main_temp+" weather_mood"+ weather_mood);

                                            if (mCur_pos<= 6){
                                                arrayList.get(mCur_pos).setmMain_temp(main_temp);
                                                arrayList.get(mCur_pos).setmCondition(weather_mood);
                                                arrayList.get(mCur_pos).setmFeel_temp(feel_temp);
                                                arrayListLarge.add(new SmallDayWeek(main_temp,weather_mood,feel_temp));

                                                thisCount++;
                                            }
                                            mCur_pos+=1;
                                        }
                                        arrayListSmall.clear();
                                        for (int i=thisCount;i<dailyArray.length();i++){
                                            String arrtemp = dailyArray.getJSONObject(i).getString("temp");
                                            JSONObject tempObj = new JSONObject(arrtemp);
                                            String main_temp = tempObj.getString("day");

                                            String arrWeather = dailyArray.getJSONObject(i).getString("weather");
                                            JSONArray weatherArray = new JSONArray(arrWeather);
                                            String weather_mood = weatherArray.getJSONObject(0).getString("main");

                                            String arrfeel_temp = dailyArray.getJSONObject(i).getString("feels_like");
                                            JSONObject feel_tempObj = new JSONObject(arrfeel_temp);
                                            String feel_temp = feel_tempObj.getString("day");

                                            arrayListSmall.add(new SmallDayWeek(main_temp,weather_mood,feel_temp));


                                        }

                                        arrayAdapter.notifyDataSetChanged();
                                        Log.i("JSON WEATHER ARRAY","JSON WEATHER ARRAY"+ dailyArray.getJSONObject(0));


                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("ERROR VOLLEY","GOOD NO " + error);
                                }
                            }
                    );

                    MySingleton.getInstance(WeekMonthView.this).addToRequestQue(jsonWeatherObjectRequest);

                }
                flag_count+=1;
            }
        });


    }
    ///

    //update the week list when week is changed
    void UpdateWeek(){
        weekNameStr="";
        arrayList.clear();

        temp_cur_next_week = 0;
        for (int i = 0;i<7;i++){
//            if(i==0) {
//                now.add(Calendar.DAY_OF_MONTH, curWeekPos);
//                Log.d("mylog::: ", String.valueOf(curWeekPos));
//            }

            weekdays[i] = format.format(now.getTime());

            curDay = dayFormat.format(now.getTime());
            curMonth = monthFormat.format(now.getTime());
            curYear = yearFormat.format(now.getTime());
            if (i==0){
                temp_cur_next_week = Integer.parseInt(dayFormat.format(now.getTime()) + monthFormat.format(now.getTime()));
            }

            arrayList.add(new DayWeek(weekdays[i], curDay, curMonth, curYear));

            if(i==0)
                weekNameStr = weeknameformat.format(now.getTime()) + " - ";
            else if(i==6)
                weekNameStr += weeknameformat.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        try {
            if (cur_next_week == temp_cur_next_week){
                int first_day_next_month = 0;

                for (int i=0;i<arrayListSmall.size();i++){
//                arrayList.get(first_day_next_month).setmTemp(arrayListsmall.get(i).getMain_temp());
                    arrayList.get(first_day_next_month).setmMain_temp(arrayListSmall.get(i).getMain_temp());
                    arrayList.get(first_day_next_month).setmFeel_temp(arrayListSmall.get(i).getFeel_temp());
                    arrayList.get(first_day_next_month).setmCondition(arrayListSmall.get(i).getText_name());
                    first_day_next_month+=1;
                }
            }
            if (cur_week == temp_cur_next_week){

                int cur_pos_temp = cur_day_pos;
                int pos =0;
                for (int i=cur_pos_temp;i<7;i++){

                    arrayList.get(i).setmMain_temp(arrayListLarge.get(pos).getMain_temp());
                    arrayList.get(i).setmFeel_temp(arrayListLarge.get(pos).getFeel_temp());
                    arrayList.get(i).setmCondition(arrayListLarge.get(pos).getText_name());
                    pos+=1;
                }

            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
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