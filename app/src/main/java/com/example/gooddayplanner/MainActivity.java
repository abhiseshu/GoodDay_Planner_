package com.example.gooddayplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    int curday;
    String curMonth;
    String curYear;
    SimpleDateFormat monthFormat;
    SimpleDateFormat yearFormat;
    private DatabaseHandler db;
    Button locationBn;

    String geoURLFirst = "https://api.opencagedata.com/geocode/v1/json?q=";
    String geoURLLast = "&key=8bf8429dedde457cb534f43c1fafb7e1";
    String weatherStartURL = "https://api.openweathermap.org/data/2.5/onecall?lat=";
    String slat;
    String slog;
    String weatherENDURL = "&exclude=hourly,minutely&appid=bb9604dfab472b092e402372cddf4413";
    int flag_count;
    int cur_day_pos;
    int first_day_cur_pos;
    int last_day_pos;
    ArrayList<SmallDayMonth> arrayListsmall;
    ArrayList<SmallDayMonth> arrayListlarge;
    int right_next_month;
    int right_cur_month;
    int right_cur_year;
    boolean pressed = false;
    EditText locationtext;

    //GPS
    private Button gpsButton;
    private LocationManager locationManager;
    private LocationListener locationListener;
    double gpslat;
    double gpslog;

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
        setContentView(R.layout.activity_main);

        cal = Calendar.getInstance();

        locationtext = (EditText) findViewById(R.id.location_text_id);

        format = new SimpleDateFormat("dd");

        int curday = Integer.parseInt(format.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        flag_count = 0;
        db = DatabaseHandler.getInstance(this);

        gridView = (GridView) findViewById(R.id.girdviewid);


        monthFormat = new SimpleDateFormat("MM");

        yearFormat = new SimpleDateFormat("yyyy");

        arrayList = new ArrayList<DayMonth>();

        arrayListsmall = new ArrayList<SmallDayMonth>();

        arrayListlarge = new ArrayList<SmallDayMonth>();

        monthText = (TextView) findViewById(R.id.monthyearid);

        monthText.setText(new SimpleDateFormat("MMM, yyyy").format(cal.getTime()));

        curMonth = monthFormat.format(cal.getTime());

        curYear = yearFormat.format(cal.getTime());

        right_next_month = Integer.parseInt(curMonth) + 1;
        right_cur_month = Integer.parseInt(curMonth);
        right_cur_year = Integer.parseInt(curYear);


        countday = 0;
        monthDays = new String[37];
        for (int i = 0; i < 37; i++) {
            monthDays[i] = "";
            arrayList.add(i, new DayMonth(monthDays[i]));
            arrayList.get(i).setmTemp("");
        }

        //gives the position of first day
        if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            countday = cal.get(Calendar.DAY_OF_WEEK) - 2;
            Log.d("countday is", ":" + countday + "and" + cal.get(Calendar.DAY_OF_WEEK));
        } else {
            countday = 6;
        }

        first_day_cur_pos = countday;
        //setting the current month first day in the calendar
        cal.set(Calendar.DAY_OF_MONTH, 1);
        myMonth = cal.get(Calendar.MONTH);

        while (myMonth == cal.get(Calendar.MONTH)) {
            monthDays[countday] = format.format(cal.getTime());
            arrayList.get(countday).setmDay(monthDays[countday]);
            if (Integer.parseInt(monthDays[countday]) == curday)
                cur_day_pos = countday;
            Log.d("tag", String.valueOf(cal.getTime()) + "day count is" + countday);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            countday += 1;
            curMonthDays += 1;
        }

        last_day_pos = countday - 1;
        updateIndicatorOnMonth();

        //is event on that day
