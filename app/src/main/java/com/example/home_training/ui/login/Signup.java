package com.example.home_training.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.home_training.R;

public class Signup extends AppCompatActivity {
    TextView back;
    EditText name, id, pw, pw2, email, birthyear, birthdate, birthday;
    Button pwcheck, submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //뒤로가기 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

        //기입 항목
        name = findViewById(R.id.signName);
        id = findViewById(R.id.signId);
        pw = findViewById(R.id.signPassword);
        pw2 = findViewById(R.id.signPW2);
        email = findViewById(R.id.signMail);
        birthyear = findViewById(R.id.signBirth);
        birthdate = findViewById(R.id.signBirth2);
        birthday = findViewById(R.id.signBirth3);

        //비밀번호 확인 버튼
        pwcheck = findViewById(R.id.pwCheckButton);
        pwcheck.setOnClickListener(v -> {
            if(pw.getText().toString().equals(pw2.getText().toString())){
                pwcheck.setText("일치");
            }else{
                pwcheck.setText("불일치");
                Toast.makeText(Signup.this, "비밀번호가 다릅니다", Toast.LENGTH_LONG).show();
            }
        });

        //회원가입 완료 버튼
        submit = findViewById(R.id.signupbutton);
        submit.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
