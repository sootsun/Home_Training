package com.example.home_training.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.home_training.MainActivity;
import com.example.home_training.R;
import com.example.home_training.ui.login.LoginActivity;
import com.google.gson.JsonIOException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DashboardWrite extends AppCompatActivity {

    private EditText title, content;
    private Button publishButton;
    private String MyId;
    private TextView back;
    private OkHttpClient client = new OkHttpClient();

    private int postId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard_write);

        title = findViewById(R.id.editTitle);
        content = findViewById(R.id.editContent);
        publishButton = findViewById(R.id.writeTextButton);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToServer();
            }
        });
    }

    private void sendDataToServer(){
        String titleText = title.getText().toString();
        String contentText = content.getText().toString();

        String MyId = getIntent().getStringExtra("userId");


        String serverUrl = "http://52.79.239.35:4000/community/posts";

        FormBody formBody = new FormBody.Builder()
                .add("id", MyId)
                .add("title", titleText)
                .add("content", contentText)
                .build();

        Request request = new Request.Builder()
                .url(serverUrl)
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
                    int postid = jsonResponse.getInt("postid");

                    runOnUiThread(new Runnable() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void run() {
                            if(success){
                                Log.i(TAG, String.valueOf(postid));

                                DashboardFragment dashboardFragment = (DashboardFragment) getSupportFragmentManager().findFragmentByTag("dashboardFragmentTag");
                                if (dashboardFragment != null) {
                                    dashboardFragment.updateData();
                                }
                                navigateToDashboardFragment();
                            }else{
                                Log.i(TAG, "실패");
                            }
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
    private void navigateToDashboardFragment() {

        // DashboardFragment로 이동하는 코드
        finish();
    }

}
