package com.example.koseongmin.parksharing;
/**
 * 회원가입2*/

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Join2Activity extends AppCompatActivity {

    EditText carNumberInput1, carNumberInput2, carNumberInput3;
    Button preButton, confirmButton;
    Spinner carTypeInput;

    String user_id, user_password, user_name, user_phone, car_number, car_type;

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);

        //회원가입1에서 인텐트에서 id, password, name, phone 데이터를 받아서 저장
        Intent intent = getIntent();
        user_id = intent.getStringExtra("id");
        user_password = intent.getStringExtra("password");
        user_name = intent.getStringExtra("name");
        user_phone = intent.getStringExtra("phone");

        carNumberInput1 = (EditText)findViewById(R.id.carNumberInput1);
        carNumberInput2 = (EditText)findViewById(R.id.carNumberInput2);
        carNumberInput3 = (EditText)findViewById(R.id.carNumberInput3);
        carTypeInput = (Spinner)findViewById(R.id.carTypeInput);
        String[] list = getResources().getStringArray(R.array.carTypeList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,list);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        carTypeInput.setAdapter(adapter);

        //2자리의 입력을 받으면 다음 칸으로 이동
        carNumberInput1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(carNumberInput1.length()==2){  // edit1  값의 제한값을 6이라고 가정했을때
                    carNumberInput2.requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        //이전 버튼 클릭 시 회원가입1로 돌아감
        preButton = (Button)findViewById(R.id.preButton);
        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        userModel = new UserModel();
        //확인 버튼 클릭 시 입력받은 데이터들 서버DB로 전송
        confirmButton = (Button)findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car_number = carNumberInput1.getText().toString() + carNumberInput2.getText().toString() + carNumberInput3.getText().toString();
                car_type = carTypeInput.getSelectedItem().toString();
                if(!Pattern.matches("^\\d{2}[가|나|다|라|마|거|너|더|러|머|버|서|어|저|고|노|도|로|모|보|소|오|조|구|누|두|루|무|부|수|우|주|바|사|아|자|허|배|호|하\\x20]\\d{4}/*$", car_number)) {    //차량번호 데이터 양식 검사
                    Toast.makeText(getApplicationContext(), "차량번호를 형식에 맞게 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {

                    userModel.setUser_id(user_id);
                    userModel.setUser_password(user_password);
                    userModel.setUser_name(user_name);
                    userModel.setUser_phone(user_phone);
                    userModel.setCar_number(car_number);
                    userModel.setCar_type(car_type);

                    RetrofitSender.getEndPoint().join(userModel).enqueue(new Callback<RequestResult>() {    //서버 DB에 데이터 전송
                        @Override
                        public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                            if (response.code() == 200){    //전송 성공 시
                                RequestResult result = response.body();
                                Toast.makeText(getApplicationContext(),   "회원가입 성공", Toast.LENGTH_SHORT).show();

                                setResult(RESULT_OK);
                                finish();
                            }else {     //서버 접속 실패 시
                                Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<RequestResult> call, Throwable t) {
                            //전송 실패 시
                        }
                    });
                }
            }
        });

    }

}
