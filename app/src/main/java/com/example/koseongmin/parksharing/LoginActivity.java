package com.example.koseongmin.parksharing;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText idInput, passwordInput;
    Button loginButton, cancelbutton;

    int user_index;
    String user_id;
    String user_password;
    String user_name;
    String user_phone;
    String car_number;
    String car_type;
    int user_grade;
    String user_token;

    SharedPreferences loginUser;
    SharedPreferences.Editor loginUserEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUser = getSharedPreferences("loginUser", Activity.MODE_PRIVATE);
        loginUserEditor = loginUser.edit();
        user_token = loginUser.getString("user_token", null);

        idInput = (EditText)findViewById(R.id.idInput);
        passwordInput = (EditText)findViewById(R.id.passwordInput);
        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아이디 비밀번호 DB에서 가져오고 검사하는 부분
                String id = idInput.getText().toString();
                String password = passwordInput.getText().toString();

                // DB에서 아이디 비밀번호 검사
                UserModel userModel = new UserModel();
                userModel.setUser_id(id);
                userModel.setUser_password(password);
                userModel.setUser_token(user_token);
                RetrofitSender.getEndPoint().getLoginUser(userModel).enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if(response.code() == 200){
                            UserModel result = response.body();
                            if(result != null) {
                                user_index = result.getUser_index();
                                user_id = result.getUser_id();
                                user_password = result.getUser_password();
                                user_name = result.getUser_name();
                                user_phone = result.getUser_phone();
                                car_number = result.getCar_number();
                                car_type = result.getCar_type();
                                user_grade = result.getUser_grade();

                                loginUserEditor.putBoolean("autoLogin", true);
                                loginUserEditor.putInt("user_index", user_index);
                                loginUserEditor.putString("user_id", user_id);
                                loginUserEditor.putString("user_password", user_password);
                                loginUserEditor.putString("user_name", user_name);
                                loginUserEditor.putString("user_phone", user_phone);
                                loginUserEditor.putString("car_number", car_number);
                                loginUserEditor.putString("car_type", car_type);
                                loginUserEditor.putInt("user_grade", user_grade);
                                // DB 에서 가져온 사용자 정보를 loginUser에 저장
                                loginUserEditor.commit();// commit()으로 저장
                                Toast.makeText(getApplicationContext(), user_name + "님 로그인", Toast.LENGTH_SHORT).show();

                                setResult(RESULT_OK);
                                finish();

                            }else{
                                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "등록되지 않은 회원입니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        cancelbutton = (Button)findViewById(R.id.cancelButton);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
