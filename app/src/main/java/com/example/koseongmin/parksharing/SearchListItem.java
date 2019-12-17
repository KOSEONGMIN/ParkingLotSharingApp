package com.example.koseongmin.parksharing;

public class SearchListItem {
    private int apt_index;
    private String apt_phone;
    private String apt_name;
    private String address;
    private String open_time;
    private String close_time;
    private int fee;
    private int bookable_count;
    private int isbookable;

    private String bookable_time;           //booable_time은 "00:00~00:00"의 모양으로 띄우기 위해 임의로 만든 변수

    public SearchListItem(int apt_index, String apt_phone, String apt_name, String address, String open_time, String close_time, int fee, int bookable_count, int isbookable) {
        this.apt_index = apt_index;
        this.apt_phone = apt_phone;
        this.apt_name = apt_name;
        this.address = address;
        this.open_time = open_time;
        this.close_time = close_time;
        this.fee = fee;
        this.bookable_count = bookable_count;
        this.isbookable = isbookable;
        if((open_time.equals("00:00")) && (close_time.equals("00:00"))){
            this.bookable_time = "제한 없음";
        }else{
            this.bookable_time = open_time + "~" + close_time + "시";
        }

    }

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

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
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

    public String getBookable_time() {
        return bookable_time;
    }

    public void setBookable_time(String bookable_time) {
        this.bookable_time = bookable_time;
    }
}
