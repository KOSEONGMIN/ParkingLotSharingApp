package com.example.koseongmin.parksharing;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.MarkerIcons;

public class ParkingLot {
    private int apt_index;
    private String apt_phone;
    private String apt_name;
    private String address;
    private double latitude;
    private double longitude;
    private int fee;
    private String open_time;
    private String close_time;
    private int bookable_count;
    private int isbookable;      //String 형으로 할지 int 형으로 할지 결정해야함

    public ParkingLot(int apt_index, String apt_phone, String apt_name, String address, double latitude, double longitude, int fee, String open_time, String close_time, int bookable_count, int isbookable) {
        this.apt_index = apt_index;
        this.apt_phone = apt_phone;
        this.apt_name = apt_name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fee = fee;
        this.open_time = open_time;
        this.close_time = close_time;
        this.bookable_count = bookable_count;
        this.isbookable = isbookable;
    }

    public int getApt_index() {
        return apt_index;
    }

    public String getApt_phone() {
        return apt_phone;
    }

    public String getApt_name() {
        return apt_name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getFee() {
        return fee;
    }

    public String getOpen_time() {
        return open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public int getBookable_count() {
        return bookable_count;
    }

    public int getIsbookable() {
        return isbookable;
    }
}
