package com.example.koseongmin.parksharing;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchListLayout extends LinearLayout {
    Context mContext;
    LayoutInflater inflater;

    TextView apt_name, address, isbookable, bookableTime, fee;
    boolean check = true;

    public SearchListLayout(Context context){
        super(context);

        mContext = context;
        init();
    }

    public SearchListLayout(Context context, AttributeSet attrs){
        super(context, attrs);

        mContext = context;
        init();
    }

    private void init() {
        inflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.searchlist_item, this, true);

        apt_name = (TextView)findViewById(R.id.name);
        address = (TextView)findViewById(R.id.address);
        isbookable = (TextView)findViewById(R.id.isbookable);
        bookableTime = (TextView)findViewById(R.id.bookableTime);
        fee = (TextView)findViewById(R.id.fee);

    }

    public void setCheck(boolean check1){
        this.check = check1;
    }

    public void setApt_name(String name1){
        apt_name.setText(name1);

    }

    public void setAddress(String address1){
        address.setText(address1);

    }

    public void setIsbookable(String isbookable1){
        isbookable.setText(isbookable1);

    }

    public void setBookableTime(String bookableTime1){
        bookableTime.setText(bookableTime1);

    }

    public void setFee(String fee1){
        fee.setText(fee1);

    }
}
