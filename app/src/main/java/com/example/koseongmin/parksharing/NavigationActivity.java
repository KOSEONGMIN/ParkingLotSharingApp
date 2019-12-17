package com.example.koseongmin.parksharing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.Projection;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    final int SEARCH_REQUEST_CODE = 1004;
    final int RESERVATION_REQUEST_CODE = 1005;
    final int MYPAGE_REQUEST_CODE = 1007;
    final int MAIN_REQUEST_CODE = 1010;

    TextView nav_name, nav_id;

    private FusedLocationSource locationSource;
    private boolean autoLogin;
    private boolean is_alert;
    private String user_id;  // DB에서 받아온 사용자의 UID
    private String user_name;
    private String user_password;
    private int user_grade;
    private String user_token;
    private double myLatitude = 37.450811;
    private double myLongitude = 127.127130;

    ArrayList<ParkingLot> parkingLots;
    ArrayList<Marker> markers;
    MapFragment mapFragment;

    //marker 리스트에 추가할 때 사용
    int apt_index = -1;
    String apt_phone = null;
    String apt_name = null;
    String address = null;
    double latitude = -1;
    double longitude = -1;
    int fee = -1;
    String open_time = null;
    String close_time = null;
    int bookable_count = -1;
    int isbookable = -1;

    SharedPreferences loginUser;
    SharedPreferences.Editor loginUserEditor;

    String TAG = "NavigationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);


        parkingLots = new ArrayList<ParkingLot>();
        markers = new ArrayList<Marker>();
        loginUser = getSharedPreferences("loginUser", Activity.MODE_PRIVATE);
        loginUserEditor = loginUser.edit();
        autoLogin = loginUser.getBoolean("autoLogin", false);
        is_alert = loginUser.getBoolean("is_alert", false);

        if(is_alert){
            int push_alert_using = loginUser.getInt("push_alert_using", 0);
            int push_alert_total = loginUser.getInt("push_alert_total", 0);

            // 다이얼로그 바디

            AlertDialog.Builder alertdialog = new AlertDialog.Builder(NavigationActivity.this);

            // 다이얼로그 메세지

            alertdialog.setMessage("예약시간 15분 전입니다.\n총 주차장 수 : " + push_alert_total + "\n사용중인 차량 수 : " + push_alert_using);



            // 확인버튼

            alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener(){

                @Override

                public void onClick(DialogInterface dialog, int which) {
                    is_alert = false;
                    loginUserEditor.putBoolean("is_alert", false);
                    loginUserEditor.remove("push_alert_using");
                    loginUserEditor.remove("push_alert_total");
                    // DB 에서 가져온 사용자 정보를 loginUser에 저장
                    loginUserEditor.commit();// commit()으로 저장
                }

            });


            // 메인 다이얼로그 생성
            AlertDialog alert = alertdialog.create();
            // 아이콘 설정
            alert.setIcon(R.drawable.logo);
            // 타이틀
            alert.setTitle("[예약 알림]");
            // 다이얼로그 보기
            alert.show();



        }
        is_alert = false;
        loginUserEditor.putBoolean("is_alert", false);
        loginUserEditor.remove("push_alert_using");
        loginUserEditor.remove("push_alert_total");
        // DB 에서 가져온 사용자 정보를 loginUser에 저장
        loginUserEditor.commit();// commit()으로 저장

        Intent loadingIntent = new Intent(NavigationActivity.this, LoadingActivity.class);
        startActivity(loadingIntent);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() { //토큰 가져와서 저장
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(!task.isSuccessful()){
                    return;
                }
                // Get new Instance ID token
                String token = task.getResult().getToken();

                Log.d(TAG, "token: " + token);

                loginUserEditor.putString("user_token", token);
                loginUserEditor.commit();

                // Log and toast
                //String msg = getString(R.string.msg_token_fmt, token);
                //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();

            }
        });


        //지도 출력
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        nav_name = (TextView) headerView.findViewById(R.id.nav_name);
        nav_id = (TextView) headerView.findViewById(R.id.nav_id);

        if (!autoLogin) { //로그인 안되어있으면 MainActivity로 보냄
            Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
            startActivityForResult(intent, MAIN_REQUEST_CODE);
        }else {
            user_id = loginUser.getString("user_id", null);
            user_name = loginUser.getString("user_name", null);
            user_password = loginUser.getString("user_password", null);
            user_grade = loginUser.getInt("user_grade", -1);
            user_token = loginUser.getString("user_token", null);
            UserModel userModel = new UserModel();
            userModel.setUser_id(user_id);
            userModel.setUser_password(user_password);
            userModel.setUser_token(user_token);

            RetrofitSender.getEndPoint().getLoginUser(userModel).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.code() == 200){
                        UserModel result = response.body();
                        loginUserEditor.putInt("user_index", result.getUser_index());
                        loginUserEditor.putString("user_password", result.getUser_password());
                        loginUserEditor.putString("user_phone", result.getUser_phone());
                        loginUserEditor.putString("car_number", result.getCar_number());
                        loginUserEditor.putString("car_type", result.getCar_type());
                        loginUserEditor.putInt("user_grade", result.getUser_grade());
                        loginUserEditor.commit();
                        user_name = result.getUser_name();
                        nav_name.setText(user_name);
                        nav_id.setText(user_id);
                    }else {
                        Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                }
            });
        }

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, SearchActivity.class);
                startActivityForResult(intent, SEARCH_REQUEST_CODE);
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
                    Toast.makeText(getApplicationContext(), "예약 완료!", Toast.LENGTH_SHORT).show();
                }else if(resultCode == RESULT_CANCELED){
                    //예약 미완료 : 검색액티비티로 돌아오고, 뒤로가기 해야 돌아옴, 아무 동작 없음
                }
                break;
            case RESERVATION_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Toast.makeText(getApplicationContext(), "예약 완료!", Toast.LENGTH_SHORT).show();

                }else if(resultCode == RESULT_CANCELED){
                    //아무 동작 없음
                }
                break;
            case MYPAGE_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Toast.makeText(getApplicationContext(), "회원 수정 완료", Toast.LENGTH_SHORT).show();
                }else if(resultCode == RESULT_CANCELED){
                    //아무 동작 없음
                }else if(resultCode == RESULT_FIRST_USER){
                    //로그아웃, 메인화면 열기
                    UserModel userModel = new UserModel();
                    userModel.setUser_index(loginUser.getInt("user_index", -1));
                    RetrofitSender.getEndPoint().logoutUser(userModel).enqueue(new Callback<RequestResult>() {
                        @Override
                        public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                            if(response.code() == 200){
                                RequestResult requestResult = response.body();
                                if(requestResult.isResult()) {
                                    // 저장된 사용자정보 지움
                                    Toast.makeText(getApplicationContext(), "회원 탈퇴 완료", Toast.LENGTH_SHORT).show();
                                    loginUserEditor.putBoolean("autoLogin", false);
                                    loginUserEditor.remove("user_index");
                                    loginUserEditor.remove("user_id");
                                    loginUserEditor.remove("user_password");
                                    loginUserEditor.remove("user_name");
                                    loginUserEditor.remove("user_phone");
                                    loginUserEditor.remove("car_number");
                                    loginUserEditor.remove("car_type");
                                    loginUserEditor.remove("user_grade");
                                    loginUserEditor.commit();// commit()으로 저장

                                    Intent intent = new Intent(NavigationActivity.this, NavigationActivity.class);
                                    startActivity(intent);
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
                break;
            case MAIN_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    user_id = loginUser.getString("user_id", null);
                    user_name = loginUser.getString("user_name", null);
                    nav_name.setText(user_name);
                    nav_id.setText(user_id);
                }else if(resultCode == RESULT_CANCELED){
                    //아무 동작 없음
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mypage) {
            // 마이페이지
            Intent intent = new Intent(NavigationActivity.this, MypageActivity.class);
            startActivityForResult(intent, MYPAGE_REQUEST_CODE);
        } else if (id == R.id.nav_myreservation) {
            // 내 예약 보기
            Intent intent = new Intent(NavigationActivity.this, ReservationListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            // 로그아웃
            // user_table에 token null로 만들기
            UserModel userModel = new UserModel();
            userModel.setUser_index(loginUser.getInt("user_index", -1));
            RetrofitSender.getEndPoint().logoutUser(userModel).enqueue(new Callback<RequestResult>() {
                @Override
                public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                    if(response.code() == 200){
                        RequestResult requestResult = response.body();
                        if(requestResult.isResult()) {
                            // 저장된 사용자정보 지움
                            loginUserEditor.putBoolean("autoLogin", false);
                            loginUserEditor.remove("user_index");
                            loginUserEditor.remove("user_id");
                            loginUserEditor.remove("user_password");
                            loginUserEditor.remove("user_name");
                            loginUserEditor.remove("user_phone");
                            loginUserEditor.remove("car_number");
                            loginUserEditor.remove("car_type");
                            loginUserEditor.remove("user_grade");
                            loginUserEditor.commit();// commit()으로 저장

                            Toast.makeText(getApplicationContext(), "로그아웃 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NavigationActivity.this, NavigationActivity.class);
                            startActivity(intent);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        //Retrofit 사용해서 DB 정보 가져옴

        RetrofitSender.getEndPoint().getAllApartment().enqueue(new Callback<ArrayList<ApartmentModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ApartmentModel>> call, Response<ArrayList<ApartmentModel>> response) {
                if(response.code() == 200){
                    ArrayList<ApartmentModel> result = response.body();
                    parkingLots.clear();
                    for(int i = 0; i < markers.size(); i++){
                        markers.get(i).setMap(null);
                    }
                    markers.clear();
                    for (int i = 0; i < result.size(); i++) {
                        parkingLots.add(new ParkingLot(result.get(i).getApt_index(), result.get(i).getApt_phone(), result.get(i).getApt_name(),
                                result.get(i).getAddress(), result.get(i).getLatitude(), result.get(i).getLongitude(), result.get(i).getFee(),
                                result.get(i).getOpen_time(), result.get(i).getClose_time(), result.get(i).getBookable_count(),
                                result.get(i).getIsbookable()));
                    }

                    mapFragment.getMapAsync(NavigationActivity.this);
                }else {
                    Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<ApartmentModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        CameraUpdate cameraUpdate = CameraUpdate
                .toCameraPosition(new CameraPosition(new LatLng(myLatitude, myLongitude), 15));
        naverMap.moveCamera(cameraUpdate);

        for(int i = 0; i < parkingLots.size(); i++){
            int _apt_index = parkingLots.get(i).getApt_index();
            String _apt_phone = parkingLots.get(i).getApt_phone();
            String _apt_name = parkingLots.get(i).getApt_name();
            String _address = parkingLots.get(i).getAddress();
            double _latitude = parkingLots.get(i).getLatitude();
            double _longitude = parkingLots.get(i).getLongitude();
            int _fee = parkingLots.get(i).getFee();
            String _open_time = parkingLots.get(i).getOpen_time();
            String _close_time = parkingLots.get(i).getClose_time();
            int _bookable_count = parkingLots.get(i).getBookable_count();
            int _isbookable = parkingLots.get(i).getIsbookable();
            markers.add(new Marker());
            markers.get(i).setPosition(new LatLng(_latitude, _longitude));
            markers.get(i).setCaptionText(_apt_name);
            if(_isbookable != 0) {
                markers.get(i).setSubCaptionText("(예약 가능)");
                markers.get(i).setIcon(MarkerIcons.BLUE);
                markers.get(i).setOnClickListener(new Overlay.OnClickListener() {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay) {
                        Intent intent = new Intent(NavigationActivity.this, ReservationActivity.class);
                        intent.putExtra("modifyMode", -1);  //수정 모드:false
                        intent.putExtra("apt_index", _apt_index);
                        intent.putExtra("apt_phone", _apt_phone);
                        intent.putExtra("apt_name", _apt_name);
                        intent.putExtra("address", _address);
                        intent.putExtra("fee", _fee);
                        intent.putExtra("open_time", _open_time);
                        intent.putExtra("close_time", _close_time);
                        intent.putExtra("bookable_count", _bookable_count);
                        startActivityForResult(intent, RESERVATION_REQUEST_CODE);
                        return false;
                    }
                });
            } else {
                markers.get(i).setSubCaptionText("(예약 불가)");
                markers.get(i).setIcon(MarkerIcons.RED);
                markers.get(i).setOnClickListener(new Overlay.OnClickListener() {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay) {
                        Toast.makeText(getApplicationContext(), "예약 불가능한 주차장입니다.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }
            markers.get(i).setMap(naverMap);
        }

        naverMap.addOnCameraIdleListener(new NaverMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //화면 이동 직후 메소드

            }
        });

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
    }
}
