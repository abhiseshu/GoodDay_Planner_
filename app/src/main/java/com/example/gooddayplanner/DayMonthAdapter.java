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

        if(month.getmImgURL()!=null) {
//            ImageRequest imageRequest = new ImageRequest(month.getmImgURL(),
//                    new Response.Listener<Bitmap>() {
//                        @Override
//                        public void onResponse(Bitmap response) {
//                            mimageView.setImageBitmap(response);
//                        }
//                    }, 0, 0, null,
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.i("Image access denied", "Volley: " + error);
//                        }
//                    });
            Log.i("RUN","RUNNING");
        }
        TextView mtemp = (TextView) GridItemView.findViewById(R.id.winfoid);
        if(month.getmTemp()!=null){
            Log.i("RUN","RUNNING");
            mtemp.setText(month.getmTemp());
        }

        return GridItemView;

    }
}
