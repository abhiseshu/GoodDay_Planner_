package com.example.gooddayplanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EventCreateActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Button btnClose;
    private Button btnSave;
    private TextView tbEventName;
    private Button btnPickerDate;
    private TextView lablePickedDate;
    private Button btnPickerStartTime;
    private Button btnPickerEndTime;
    private TextView labelPickedStartTime;
    private TextView labelPickedEndTime;
    private String stringStartTime;
    private String stringEndTime;
    private TextView tbEventDescription;
    private TextView tbEventLocation;


    private boolean startPressed;
    private boolean endPressed;
    private Intent intent;
    private int requestCode;
    private Event event;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        //Initialise the variables
        btnClose = findViewById(R.id.btn_close);
        btnSave = findViewById(R.id.btn_edit);
        tbEventName = findViewById(R.id.tb_event_name);
        btnPickerDate = findViewById(R.id.btn_date_picker);
        lablePickedDate = findViewById(R.id.lable_picked_date);
        btnPickerStartTime = findViewById(R.id.btn_picker_start_time);
        btnPickerEndTime = findViewById(R.id.btn_picker_end_time);
        labelPickedStartTime = findViewById(R.id.label_picked_start_time);
        labelPickedEndTime = findViewById(R.id.label_picked_end_time);
        stringStartTime = getResources().getString(R.string.lable_date);
        stringEndTime = getResources().getString(R.string.lable_end_time);
        tbEventDescription = findViewById(R.id.tb_event_description);
        tbEventLocation = findViewById(R.id.tb_event_location);

        startPressed = false;
        endPressed = false;
        intent = getIntent();
        requestCode = intent.getIntExtra("REQUEST_CODE", 0);
        event = new Event();
        event.setID(intent.getLongExtra("ID", -1));
        event.setName(intent.getStringExtra("NAME"));
        event.setDate(intent.getLongExtra("DATE", 0));
        event.setStart(intent.getLongExtra("START", 0));
        event.setEnd(intent.getLongExtra("END", 0));
        try {
            if (!intent.getStringExtra("DESCRIPTION").isEmpty()) {
                event.setLocation(intent.getStringExtra("LOCATION"));
                event.setDescription(intent.getStringExtra("DESCRIPTION"));
            }
        }catch (NullPointerException e){
            Log.i("TAG", "onCreate: " + e);
        }

        db = DatabaseHandler.getInstance(this);

        //Initialise the listeners
        btnClose.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnPickerDate.setOnClickListener(this);
        btnPickerStartTime.setOnClickListener(this);
        btnPickerEndTime.setOnClickListener(this);


    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void onClick(View v) {

        if (v.getId() == btnClose.getId()) {
            Log.i("Request Code","RC" + requestCode);
            finish();
        } else if (v.getId() == btnSave.getId()) {
            if (event.getEnd() > 0 && tbEventName.getText().length() > 0) {
                Calendar c = Calendar.getInstance();
                event.setName(tbEventName.getText().toString());
                event.setStart(event.getDate() + event.getStart());
                event.setEnd(event.getDate() + event.getEnd());
                event.setLocation(tbEventLocation.getText().toString());
                event.setDescription(tbEventDescription.getText().toString());
                long i = db.addEvent(event);

                if (i < 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error");
                    builder.setMessage("An error has occurred saving the event");
                    builder.setCancelable(true);

                    builder.setNeutralButton(
                            "Ok",
                            (dialog, id) -> dialog.cancel()
                    );

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        NotificationChannel channel = new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager manager = getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(channel);
                    }
//                    Calendar casl = Calendar.getInstance();
//
//                    casl.setTimeInMillis(event.getDate());
//                    int mYear = casl.get(Calendar.YEAR);
//                    int mMonth = casl.get(Calendar.MONTH);
//                    int mDay = casl.get(Calendar.DAY_OF_MONTH);


//                    Log.i("CREate event date",""+ event.getDate() + " " +mDay+" " + mMonth + " " + mYear);


                    String message = "New Event is added: " + event.getName();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.putExtra("day",mDay+"");
//                    intent.putExtra("month",(mMonth+1)+"");
//                    intent.putExtra("year",mYear+"");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.cloud)
                            .setContentInfo("New Notification")
                            .setContentText(message)
                            .setChannelId("My Notification")
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

//                    Intent notifi_intent = new Intent(getApplicationContext(),MainActivity.class);
//                    notifi_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,notifi_intent,PendingIntent.FLAG_UPDATE_CURRENT);
//                    builder.setContentIntent(pendingIntent);

//                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                    notificationManager.notify(0,builder.build());
                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                    managerCompat.notify(1,builder.build());


                    Snackbar.make(btnSave, "Event", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    finish();

                    //finishActivity(requestCode);
                }
            } else if (tbEventName.getText().length() == 0) {
                tbEventName.setError("You must input a name for the event");
            } else if (event.getEnd() == 0) {
                Toast toast = Toast.makeText(this, "You must input start and end times to create an event", Toast.LENGTH_LONG);
                toast.show();
            }
            //test
//            SimpleDateFormat s1 = new SimpleDateFormat("dd/MM/yyyy");
//            SimpleDateFormat s2 = new SimpleDateFormat("ddMMyyyy");
//            Date d = null;
//            try {
//                d = s1.parse("12/01/2021");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            String s3 = s2.format(d);
//            Log.i("DATE","CHECK"+ d);
////            long l = 1613170800000;
////            Log.i("DATE","IN LONG"+l);
//
//            GregorianCalendar gregorianCalendar = new GregorianCalendar(2021, 1, 13);
//            Log.i("DATE CHECK","TOP REAL IS "+ gregorianCalendar.getTimeInMillis());
//
//            Event obj[] = db.getEventsForDay(gregorianCalendar.getTimeInMillis());
//            for(int i=0;i<obj.length;i++){
//                Log.i("EVENT INFO",obj[i].getName() + " " + obj[i].getStart() + " " + obj[i].getEnd() + " " + obj[i].getDate()+" "+ obj[i].getLocation() +" "+ obj[i].getDescription() +" " + obj[i].getID());
//            }

//            finish();
        } else if (v.getId() == btnPickerDate.getId()) {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date");
        } else if (v.getId() == btnPickerStartTime.getId() && event.getDate() > 0) {
            DialogFragment startTimePicker = new TimePickerFragment();
            startTimePicker.show(getSupportFragmentManager(), "time start");

            startPressed = true;
        } else if (v.getId() == btnPickerEndTime.getId() && event.getStart() > 0) {
            DialogFragment endTimePicker = new TimePickerFragment();
            endTimePicker.show(getSupportFragmentManager(), "time end");

            endPressed = true;
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        lablePickedDate.setText(day + "/" + (month+1) + "-" + year);
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, day);
        Log.i("DATE CHECK","REAL IS "+ gregorianCalendar.getTimeInMillis());
        event.setDate(gregorianCalendar.getTimeInMillis());
        event.setStart(0);
        event.setEnd(0);
        labelPickedStartTime.setText(stringStartTime);
        labelPickedEndTime.setText(stringEndTime);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if (startPressed) {
            if (minute < 10) {
                labelPickedStartTime.setText(hour + ":0" + minute);
            } else {
                labelPickedStartTime.setText(hour + ":" + minute);
            }
            event.setStart(hour * 3600000 + minute * 60000);
            event.setEnd(0);
            labelPickedEndTime.setText(stringEndTime);
            startPressed = false;
        } else if (endPressed) {
            long time = hour * 3600000 + minute * 60000;
            if (time > event.getStart()) {
                if (minute < 10) {
                    labelPickedEndTime.setText(hour + ":0" + minute);
                } else {
                    labelPickedEndTime.setText(hour + ":" + minute);
                }
                event.setEnd(time);
                endPressed = false;
            }
        }
    }
}