package com.example.koseongmin.parksharing;
/**
 * Retrofit2 인터페이스*/

import android.telecom.CallScreeningService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebEndPoints {
    //GET 방식
    @GET("getAllApartment")         //지도에 띄우기 위해 모든 아파트 정보 가져옴
    Call<ArrayList<ApartmentModel>> getAllApartment();

    //POST 방식
    @POST("join")                   //회원 가입 시 내 정보 전달
    Call<RequestResult> join(@Body UserModel userModel);

    @POST("postReservation")        //예약 정보 전달
    Call<RequestResult> postReservation(@Body ReservationModel reservationModel);

    //PUT 방식
    @PUT("getReservationList")      //내 예약목록 모두 가져옴
    Call<ArrayList<ReservationModel>> getReservationList(@Body UserModel userModel);

    @PUT("getSearchedApartment")    //이름과 주소로 일부 아파트 검색
    Call<ArrayList<ApartmentModel>> getSearchedApartment(@Body ApartmentModel apartmentModel);

    @PUT("getSelectedApartment")     //내 예약목록에서 수정 or 삭제할 목록 선택 시 해당 아파트 정보 불러옴
    Call<ApartmentModel> getSelectedApartment(@Body ApartmentModel apartmentModel);

    @PUT("getLoginUser")            //로그인 할 때 user_id로 검색하고 일치하면 모든 정보 가져옴
    Call<UserModel> getLoginUser(@Body UserModel userModel);

    @PUT("getBookedCount")          //선택된 시간 내에 예약된 목록 개수 세기 위해 리스트 가져옴
    Call<ArrayList<ReservationModel>> getBookedCount(@Body ReservationModel reservationModel);

    @PUT("modifyUser")              //user_index 이용해서 나머지 정보들 수정하기 위해 데이터 전달
    Call<RequestResult> modifyUser(@Body UserModel userModel);

    @PUT("modifyReservation")       //book_index 이용해서 예약정보 수정하기 위해 데이터 전달(회원정보를 수정했을 수 있어서 차번호, 차종, 사용자전화번호도 다시 전송)
    Call<RequestResult> modifyReservation(@Body ReservationModel reservationModel);

    @PUT("deleteReservation")    //book_index를 사용해서 예약정보 삭제(비활성화)
    Call<RequestResult> deleteReservation(@Body ReservationModel reservationModel);

    @PUT("disableUser")           //user_index를 사용해서 회원정보 비활성화(회원 탈퇴)
    Call<RequestResult> disableUser(@Body UserModel userModel);

    @PUT("logoutUser")             //로그아웃 시 user_token을 삭제하기 위해 사용
    Call<RequestResult> logoutUser(@Body UserModel userModel);
}
