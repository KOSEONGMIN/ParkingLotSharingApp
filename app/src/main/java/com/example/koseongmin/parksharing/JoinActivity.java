package com.example.koseongmin.parksharing;
/**
 *회원가입1
 */



import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {
    EditText idInput, passwordInput, passwordConfirmInput, nameInput, phoneInput;
    Button preButton, nextButton;
    final int JOIN2_REQUEST_CODE = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        idInput = (EditText)findViewById(R.id.idInput);
        passwordInput = (EditText)findViewById(R.id.passwordInput);
        passwordConfirmInput = (EditText)findViewById(R.id.passwordConfirmInput);
        nameInput = (EditText)findViewById(R.id.nameInput);
        phoneInput = (EditText)findViewById(R.id.phoneInput);
        phoneInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        preButton = (Button)findViewById(R.id.preButton);
        preButton.setOnClickListener(new View.OnClickListener() {   //이전 버튼 클릭 시 메인화면으로 돌아감
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        nextButton = (Button)findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {  //다음 버튼 클릭 시 회원가입2로 넘어감
            @Override
            public void onClick(View v) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(idInput.getText()).matches()){  //이메일 형식 검사
                    Toast.makeText(getApplicationContext(), "ID가 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                }else if(!Pattern.matches("[가-힣]{2,4}$",nameInput.getText().toString())){   //이름 형식 검사
                    Toast.makeText(getApplicationContext(), "이름을 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                }else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$", passwordInput.getText().toString())){ //비밀번호 형식검사
                    Toast.makeText(getApplicationContext(), "8~15자의 영문,숫자,특수기호 조합만 가능합니다.", Toast.LENGTH_LONG).show();
                }else if(!passwordInput.getText().toString().equals(passwordConfirmInput.getText().toString())){    //비밀번호 일치 검사
                    Toast.makeText(getApplicationContext(), "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }else if(!Pattern.matches("^010-\\d{4}-\\d{4}$", phoneInput.getText().toString())){ //휴대폰번호 형식 검사
                    Toast.makeText(getApplicationContext(), "휴대폰 번호를 정확히 입력하시오", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(JoinActivity.this, Join2Activity.class); //회원가입2로 인텐트 전달
                    intent.putExtra("id", idInput.getText().toString());
                    intent.putExtra("password", passwordInput.getText().toString());
                    intent.putExtra("name", nameInput.getText().toString());
                    intent.putExtra("phone", phoneInput.getText().toString());
                    startActivityForResult(intent, JOIN2_REQUEST_CODE);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case JOIN2_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    //회원가입2 모두 완료시 코드
                    //메인화면으로 바로 이동
                    finish();
                }else if(resultCode == RESULT_CANCELED){
                    //회원가입2 이전 시 코드
                }
                break;
        }
    }
}
