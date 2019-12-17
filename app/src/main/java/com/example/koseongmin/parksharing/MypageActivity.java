package com.example.koseongmin.parksharing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MypageActivity extends AppCompatActivity {
    TextView myNameData, myIdData;
    EditText myPrePasswordInput, myPasswordInput, myPasswordConfirmInput, myPhoneInput;
    EditText carNumberInput1, carNumberInput2, carNumberInput3;
    Spinner myCarTypeSelect;
    Button confirmButton, cancelButton, withdrawalButton;

    String user_id, user_password, user_name, user_phone, car_number, car_type;

    SharedPreferences loginUser;
    SharedPreferences.Editor loginUserEditor;

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        loginUser = getSharedPreferences("loginUser", Activity.MODE_PRIVATE);
        loginUserEditor = loginUser.edit();
        user_id = loginUser.getString("user_id", null);
        user_password = loginUser.getString("user_password", null);
        user_name = loginUser.getString("user_name", null);
        user_phone = loginUser.getString("user_phone", null);
        car_number = loginUser.getString("car_number", null);
        car_type = loginUser.getString("car_type", null);

        myNameData = (TextView) findViewById(R.id.myNameData);
        myIdData = (TextView) findViewById(R.id.myIdData);
        myPrePasswordInput = (EditText) findViewById(R.id.myPrePasswordInput);
        myPasswordInput = (EditText) findViewById(R.id.myPasswordInput);
        myPasswordConfirmInput = (EditText) findViewById(R.id.myPasswordConfirmInput);
        myPhoneInput = (EditText) findViewById(R.id.myPhoneInput);
        myPhoneInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        carNumberInput1 = (EditText)findViewById(R.id.carNumberInput1);
        carNumberInput2 = (EditText)findViewById(R.id.carNumberInput2);
        carNumberInput3 = (EditText)findViewById(R.id.carNumberInput3);
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

        myCarTypeSelect = (Spinner) findViewById(R.id.myCarTypeSelect);
        String[] list = getResources().getStringArray(R.array.carTypeList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,list);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        myCarTypeSelect.setAdapter(adapter);

        myNameData.setText(user_name);
        myIdData.setText(user_id);
        myPhoneInput.setText(user_phone);
        carNumberInput1.setText(car_number.substring(0, 2));
        carNumberInput2.setText(car_number.substring(2, 3));
        carNumberInput3.setText(car_number.substring(3, 7));
        if(car_type.equals("소형")){
            myCarTypeSelect.setSelection(0);
        }else if(car_type.equals("중형")){
            myCarTypeSelect.setSelection(1);
        }else if(car_type.equals("대형")){
            myCarTypeSelect.setSelection(2);
        }else if(car_type.equals("승합")){
            myCarTypeSelect.setSelection(3);
        }else if(car_type.equals("화물")){
            myCarTypeSelect.setSelection(4);
        }


        confirmButton = (Button) findViewById(R.id.confirmButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        withdrawalButton = (Button) findViewById(R.id.withdrawalButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car_number = carNumberInput1.getText().toString() + carNumberInput2.getText().toString() + carNumberInput3.getText().toString();
                if(myPrePasswordInput.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "현재 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$", myPasswordInput.getText().toString())){
                    Toast.makeText(getApplicationContext(), "8~15자의 영문,숫자,특수기호 조합만 가능합니다.", Toast.LENGTH_LONG).show();
                }else if(!myPrePasswordInput.getText().toString().equals(user_password)){
                    Toast.makeText(getApplicationContext(), "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }else if(!myPasswordInput.getText().toString().equals(myPasswordConfirmInput.getText().toString())){
                    Toast.makeText(getApplicationContext(), "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }else if(!Pattern.matches("^010-\\d{4}-\\d{4}$", myPhoneInput.getText())){
                    Toast.makeText(getApplicationContext(), "휴대폰 번호를 정확히 입력하시오", Toast.LENGTH_SHORT).show();
                }else if(!Pattern.matches("^\\d{2}[가|나|다|라|마|거|너|더|러|머|버|서|어|저|고|노|도|로|모|보|소|오|조|구|누|두|루|무|부|수|우|주|바|사|아|자|허|배|호|하\\x20]\\d{4}/*$", car_number)) {    //차량번호 데이터 양식 검사
                    Toast.makeText(getApplicationContext(), "차량번호를 형식에 맞게 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    userModel = new UserModel();
                    user_password = myPasswordInput.getText().toString();
                    user_phone = myPhoneInput.getText().toString();
                    car_type = myCarTypeSelect.getSelectedItem().toString();

                    userModel = new UserModel();
                    userModel.setUser_password(user_password);
                    userModel.setUser_phone(user_phone);
                    userModel.setCar_number(car_number);
                    userModel.setCar_type(car_type);
                    userModel.setUser_index(loginUser.getInt("user_index", -1));
                    RetrofitSender.getEndPoint().modifyUser(userModel).enqueue(new Callback<RequestResult>() {
                        @Override
                        public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                            RequestResult result = response.body();
                            if(response.code() == 200) {
                                loginUserEditor.putString("user_password", user_password);
                                loginUserEditor.putString("user_phone", user_phone);
                                loginUserEditor.putString("car_number", car_number);
                                loginUserEditor.putString("car_type", car_type);
                                loginUserEditor.commit();// commit()으로 저장
                                setResult(RESULT_OK);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), "서버 접속 실패", Toast.LENGTH_SHORT).show();
                                user_password = loginUser.getString("user_password", null);
                                user_name = loginUser.getString("user_name", null);
                                user_phone = loginUser.getString("user_phone", null);
                                car_number = loginUser.getString("car_number", null);
                                car_type = loginUser.getString("car_type", null);
                            }
                        }
                        @Override
                        public void onFailure(Call<RequestResult> call, Throwable t) {
                            user_password = loginUser.getString("user_password", null);
                            user_name = loginUser.getString("user_name", null);
                            user_phone = loginUser.getString("user_phone", null);
                            car_number = loginUser.getString("car_number", null);
                            car_type = loginUser.getString("car_type", null);
                        }
                    });

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
        withdrawalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 정말 탈퇴할 것인지 묻는 창 띄우고 DB에서 데이터 삭제
                String userPassword = loginUser.getString("user_password", null);
                UserModel userModel = new UserModel();
                userModel.setUser_index(loginUser.getInt("user_index", -1));
                if(myPrePasswordInput.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "현재 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(!myPrePasswordInput.getText().toString().equals(userPassword)){
                    Toast.makeText(getApplicationContext(), "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    // 로그아웃 및 메인화면으로 돌아가는 것은 NavigationActivity.java에 구현함
                    RetrofitSender.getEndPoint().disableUser(userModel).enqueue(new Callback<RequestResult>() {
                        @Override
                        public void onResponse(Call<RequestResult> call, Response<RequestResult> response) {
                            if(response.code() == 200){
                                RequestResult requestResult = response.body();
                                if(requestResult.isResult()) {

                                    setResult(RESULT_FIRST_USER);
                                    finish();
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
            }
        });
    }
}
