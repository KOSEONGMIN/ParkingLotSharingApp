package com.example.koseongmin.parksharing;

import java.util.Date;

public class ReservationListItem {
    private int book_index;
    private int apt_index;
    private String start_time;
    private String finish_time;
    private long bill;
    private String apt_name;
    private int is_used;

    public ReservationListItem(int book_index, int apt_index, String start_time, String finish_time, long bill, String apt_name,  int is_used) {
        this.book_index = book_index;
        this.apt_index = apt_index;
        this.start_time = start_time;
        this.finish_time = finish_time;
        this.bill = bill;
        this.apt_name = apt_name;
        this.is_used = is_used;
    }

    public int getBook_index() {
        return book_index;
    }

    public void setBook_index(int book_index) {
        this.book_index = book_index;
    }

    public int getApt_index() {
        return apt_index;
    }

    public void setApt_index(int apt_index) {
        this.apt_index = apt_index;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public long getBill() {
        return bill;
    }

    public void setBill(long bill) {
        this.bill = bill;
    }

    public String getApt_name() {
        return apt_name;
    }

    public void setApt_name(String apt_name) {
        this.apt_name = apt_name;
    }

    public int getIs_used() {
        return is_used;
    }

    public void setIs_used(int is_used) {
        this.is_used = is_used;
    }
}
