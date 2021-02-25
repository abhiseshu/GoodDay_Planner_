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

public class DayEventsAdapter extends ArrayAdapter<Event> {
    public DayEventsAdapter(@NonNull Context context, int resource, @NonNull List<Event> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //return super.getView(position, convertView, parent);

        View ListItemView = convertView;
        if (ListItemView == null) {
            ListItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.event_view_layout, parent, false);
        }

        Event event = (Event) getItem(position);

        TextView mEvent = (TextView) ListItemView.findViewById(R.id.event_name_day_id);

        //TextView mTime = (TextView) ListItemView.findViewById(R.id.event_time_day_id);

        TextView mLocation = (TextView) ListItemView.findViewById(R.id.event_location_day_id);

        TextView mDesc = (TextView) ListItemView.findViewById(R.id.event_desc_day_id);

        TextView mtime = (TextView) ListItemView.findViewById(R.id.event_time_day_id);

        Log.i("Day Event Adapter"," "+ event.getName());

        mEvent.setText(event.getName());

        mLocation.setText(event.getLocation());

        mDesc.setText(event.getDescription());

        long realStartTime  = event.getStart() - event.getDate() ;
        double mStartTime = (double) realStartTime/3600000;

        //start time
        int mStartHour = (int) mStartTime;
        double mtempRatio = mStartTime - (double) mStartHour;
        int mStartMinutes = (int) (mtempRatio * 60);

        String mRealStartTime;

        if (mStartMinutes < 10)
            mRealStartTime = mStartHour +":0"+ mStartMinutes;
        else
            mRealStartTime = mStartHour +":"+ mStartMinutes;

        //end time
        long realEndTime  = event.getEnd() - event.getDate() ;
        double mEndTime = (double) realEndTime/3600000;


        int mEndHour = (int) mEndTime;
        double mEndRatio = mEndTime - (double) mEndHour;
        int mEndMinutes = (int) (mEndRatio * 60);

        String mRealEndTime;

        if (mEndMinutes < 10)
            mRealEndTime = mEndHour +":0"+ mEndMinutes;
        else
            mRealEndTime = mEndHour +":"+ mEndMinutes;

        mtime.setText(mRealStartTime+"-"+mRealEndTime);

        return ListItemView;

    }
}
