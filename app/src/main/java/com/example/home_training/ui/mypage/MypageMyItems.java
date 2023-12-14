package com.example.home_training.ui.mypage;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.home_training.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MypageMyItems extends AppCompatActivity {

    private TextView back;
    private ListView myItems;
    private String MyId;
    private ArrayList<String> postTitles;
    private String serverUrl = "http://52.79.239.35:4000/item/useritem";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mypage_myitems);

        back = findViewById(R.id.myItemBack);
        back.setOnClickListener(v -> onBackPressed());

        myItems = findViewById(R.id.myItemListview);

        postTitles = new ArrayList<>();

        MyId = getIntent().getStringExtra("userId");

        Log.i(TAG, "id = " + MyId);

        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("userid", MyId)
                .build();

        Request request = new Request.Builder()
                .url(serverUrl) // 실제 API의 엔드포인트로 변경
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        String responseBody = response.body().string();
                        // JSON 배열 파싱
                        JSONArray jsonArray = new JSONArray(responseBody);
                        Log.i(TAG, "onResponse 작동");

                        // JSONArray에서 item 추출
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String itemName = jsonArray.getString(i);
                            postTitles.add(itemName);

                            Log.i(TAG, "json item 추출 중");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        // 데이터를 가져온 후 리스트뷰에 표시
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MypageMyItems.this,
                                                android.R.layout.simple_list_item_1, postTitles);

                                        myItems.setAdapter(adapter);
                                    });
                                }
                            });

                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse는 작동 되나 배열 파싱 실패");
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
