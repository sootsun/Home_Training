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

import com.example.home_training.MainActivity;
import com.example.home_training.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Signup extends AppCompatActivity {
    private String serverUrl = "http://43.201.96.17:4000/user/new-user";
    private OkHttpClient client = new OkHttpClient();

    TextView back;
    EditText name, id, pw, pw2, email, birthYear, birthMonth, birthDay;
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
        birthYear = findViewById(R.id.signBirth);
        birthMonth = findViewById(R.id.signBirth2);
        birthDay = findViewById(R.id.signBirth3);

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
            // 입력받은 정보값들
            String userName = name.getText().toString();
            String userId = id.getText().toString();
            String userPw = pw.getText().toString();
            String userEmail = email.getText().toString();
            String userBirthYear = birthYear.getText().toString();
            String userBirthMonth = birthMonth.getText().toString();
            String userBirthDay = birthDay.getText().toString();

            // 서버에 정보 전송
            FormBody formBody = new FormBody.Builder()
                    .add("name", userName)
                    .add("id", userId)
                    .add("password", userPw)
                    .add("email", userEmail)
                    .add("birthyear", userBirthYear)
                    .add("birthmonth", userBirthMonth)
                    .add("birthday", userBirthDay)
                    .build();

            Request request = new Request.Builder()
                    .url(serverUrl)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback(){

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();

                    try{
                        JSONObject jsonResponse = new JSONObject(responseData);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        runOnUiThread(new Runnable(){

                            @Override
                            public void run() {
                                if(success){
                                    // 회원가입 성공
                                    Toast.makeText(Signup.this, message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Signup.this, LoginActivity.class);
                                    startActivity(intent);
                                }else{
                                    // 회원가입 실패
                                    Toast.makeText(Signup.this, "정보 입력 누락: " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        Toast.makeText(Signup.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        });
    }
}
