package com.example.koseongmin.parksharing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReservationListLayout extends LinearLayout {
    Context mContext;
    LayoutInflater inflater;

    TextView apt_name, reservationTime, bill;
    boolean check = true;

    public ReservationListLayout(Context context){
        super(context);

        mContext = context;
        init();
    }

    public ReservationListLayout(Context context, AttributeSet attrs){
        super(context, attrs);

        mContext = context;
        init();
    }

    private void init() {
        inflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.reservationlist_item, this, true);

        apt_name = (TextView)findViewById(R.id.name);
        reservationTime = (TextView)findViewById(R.id.reservationTime);
        bill = (TextView)findViewById(R.id.bill);
    }

    public void setCheck(boolean check1){
        this.check = check1;
    }

    public void setApt_name(String name1){
        apt_name.setText(name1);

    }

    public void setReservationTime(String reservationTime1){
        reservationTime.setText(reservationTime1);

    }

    public void setBill(String bill1){
        bill.setText(bill1);

    }
}
