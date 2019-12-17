package com.example.koseongmin.parksharing;

import java.util.Date;

public class ReservationModel {
    private int book_index;
    private int user_index;
    private int apt_index;
    private String apt_name;
    private String car_number;
    private String car_type;
    private String user_phone;
    private String start_time;
    private String finish_time;
    private long bill;
    private int is_used;

    public int getBook_index() {
        return book_index;
    }

    public void setBook_index(int book_index) {
        this.book_index = book_index;
    }

    public int getUser_index() {
        return user_index;
    }

    public void setUser_index(int user_index) {
        this.user_index = user_index;
    }

    public int getApt_index() {
        return apt_index;
    }

    public void setApt_index(int apt_index) {
        this.apt_index = apt_index;
    }

    public String getApt_name() {
        return apt_name;
    }

    public void setApt_name(String apt_name) {
        this.apt_name = apt_name;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
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

    public int getIs_used() {
        return is_used;
    }

    public void setIs_used(int is_used) {
        this.is_used = is_used;
    }
}
