package com.example.koseongmin.parksharing;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReservationListActivity extends AppCompatActivity {
    final int RESERVATIONLIST_REQUEST_CODE = 1008;
    ListView listView;
    ReservationListAdapter adapter;
    String apt_name;

    int _book_index;
    int _apt_index;
    String _start_time;
    String _finish_time;
    long _bill;
    String _apt_name;
    int _is_used;


    SharedPreferences loginUser;

    ArrayList<ReservationListItem> items = new ArrayList<ReservationListItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);


        items.clear();
        listView = (ListView)findViewById(R.id.reservation_listView);


        loginUser = getSharedPreferences("loginUser", Activity.MODE_PRIVATE);
        UserModel userModel = new UserModel();
        userModel.setUser_index(loginUser.getInt("user_index", -1));
        RetrofitSender.getEndPoint().getReservationList(userModel).enqueue(new Callback<ArrayList<ReservationModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ReservationModel>> call, Response<ArrayList<ReservationModel>> response) {
                if(response.code() == 200){
                    ArrayList<ReservationModel> reservationModels = response.body();
                    if(reservationModels.size() == 0){
                        Toast.makeText(getApplicationContext(), "예약 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    for(int i = 0; i < reservationModels.size() ; i++){
                        _book_index = reservationModels.get(i).getBook_index();
                        _apt_index = reservationModels.get(i).getApt_index();
                        _start_time = reservationModels.get(i).getStart_time();
                        _finish_time = reservationModels.get(i).getFinish_time();
                        _bill = reservationModels.get(i).getBill();
                        _is_used = reservationModels.get(i).getIs_used();
                        _apt_name = reservationModels.get(i).getApt_name();
                        items.add(new ReservationListItem(_book_index, _apt_index, _start_time, _finish_time, _bill, _apt_name, _is_used));
                    }
                    adapter = new ReservationListAdapter(getApplicationContext(), R.layout.reservationlist_item, items);
                    listView.setAdapter(adapter);
                }else {
                    Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ReservationModel>> call, Throwable t) {

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReservationListItem curItem = (ReservationListItem) adapter.getItem(position);
                if(curItem.getIs_used() == 1){  //사용된 예약 리스트임. 수정/삭제할 수 없음
                    Toast.makeText(getApplicationContext(), "수정/삭제할 수 없는 예약목록입니다.", Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservationListActivity.this);
                    builder.setTitle("선택한 예약을 수정/삭제 하시겠습니까?");
                    builder.setMessage("AlertDialog Content");
                    builder.setPositiveButton("삭제",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // 삭제구문
                                    ReservationModel reservationModel = new ReservationModel();
                                    reservationModel.setBook_index(curItem.getBook_index());
                                    RetrofitSender.getEndPoint().deleteReservation(reservationModel).enqueue(new Callback<RequestResult>() {
                                        @Override
                                        public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                                            if (response.code() == 200){
                                                RequestResult requestResult = response.body();
                                                if (requestResult.isResult() == true) {
                                                    Toast.makeText(getApplicationContext(), "예약 삭제 성공", Toast.LENGTH_SHORT).show();
                                                    items.remove(position);
                                                    listView.clearChoices();
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }else {
                                                Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<RequestResult> call, Throwable t) {

                                        }
                                    });
                                }
                            });
                    builder.setNeutralButton("수정",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ReservationListActivity.this, ReservationActivity.class);
                                    intent.putExtra("modifyMode", 1);
                                    intent.putExtra("book_index", curItem.getBook_index());
                                    intent.putExtra("apt_index", curItem.getApt_index());
                                    intent.putExtra("apt_name", curItem.getApt_name());
                                    intent.putExtra("bill", curItem.getBill());
                                    intent.putExtra("start_time_year", curItem.getStart_time().substring(0, 4));
                                    intent.putExtra("start_time_month", curItem.getStart_time().substring(5, 7));
                                    intent.putExtra("start_time_date", curItem.getStart_time().substring(8, 10));
                                    intent.putExtra("start_time_hours", curItem.getStart_time().substring(11, 13));
                                    intent.putExtra("start_time_minute", curItem.getStart_time().substring(14, 16));
                                    intent.putExtra("finish_time_year", curItem.getFinish_time().substring(0, 4));
                                    intent.putExtra("finish_time_month", curItem.getFinish_time().substring(5, 7));
                                    intent.putExtra("finish_time_date", curItem.getFinish_time().substring(8, 10));
                                    intent.putExtra("finish_time_hours", curItem.getFinish_time().substring(11, 13));
                                    intent.putExtra("finish_time_minute", curItem.getFinish_time().substring(14, 16));

                                    startActivityForResult(intent, RESERVATIONLIST_REQUEST_CODE);
                                }
                            });
                    builder.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //동작 없음
                                    listView.clearChoices();
                                }
                            });
                    builder.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case RESERVATIONLIST_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    items.clear();
                    UserModel userModel = new UserModel();
                    userModel.setUser_index(loginUser.getInt("user_index", -1));
                    RetrofitSender.getEndPoint().getReservationList(userModel).enqueue(new Callback<ArrayList<ReservationModel>>() {
                        @Override
                        public void onResponse(Call<ArrayList<ReservationModel>> call, Response<ArrayList<ReservationModel>> response) {
                            if(response.code() == 200){
                                ArrayList<ReservationModel> reservationModels = response.body();
                                for(int i = 0; i < reservationModels.size() ; i++){
                                    _book_index = reservationModels.get(i).getBook_index();
                                    _apt_index = reservationModels.get(i).getApt_index();
                                    _start_time = reservationModels.get(i).getStart_time();
                                    _finish_time = reservationModels.get(i).getFinish_time();
                                    _bill = reservationModels.get(i).getBill();
                                    _is_used = reservationModels.get(i).getIs_used();
                                    _apt_name = reservationModels.get(i).getApt_name();
                                    items.add(new ReservationListItem(_book_index, _apt_index, _start_time, _finish_time, _bill, _apt_name, _is_used));

                                    listView.clearChoices();
                                    adapter.notifyDataSetChanged();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ArrayList<ReservationModel>> call, Throwable t) {

                        }
                    });
                }else if(resultCode == RESULT_CANCELED){
                    //아무 동작 없음
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        listView.clearChoices();
    }
}
