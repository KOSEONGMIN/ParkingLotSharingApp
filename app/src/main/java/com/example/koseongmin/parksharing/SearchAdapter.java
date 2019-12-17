package com.example.koseongmin.parksharing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {
    Context mContext;
    int searchlist_item;
    ArrayList<SearchListItem> items;
    LayoutInflater inflater;

    public SearchAdapter(Context context, int searchlist_item, ArrayList<SearchListItem> items){
        mContext = context;
        this.searchlist_item = searchlist_item;
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

    public void addItem(SearchListItem item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchListLayout searchListLayout = null;

        if(convertView == null){
            searchListLayout = new SearchListLayout(mContext);
        } else {
            searchListLayout = (SearchListLayout) convertView;
        }

        SearchListItem item = items.get(position);

        if (item.getIsbookable() == 1){
            searchListLayout.setCheck(true);
            searchListLayout.setIsbookable("예약가능");
        }else {
            searchListLayout.setCheck(false);
            searchListLayout.setIsbookable("예약불가");
        }

        searchListLayout.setApt_name(item.getApt_name());

        searchListLayout.setAddress(item.getAddress());

        searchListLayout.setBookableTime(item.getBookable_time());

        searchListLayout.setFee(item.getFee() + "원/15분");

        return  searchListLayout;
    }
}
