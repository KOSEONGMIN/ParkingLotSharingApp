package com.example.koseongmin.parksharing;
/**
 * 아파트 모델*/

public class ApartmentModel {
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
    private int isbookable;

    public int getApt_index() {
        return apt_index;
    }

    public void setApt_index(int apt_index) {
        this.apt_index = apt_index;
    }

    public String getApt_phone() {
        return apt_phone;
    }

    public void setApt_phone(String apt_phone) {
        this.apt_phone = apt_phone;
    }

    public String getApt_name() {
        return apt_name;
    }

    public void setApt_name(String apt_name) {
        this.apt_name = apt_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public int getBookable_count() {
        return bookable_count;
    }

    public void setBookable_count(int bookable_count) {
        this.bookable_count = bookable_count;
    }

    public int getIsbookable() {
        return isbookable;
    }

    public void setIsbookable(int isbookable) {
        this.isbookable = isbookable;
    }
}