//        for (int j=0;j<37;j++){
//
//            String tempDay = arrayList.get(j).getmDay();
//
//            if(tempDay == "")
//                continue;
//            GregorianCalendar gregorianCalendar = new GregorianCalendar(Integer.parseInt(curYear), Integer.parseInt(curMonth) -1, Integer.parseInt(tempDay));
//            Log.i("MAIN ACTIVITY",j+" NO: "+ tempDay+" "+curMonth+" "+curYear +" "+ gregorianCalendar.getTimeInMillis());
//
//            Event obj[] = db.getEventsForDay(gregorianCalendar.getTimeInMillis());
//            for(int i=0;i<obj.length;i++){
//                Log.i("EVENT INFO",obj[i].getName() + " " + obj[i].getStart() + " " + obj[i].getEnd() + " " + obj[i].getDate()+" "+ obj[i].getLocation() +" "+ obj[i].getDescription() +" " + obj[i].getID());
//            }
//            if (obj.length >= 1){
//                arrayList.get(j).setEvent(true);
//            }
//
//        }

        dayMonthAdapter = new DayMonthAdapter(getApplicationContext(), R.layout.monthdayitem, arrayList);

        gridView.setAdapter(dayMonthAdapter);

        //
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
        gpsButton = (Button) findViewById(R.id.gps_location_id);
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

                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},10);

                    }else{
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,50000,0,locationListener);

                        Location xlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        try {
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

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                updateIndicatorOnMonth();
                dayMonthAdapter.notifyDataSetChanged();
            }
        }, delay);

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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("CLICK POS", "POS IS" + arrayList.get(i).getmDay());

                Intent dayIntent = new Intent(getApplicationContext(),DaysViewActivity.class);
                dayIntent.putExtra("day",arrayList.get(i).getmDay());
                dayIntent.putExtra("month",curMonth);
                dayIntent.putExtra("year",curYear);
                startActivity(dayIntent);
            }
        });


        //Location api working
        locationBn = (Button) findViewById(R.id.locationMonthBnid);
        locationBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAP","TAPPED");

                ///
                ///
                ///
                if (locationtext.getText().toString() == null || locationtext.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter the location or set to current location",Toast.LENGTH_LONG).show();
                }
                else if(right_cur_month != Integer.parseInt(curMonth)){
                    Toast.makeText(getApplicationContext(),"Click from the current month to get the forecast",Toast.LENGTH_LONG).show();
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

                                        Log.i("JSON ARRAY","JSON ARRAY"+ resultsArray.getJSONObject(0));

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

                    MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);

                }
//                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                ///
                ///
                ///
//                try {
//                    Thread.sleep(1500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

