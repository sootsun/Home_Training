package com.example.home_training.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.home_training.MainActivity;
import com.example.home_training.R;
import com.google.android.material.textfield.TextInputEditText;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText usernameEditText, passwordEditText;
    private Button loginButton;
    private OkHttpClient client = new OkHttpClient();
    private String serverUrl = "http://52.79.239.35:4000/user/login";
    TextView sign, findPassword;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //로그인 버튼들
        usernameEditText = findViewById(R.id.ediId);
        passwordEditText = findViewById(R.id.ediPassword);
        loginButton = findViewById(R.id.loginButton);

        //회원가입 버튼
        sign = findViewById(R.id.signin);

        //회원가입 버튼 클릭 시, 회원가입 페이지로 이동
        sign.setOnClickListener(v ->{
            Intent intent = new Intent(this, Signup.class);
            startActivity(intent);
        });

        // 비밀번호 찾기 버튼
        findPassword = findViewById(R.id.findPassword);

        // 비밀번호 찾기 버튼 클릭시, 비밀번호 찾기 페이지로 이동
        findPassword.setOnClickListener(v ->{
            Intent intent = new Intent(this, FindPassword.class);
            startActivity(intent);
        });

        // 로그인 버튼을 눌러 id및 password 인증
        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String id = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();


                //HTTP POST 요청을 사용하여 로그인 요청을 서버에 전송
                FormBody formBody = new FormBody.Builder()
                        .add("id", id)
                        .add("password", password)
                        .build();

                Request request = new Request.Builder()
                        .url(serverUrl)
                        .post(formBody)
                        .build();

                Toast.makeText(LoginActivity.this, "post 성공", Toast.LENGTH_SHORT).show();

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
                                        // 로그인 성공
                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("userId", id);
                                        startActivity(intent);
                                    }else{
                                        // 로그인 실패
                                        Toast.makeText(LoginActivity.this, "로그인 실패: "+ message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "로그인 실패: ", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
