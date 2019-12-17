package com.example.koseongmin.parksharing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final int LOGIN_REQUEST_CODE = 1001;
    final int JOIN_REQUEST_CODE = 1002;

    private long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_REQUEST_CODE);
            }
        });

        Button joinButton = (Button)findViewById(R.id.joinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinActivity.class);
                startActivityForResult(intent, JOIN_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case LOGIN_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    //로그인 성공시 코드
                    setResult(RESULT_OK);
                    finish();
                }else if(resultCode == RESULT_CANCELED){
                    //로그인 취소시 코드
                }
                break;
            case JOIN_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    //회원가입 완료시 코드
                }else if(resultCode == RESULT_CANCELED){
                    //회원가입 취소시 코드
                }
                break;
        }
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            finishAffinity();
        }

    }
}
