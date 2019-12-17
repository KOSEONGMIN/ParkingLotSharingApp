package com.example.koseongmin.parksharing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ReservationListAdapter extends BaseAdapter {
    Context mContext;
    int reservationlist_item;
    ArrayList<ReservationListItem> items;
    LayoutInflater inflater;

    public ReservationListAdapter(Context context, int reservationlist_item, ArrayList<ReservationListItem> items){
        mContext = context;
        this.reservationlist_item = reservationlist_item;
        this.items = items;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(ReservationListItem item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReservationListLayout reservationListLayout = null;

        if (convertView == null) {
            reservationListLayout = new ReservationListLayout(mContext);
        } else {
            reservationListLayout = (ReservationListLayout) convertView;
        }

        ReservationListItem item = items.get(position);

        if (item.getIs_used() == 0){
            reservationListLayout.setCheck(true);
        }else {
            reservationListLayout.setCheck(false);
        }

        reservationListLayout.setApt_name(item.getApt_name());

        reservationListLayout.setReservationTime(item.getStart_time() + " ~ " +item.getFinish_time());

        reservationListLayout.setBill(item.getBill() + "원");

        return reservationListLayout;
    }
}