//                showWorkingDialog();
//
//                new Handler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        removeWorkingDialog();
//                    }
//
//                },2000);
                String fullweatherURL;
                Log.i("loc text",locationtext.getText().toString());
                if (locationtext.getText().toString().equals("Set To Current Location")){
                    fullweatherURL = weatherStartURL + gpslat +"&lon="+gpslog+weatherENDURL;
                    Log.i("loc two text",locationtext.getText().toString() + fullweatherURL);

                }else{
                    fullweatherURL = weatherStartURL + slat +"&lon="+slog+weatherENDURL;
                }

            if(right_cur_month == Integer.parseInt(curMonth)){
                JsonObjectRequest jsonWeatherObjectRequest =  new JsonObjectRequest(Request.Method.GET, fullweatherURL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("NEW JSON","JSON: "+ response);

                                try {
                                    String sDailyArray = response.getString("daily");

                                    JSONArray dailyArray = new JSONArray(sDailyArray);

                                    int thisCount = 0;
                                    int mCur_pos = cur_day_pos;
                                    arrayListlarge.clear();
                                    for (int i=0;i<dailyArray.length();i++){
                                        String arrtemp = dailyArray.getJSONObject(i).getString("temp");
                                        JSONObject tempObj = new JSONObject(arrtemp);
                                        String main_temp = tempObj.getString("day");

                                        String arrWeather = dailyArray.getJSONObject(i).getString("weather");
                                        JSONArray weatherArray = new JSONArray(arrWeather);
                                        String weather_mood = weatherArray.getJSONObject(0).getString("main");

                                        Log.i("WEATHER DATA","main temp:"+ main_temp+" weather_mood"+ weather_mood);

                                        if (mCur_pos<= last_day_pos){
                                            arrayList.get(mCur_pos).setmTemp(main_temp);
                                            arrayList.get(mCur_pos).setmWeather_mood(weather_mood);
                                            arrayListlarge.add(new SmallDayMonth(main_temp,weather_mood));

                                            thisCount++;

                                        }
                                        mCur_pos+=1;

                                    }
                                    arrayListsmall.clear();
                                    for (int i=thisCount;i<dailyArray.length();i++){
                                        String arrtemp = dailyArray.getJSONObject(i).getString("temp");
                                        JSONObject tempObj = new JSONObject(arrtemp);
                                        String main_temp = tempObj.getString("day");

                                        String arrWeather = dailyArray.getJSONObject(i).getString("weather");
                                        JSONArray weatherArray = new JSONArray(arrWeather);
                                        String weather_mood = weatherArray.getJSONObject(0).getString("main");

                                        arrayListsmall.add(new SmallDayMonth(main_temp,weather_mood));




                                    }

                                    dayMonthAdapter.notifyDataSetChanged();
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

                MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonWeatherObjectRequest);

            }
                flag_count+=1;
            }
        });


    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Switch(requestCode){
//            case 10:
//                if (grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED)
//                    configure
//        }
//    }

    ///
    private ProgressDialog working_dialog;

    private void showWorkingDialog() {
        working_dialog = ProgressDialog.show(this, "","Working please wait...", true);
    }

    private void removeWorkingDialog() {
        if (working_dialog != null) {
            working_dialog.dismiss();
            working_dialog = null;
        }
    }

    ///

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

        curMonth = monthFormat.format(cal.getTime());

        curYear = yearFormat.format(cal.getTime());

        for(int i = 0; i<37;i++){
            monthDays[i] = "";
            arrayList.get(i).setmDay(monthDays[i]);
            arrayList.get(i).setmTemp("");
            arrayList.get(i).setmWeather_mood(null);
        }
        dayMonthAdapter.notifyDataSetChanged();

        if(cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
            countday = cal.get(Calendar.DAY_OF_WEEK) - 2;
            Log.d("tag", String.valueOf(countday));
        }else
            countday = 6;

        try{
            if (right_next_month == Integer.parseInt(curMonth) && right_cur_year == Integer.parseInt(curYear) ){
                int first_day_next_month = countday;

                for (int i=0;i<arrayListsmall.size();i++){
                    arrayList.get(first_day_next_month).setmTemp(arrayListsmall.get(i).getMain_temp());
                    arrayList.get(first_day_next_month).setmWeather_mood(arrayListsmall.get(i).getText_name());
                    first_day_next_month+=1;
                }
            }
            if (right_cur_month == Integer.parseInt(curMonth) && right_cur_year == Integer.parseInt(curYear) ){

                int cur_pos_temp = cur_day_pos;
                int pos =0;
                for (int i=cur_pos_temp;i<=last_day_pos;i++){
                    arrayList.get(i).setmTemp(arrayListlarge.get(pos).getMain_temp());
                    arrayList.get(i).setmWeather_mood(arrayListlarge.get(pos).getText_name());
                    pos+=1;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }


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

        updateIndicatorOnMonth();

        dayMonthAdapter.notifyDataSetChanged();

    }

    void updateIndicatorOnMonth(){

        for (int j=0;j<37;j++){

            String tempDay = arrayList.get(j).getmDay();
            arrayList.get(j).setEvent(false);
            if(tempDay == "")
                continue;
            GregorianCalendar gregorianCalendar = new GregorianCalendar(Integer.parseInt(curYear), Integer.parseInt(curMonth) -1, Integer.parseInt(tempDay));
//            Log.i("MAIN ACTIVITY",j+" NO: "+ tempDay+" "+curMonth+" "+curYear +" "+ gregorianCalendar.getTimeInMillis());

            try {
                Event obj[] = db.getEventsForDay(gregorianCalendar.getTimeInMillis());
                for(int i=0;i<obj.length;i++){
//                    Log.i("EVENT INFO",obj[i].getName() + " " + obj[i].getStart() + " " + obj[i].getEnd() + " " + obj[i].getDate()+" "+ obj[i].getLocation() +" "+ obj[i].getDescription() +" " + obj[i].getID());
                }
                if (obj.length >= 1){
                    arrayList.get(j).setEvent(true);
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }



}