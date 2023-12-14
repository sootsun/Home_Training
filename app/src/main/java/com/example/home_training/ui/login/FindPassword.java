package com.example.home_training.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.home_training.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FindPassword extends AppCompatActivity {

    private String serverUrl = "http://52.79.239.35:4000/user";
    private OkHttpClient client = new OkHttpClient();

    EditText checkid, checkname, newPW, newPW2;
    Button check, comparePW, changePW;
    TextView back;

    LinearLayout verifyLayout, newPwLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);

        //뒤로가기 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());


        // 비밀번호 찾기 기입 항목들
        checkid = findViewById(R.id.checkId);
        checkname = findViewById(R.id.checkName);

        // layout
        verifyLayout = (LinearLayout) findViewById(R.id.verifyLayout);
        newPwLayout = (LinearLayout)findViewById(R.id.newPWLayout);

        // 새 비밀번호 입력 및 일치확인
        newPW = findViewById(R.id.newPassword);
        newPW2 = findViewById(R.id.newPassword2);

        // 비밀번호 확인 버튼
        check = findViewById(R.id.givePassword);
        check.setOnClickListener(v -> {
            String id = checkid.getText().toString();
            String name = checkname.getText().toString();

            FormBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("name", name)
                    .build();
            Request request = new Request.Builder()
                    .url(serverUrl+ "/passwordVerify")
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();

                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        boolean success = jsonResponse.getBoolean("success");

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (success) {
                                    // 비밀번호 찾기 성공, 비밀번호 재설정 시작
                                    verifyLayout.setVisibility(View.INVISIBLE);
                                    newPwLayout.setVisibility(View.VISIBLE);

                                    comparePW = findViewById(R.id.pwCheckButton);
                                    comparePW.setOnClickListener(v -> {
                                        if(newPW.getText().toString().equals(newPW2.getText().toString())){
                                            comparePW.setText("일치");
                                        }else{
                                            comparePW.setText("불일치");
                                        }
                                    });

                                    changePW = findViewById(R.id.changePW);
                                    changePW.setOnClickListener(v->{
                                        // 새 비밀번호
                                        String password = newPW.getText().toString();

                                        FormBody formBody1 = new FormBody.Builder()
                                                .add("password", password)
                                                .build();

                                        Request request = new Request.Builder()
                                                .url(serverUrl+ "/findpassword")
                                                .post(formBody1)
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
                                                                // 비밀번호 재설정 성공
                                                                Toast.makeText(FindPassword.this, message, Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(FindPassword.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }else{
                                                                // 비밀번호 재설정 실패
                                                                Toast.makeText(FindPassword.this, message, Toast.LENGTH_SHORT).show();
                                                            }
                                                            return;
                                                        }
                                                    });
                                                } catch (JSONException e) {
                                                    Toast.makeText(FindPassword.this, "재설정 실패", Toast.LENGTH_SHORT).show();
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    });
                                } else {
                                    // 실패
                                    Toast.makeText(FindPassword.this, "해당 이름, 혹은 아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }
                        });
                    } catch (JSONException e) {
                        Toast.makeText(FindPassword.this, "비밀번호 찾기 실패", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

        });
    }
}
