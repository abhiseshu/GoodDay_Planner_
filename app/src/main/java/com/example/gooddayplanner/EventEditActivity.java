package com.example.gooddayplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EventEditActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private Button btnClose;
    private Button btnDelete;
    private Button btnEdit;
    private TextView tbEventName;
    private Button btnPickerDate;
    private TextView lablePickedDate;
    private Button btnPickerStartTime;
    private Button btnPickerEndTime;
    private TextView labelPickedStartTime;
    private TextView labelPickedEndTime;
    private String stringStartTime;
    private String stringEndTime;
    private TextView tbEventLocation;
    private TextView tbEventDescription;

    private boolean editPressed;
    private boolean startPressed;
    private boolean endPressed;
    private Intent intent;
    private int requestCode;
    private Event event;
    private DatabaseHandler db;
    private  Calendar cal;
    private long temp_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        //Initialise the variables
        btnClose = findViewById(R.id.btn_close);
        btnDelete = findViewById(R.id.btn_delete);
        btnEdit = findViewById(R.id.btn_edit);
        tbEventName = findViewById(R.id.tb_event_name);
        btnPickerDate = findViewById(R.id.btn_date_picker);
        lablePickedDate = findViewById(R.id.lable_picked_date);
        btnPickerStartTime = findViewById(R.id.btn_picker_start_time);
        btnPickerEndTime = findViewById(R.id.btn_picker_end_time);
        labelPickedStartTime = findViewById(R.id.label_picked_start_time);
        labelPickedEndTime = findViewById(R.id.label_picked_end_time);
        stringStartTime = getResources().getString(R.string.lable_date);
        stringEndTime = getResources().getString(R.string.lable_end_time);

        tbEventLocation = findViewById(R.id.tb_event_location);
        tbEventDescription = findViewById(R.id.tb_event_description);

        editPressed = false;
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
        event.setLocation(intent.getStringExtra("LOCATION"));
        event.setDescription(intent.getStringExtra("DESCRIPTION"));

        temp_date = intent.getLongExtra("DATE",0);

        try {
            if (!intent.getStringExtra("DESCRIPTION").isEmpty()) {
                event.setDescription(intent.getStringExtra("DESCRIPTION"));
            }
            if (!intent.getStringExtra("LOCATION").isEmpty()) {
                event.setLocation(intent.getStringExtra("LOCATION"));
            }
        }
        catch (Exception e){
            Log.i("EVENT EDIT EXCEPTION", " " + e);
        }

        db = DatabaseHandler.getInstance(this);

        //Initialise the listeners
        btnClose.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnPickerDate.setOnClickListener(this);
        btnPickerStartTime.setOnClickListener(this);
        btnPickerEndTime.setOnClickListener(this);
//
//        tbEventName.setFocusable(false);
//        tbEventLocation.setFocusable(false);
//        tbEventDescription.setFocusable(false);

        Calendar c = Calendar.getInstance();
//Set time in milliseconds
        c.setTimeInMillis(event.getDate());
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        tbEventName.setText(event.getName());
        lablePickedDate.setText(mDay + "/" + (mMonth+1) + "-" + mYear);
        labelPickedStartTime.setText(intent.getStringExtra("EXTRA_START"));
        labelPickedEndTime.setText(intent.getStringExtra("EXTRA_END"));
        tbEventLocation.setText(event.getLocation());
        tbEventDescription.setText(event.getDescription());

    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void onClick(View v) {
        if (v.getId() == btnClose.getId()) {
            finish();
        } else if (v.getId() == btnDelete.getId()) {
            Log.i("dfsf","dfsf");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete event");
            builder.setMessage("Are you sure you want to delete this event?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) -> {
                        dialog.cancel();
                        db.deleteEvent(event);
                        finishActivity(requestCode);
                        finish();
                    }
            );

            builder.setNegativeButton(
                    "No",
                    (dialog, i) -> dialog.cancel()
            );

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (v.getId() == btnEdit.getId()) {
            if (editPressed) {
                if (event.getEnd() > 0 && tbEventName.getText().length() > 0) {
                    Calendar c = Calendar.getInstance();
                    event.setName(tbEventName.getText().toString());
                        event.setStart(event.getDate() + event.getStart());
                        event.setEnd(event.getDate() + event.getEnd());

                    event.setLocation(tbEventLocation.getText().toString());
                    event.setDescription(tbEventDescription.getText().toString());

                    long i = db.updateEvent(event);

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
                        finish();
                        finishActivity(requestCode);
                    }
                } else if (tbEventName.getText().length() == 0) {
                    tbEventName.setError("You must input a name for the event");
                } else if (event.getEnd() == 0) {
                    Toast toast = Toast.makeText(this, "You must input start and end times to create an event", Toast.LENGTH_LONG);
                    toast.show();
                }
            } else {
                Log.i("SET FOCUS", "CHECK");
                Toast.makeText(this, "You can edit now!!", Toast.LENGTH_LONG).show();
                tbEventName.setFocusable(true);
                tbEventLocation.setFocusable(true);
                tbEventDescription.setFocusable(true);
                editPressed = true;
            }
        } else if (v.getId() == btnPickerDate.getId() && editPressed) {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date");
        } else if (v.getId() == btnPickerStartTime.getId() && event.getDate() > 0 && editPressed) {
            DialogFragment startTimePicker = new TimePickerFragment();
            startTimePicker.show(getSupportFragmentManager(), "time start");

            startPressed = true;
        } else if (v.getId() == btnPickerEndTime.getId() && event.getStart() > 0 && editPressed) {
            DialogFragment endTimePicker = new TimePickerFragment();
            endTimePicker.show(getSupportFragmentManager(), "time end");

            endPressed = true;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        lablePickedDate.setText(day + "/" + (month+1) + "-" + year);
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, day);
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
