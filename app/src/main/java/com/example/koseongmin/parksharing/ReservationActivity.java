package com.example.koseongmin.parksharing;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity {
    Button confirmButton, cancelButton;
    TextView parkingName, bookableCount, parkingAddress, parkingTime, parkingFee, totalPrice;
    TextView reservationStartTime, reservationFinishTime;
    String startD, startT;
    String finishD, finishT;
    boolean isStartSelected = false;
    int startYear, startMonth, startDate, startHour, startMinute;
    int finishYear, finishMonth, finishDate, finishHour, finishMinute;
    Calendar cal = Calendar.getInstance();       //시작시간으로 선택된 년월일시분 중 시와 분을 바꾸기 위한 변수
    Calendar startCal = Calendar.getInstance();
    Calendar finishCal = Calendar.getInstance();
    String START;
    String FINISH;
    SharedPreferences loginUser;

    int user_index;
    String car_number;
    String car_type;
    String user_phone;
    int user_grade;

    int book_index;
    int apt_index;
    String apt_phone;
    String apt_name;
    String address;
    int fee;
    String open_time;
    String close_time;
    int bookable_count;
    int temp_bookable_count;
    long bill;

    int openH, closeH;

    boolean isBookable = false;

    ReservationModel reservationModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        loginUser = getSharedPreferences("loginUser", Activity.MODE_PRIVATE);

        parkingName = (TextView) findViewById(R.id.parkingName);
        parkingAddress = (TextView) findViewById(R.id.parkingAddress);
        parkingTime = (TextView) findViewById(R.id.parkingTime);
        parkingFee = (TextView) findViewById(R.id.parkingFee);
        bookableCount = (TextView) findViewById(R.id.bookableCount);
        totalPrice = (TextView) findViewById(R.id.totalPrice);

        reservationStartTime= (TextView) findViewById(R.id.reservationStartTime);
        reservationFinishTime= (TextView) findViewById(R.id.reservationFinishTime);

        //예약리스트 DB에 넣을 사용자 정보 가져오기
        user_index = loginUser.getInt("user_index", -1);
        user_phone = loginUser.getString("user_phone", null);
        car_number = loginUser.getString("car_number", null);
        car_type = loginUser.getString("car_type", null);

        UserModel userModel = new UserModel();
        userModel.setUser_id(loginUser.getString("user_id", null));
        userModel.setUser_password(loginUser.getString("user_password", null));
        userModel.setUser_token(loginUser.getString("user_token", null));

        RetrofitSender.getEndPoint().getLoginUser(userModel).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.code() == 200){
                    UserModel result = response.body();
                    user_grade = result.getUser_grade();

                }else {
                    Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });

        //아파트명, 주소 등 받아와서 각 텍스트뷰에 띄우기
        Intent intent = getIntent();
        int modifyMode = intent.getIntExtra("modifyMode", -1);
        apt_index = intent.getIntExtra("apt_index", -1);
        apt_phone = intent.getStringExtra("apt_phone");
        apt_name = intent.getStringExtra("apt_name");
        address = intent.getStringExtra("address");
        fee = intent.getIntExtra("fee", -1);
        open_time = intent.getStringExtra("open_time");
        close_time = intent.getStringExtra("close_time");
        bookable_count = intent.getIntExtra("bookable_count", -1);
        bill = intent.getLongExtra("bill", -1);

        if(modifyMode == 1){
            apt_index = intent.getIntExtra("apt_index", -1);
            apt_name = intent.getStringExtra("apt_name");
            bill = intent.getLongExtra("bill", -1);
            book_index = intent.getIntExtra("book_index", -1);
            startYear = Integer.parseInt(intent.getStringExtra("start_time_year"));
            startMonth = Integer.parseInt(intent.getStringExtra("start_time_month")) ;
            startDate = Integer.parseInt(intent.getStringExtra("start_time_date"));
            startHour = Integer.parseInt(intent.getStringExtra("start_time_hours"));
            startMinute = Integer.parseInt(intent.getStringExtra("start_time_minute"));
            START = String.format("%04d-%02d-%02d %02d:%02d", startYear, startMonth, startDate, startHour, startMinute);
            finishYear = Integer.parseInt(intent.getStringExtra("finish_time_year"));
            finishMonth = Integer.parseInt(intent.getStringExtra("finish_time_month"));
            finishDate = Integer.parseInt(intent.getStringExtra("finish_time_date"));
            finishHour = Integer.parseInt(intent.getStringExtra("finish_time_hours"));
            finishMinute = Integer.parseInt(intent.getStringExtra("finish_time_minute"));
            ApartmentModel apartmentModel = new ApartmentModel();
            apartmentModel.setApt_index(apt_index);
            RetrofitSender.getEndPoint().getSelectedApartment(apartmentModel).enqueue(new Callback<ApartmentModel>() {
                @Override
                public void onResponse(Call<ApartmentModel> call, Response<ApartmentModel> response) {
                    if(response.code() == 200){
                        ApartmentModel apartmentModel1 = response.body();
                        apt_phone = apartmentModel1.getApt_phone();
                        address = apartmentModel1.getAddress();
                        fee = apartmentModel1.getFee();
                        open_time = apartmentModel1.getOpen_time();
                        close_time = apartmentModel1.getClose_time();
                        bookable_count = apartmentModel1.getBookable_count();

                        reservationStartTime.setText(String.format("%d년 %d월 %d일 %02d시 %02d분", startYear, startMonth, startDate, startHour, startMinute));
                        reservationFinishTime.setText(String.format("%d년 %d월 %d일 %02d시 %02d분", finishYear, finishMonth, finishDate, finishHour, finishMinute));
                        totalPrice.setText(bill + " 원");
                        parkingName.setText(apt_name);
                        parkingAddress.setText(address);
                        if((open_time.equals("00:00")) && (close_time.equals("00:00"))){
                            parkingTime.setText("제한 없음");
                        }else{
                            parkingTime.setText(open_time + "~" + close_time + "시");
                        }
                        parkingFee.setText(fee + "원/15분");

                        openH = Integer.parseInt(open_time.substring(0,2));
                        closeH = Integer.parseInt(close_time.substring(0,2));
                    }else{
                        Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ApartmentModel> call, Throwable t) {

                }
            });
        }else{
            openH = Integer.parseInt(open_time.substring(0,2));
            closeH = Integer.parseInt(close_time.substring(0,2));
            parkingName.setText(apt_name);
            parkingAddress.setText(address);
            if((open_time.equals("00:00")) && (close_time.equals("00:00"))){
                parkingTime.setText("제한 없음");
            }else{
                parkingTime.setText(open_time + "~" + close_time + "시");
            }
            parkingFee.setText(fee + "원/15분");
        }


        reservationStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance(); //현재 년월일시분


                startYear = calendar.get(Calendar.YEAR);
                startMonth = calendar.get(Calendar.MONTH);
                startDate = calendar.get(Calendar.DATE);
                startHour = calendar.get(Calendar.HOUR_OF_DAY);
                startMinute = calendar.get(Calendar.MINUTE);

                DatePickerDialog dialog = new DatePickerDialog(ReservationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        startYear = year; startMonth = month; startDate = dayOfMonth;
                        startD = String.format("%d년 %d월 %d일", year, month+1, dayOfMonth);
                        MyTimePicker myTimePicker = new MyTimePicker();
                        myTimePicker.setListener(startTimeListener);
                        myTimePicker.show(getSupportFragmentManager(), "YearMonthPickerTest");
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                dialog.getDatePicker().setMinDate(new Date().getTime());

                dialog.show();

                isStartSelected = true;
            }
        });

        reservationFinishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStartSelected){
                    DatePickerDialog dialog = new DatePickerDialog(ReservationActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            finishYear = year; finishMonth = month; finishDate = dayOfMonth;
                            finishD = String.format("%d년 %d월 %d일", year, month+1, dayOfMonth);
                            MyTimePicker myTimePicker = new MyTimePicker();

                            myTimePicker.setCalendar(startCal);
                            myTimePicker.setListener(finishTimeListener);
                            myTimePicker.show(getSupportFragmentManager(), "YearMonthPickerTest");
                        }
                    }, startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH), startCal.get(Calendar.DATE));
                    dialog.getDatePicker().setMinDate(startDate);

                    dialog.show();
                }else{
                    Toast.makeText(getApplicationContext(), "시작시간을 먼저 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmButton = (Button)findViewById(R.id.confirmButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((openH == 0) && (closeH == 0)) {  //시간제한 없음
                    //기예약된 수에 따라 isbookable 결정
                }else if(openH < closeH){            // ex) 11:00 ~ 23:00
                    if((startHour < openH) || (finishHour < openH) || (startHour > closeH) || (finishHour > closeH) || ((finishHour == closeH)&&(finishMinute != 0)) || ((startHour == closeH)&&(startMinute != 0))){
                        isBookable = false;
                    }
                }else if(openH > closeH){            // ex) 23:00 ~ 11:00
                    if(((startHour > closeH)&&(startHour < openH)) || ((finishHour > closeH)&&(finishHour < openH)) || ((finishHour == closeH)&&(finishMinute != 0)) || ((startHour == closeH)&&(startMinute != 0))){
                        isBookable = false;
                    }
                }

                if(isBookable && (user_grade == 1)){
                    reservationModel = new ReservationModel();
                    reservationModel.setUser_index(user_index);
                    reservationModel.setApt_index(apt_index);
                    reservationModel.setApt_name(apt_name);
                    reservationModel.setCar_number(car_number);
                    reservationModel.setCar_type(car_type);
                    reservationModel.setStart_time(START);
                    reservationModel.setFinish_time(FINISH);
                    reservationModel.setUser_phone(user_phone);
                    reservationModel.setBill(bill);
                    reservationModel.setIs_used(0);

                    if(modifyMode == 1){
                        reservationModel.setBook_index(book_index);
                        RetrofitSender.getEndPoint().modifyReservation(reservationModel).enqueue(new Callback<RequestResult>() {
                            @Override
                            public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                                RequestResult result = response.body();
                                if (result.isResult()){
                                    Toast.makeText(getApplicationContext(), "예약 수정 성공", Toast.LENGTH_SHORT).show();

                                    setResult(RESULT_OK);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<RequestResult> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "다시 시도하세요.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else {
                        RetrofitSender.getEndPoint().postReservation(reservationModel).enqueue(new Callback<RequestResult>() {
                            @Override
                            public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                                RequestResult result = response.body();
                                if (result.isResult()){
                                    Toast.makeText(getApplicationContext(), "예약 성공", Toast.LENGTH_SHORT).show();

                                    setResult(RESULT_OK);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<RequestResult> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "다시 시도하세요.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }else if(totalPrice.getText().toString().equals("- 원")){
                    Toast.makeText(getApplicationContext(), "시간을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }else if(user_grade != 1){
                    Toast.makeText(getApplicationContext(), "신고된 회원입니다. 예약할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }else if(!isBookable){
                    Toast.makeText(getApplicationContext(), "예약 가능한 시간이 아닙니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });


    }


    TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startHour = hourOfDay; startMinute = minute * 15;
            startCal.set(startYear, startMonth, startDate, startHour, startMinute);
            START = String.format("%04d-%02d-%02d %02d:%02d", startYear, (startMonth+1), startDate, startHour, startMinute);
            startT = String.format("%02d시 %02d분", startHour, startMinute);
            reservationStartTime.setText(startD + " " + startT);
            Log.d("YearMonthPickerTest", "hourOfDay = " + startHour + ", minute = " + startMinute);
        }
    };

    TimePickerDialog.OnTimeSetListener finishTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            finishHour = hourOfDay; finishMinute = minute * 15;
            finishCal.set(finishYear, finishMonth, finishDate, finishHour, finishMinute);
            FINISH = String.format("%04d-%02d-%02d %02d:%02d", finishYear, (finishMonth+1), finishDate, finishHour, finishMinute);
            finishT = String.format("%02d시 %02d분", finishHour, finishMinute);
            reservationFinishTime.setText(finishD + " " + finishT);
            bill = ((finishCal.getTimeInMillis() - startCal.getTimeInMillis()) / 900000 * fee);

            ReservationModel reservationModel = new ReservationModel();
            reservationModel.setApt_index(apt_index);
            reservationModel.setStart_time(START);
            reservationModel.setFinish_time(FINISH);
            if((finishCal.getTimeInMillis() - startCal.getTimeInMillis()) < 0){
                Toast.makeText(getApplicationContext(), "종료시간이 시작시간보다 빠릅니다. \n 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
                isBookable = false;
            }else {
                RetrofitSender.getEndPoint().getBookedCount(reservationModel).enqueue(new Callback<ArrayList<ReservationModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ReservationModel>> call, Response<ArrayList<ReservationModel>> response) {
                        if(response.code() == 200) {
                            ArrayList<ReservationModel> result = response.body();
                            temp_bookable_count = bookable_count;
                            temp_bookable_count -= result.size();
                            //Toast.makeText(getApplicationContext(), "bookable_count : " + bookable_count + "\nresult.size : " + result.size() + "\n::: " + temp_bookable_count + "\n ====" + result.get(0).getApt_index(), Toast.LENGTH_SHORT).show();
                            if (temp_bookable_count > 0) {
                                bookableCount.setText("예약 가능 (" + temp_bookable_count + "개)");
                                isBookable = true;
                                totalPrice.setText( bill + " 원");
                            } else {
                                bookableCount.setText("예약 불가");
                                isBookable = false;
                                totalPrice.setText( "- 원");
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<ReservationModel>> call, Throwable t) {

                    }
                });
            }

            Log.d("YearMonthPickerTest", "hourOfDay = " + hourOfDay + ", minute = " + minute * 15);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
