package com.example.koseongmin.parksharing;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSender {
    public static final String BASE_URL = "http://172.30.1.54:8080/android/";
    private static Retrofit retrofit = null;
    private static WebEndPoints endPoints = null;

    public static WebEndPoints getEndPoint(){
        if(endPoints ==null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                    .build();
            endPoints = retrofit.create(WebEndPoints.class);
        }
        return endPoints;
    }


}
