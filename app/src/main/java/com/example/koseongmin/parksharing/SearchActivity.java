package com.example.koseongmin.parksharing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    final int SEARCH_REQUEST_CODE = 1006;
    ListView listView;
    SearchAdapter adapter;
    EditText searchInput;
    Button searchButton;

    ArrayList<SearchListItem> items = new ArrayList<SearchListItem>();

    SharedPreferences loginUser;
    SharedPreferences.Editor loginUserEditor;

    int apt_index;
    String apt_phone, apt_name, address, open_time, close_time;
    int fee, bookable_count, isbookable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = (EditText)findViewById(R.id.searchInput);
        listView = (ListView)findViewById(R.id.search_listView);

        loginUser = getSharedPreferences("loginUser", Activity.MODE_PRIVATE);

        searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchInput.getText().toString().length() < 2){
                    Toast.makeText(getApplicationContext(), "두 글자 이상 입력하시오.", Toast.LENGTH_SHORT).show();
                }else {
                    items.clear();
                    ApartmentModel apartmentModel = new ApartmentModel();
                    apartmentModel.setApt_name(searchInput.getText().toString());
                    RetrofitSender.getEndPoint().getSearchedApartment(apartmentModel).enqueue(new Callback<ArrayList<ApartmentModel>>() {
                        @Override
                        public void onResponse(Call<ArrayList<ApartmentModel>> call, Response<ArrayList<ApartmentModel>> response) {
                            if(response.code() == 200){
                                ArrayList<ApartmentModel> apartmentModels = response.body();
                                if(apartmentModels.size() < 1){
                                    Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                                }else{
                                    for(int i = 0; i < apartmentModels.size(); i++){
                                        apt_index = apartmentModels.get(i).getApt_index();
                                        apt_phone = apartmentModels.get(i).getApt_phone();
                                        apt_name = apartmentModels.get(i).getApt_name();
                                        address = apartmentModels.get(i).getAddress();
                                        fee = apartmentModels.get(i).getFee();
                                        open_time = apartmentModels.get(i).getOpen_time();
                                        close_time = apartmentModels.get(i).getClose_time();
                                        bookable_count = apartmentModels.get(i).getBookable_count();
                                        isbookable = apartmentModels.get(i).getIsbookable();

                                        items.add(new SearchListItem(apt_index, apt_phone, apt_name, address, open_time, close_time, fee, bookable_count, isbookable));
                                    }
                                    adapter = new SearchAdapter(getApplicationContext(), R.layout.searchlist_item, items);
                                    listView.setAdapter(adapter);
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ArrayList<ApartmentModel>> call, Throwable t) {

                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SearchListItem curItem = (SearchListItem) adapter.getItem(position);    //curItem의 각 정보를 intent로 전달
                            if (curItem.getIsbookable() == 0){  //예약 불가인 아파트
                                Toast.makeText(getApplicationContext(), "예약 할 수 없는 주차장입니다.", Toast.LENGTH_SHORT).show();
                            }else {
                                Intent intent = new Intent(SearchActivity.this, ReservationActivity.class);
                                intent.putExtra("modifyMode", -1);  //수정 모드:false
                                intent.putExtra("apt_index", curItem.getApt_index());
                                intent.putExtra("apt_phone", curItem.getApt_phone());
                                intent.putExtra("apt_name", curItem.getApt_name());
                                intent.putExtra("address", curItem.getAddress());
                                intent.putExtra("fee", curItem.getFee());
                                intent.putExtra("open_time", curItem.getOpen_time());
                                intent.putExtra("close_time", curItem.getClose_time());
                                intent.putExtra("bookable_count", curItem.getBookable_count());
                                startActivityForResult(intent, SEARCH_REQUEST_CODE);
                            }
                        }
                    });

                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case SEARCH_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    //예약 완료 : 예약페이지에서 예약페이지와 검색액티비티를 종료하고 바로 돌아옴
                    setResult(RESULT_OK);
                    finish();
                }else if(resultCode == RESULT_CANCELED){
                    //예약 미완료 : 검색액티비티로 돌아오고, 뒤로가기 해야 지도로 돌아감
                    listView.clearChoices();
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        items.clear();
        listView.clearChoices();
    }

}
