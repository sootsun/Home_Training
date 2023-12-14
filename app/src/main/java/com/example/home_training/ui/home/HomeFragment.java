package com.example.home_training.ui.home;

import static android.app.Activity.RESULT_OK;
import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.home_training.R;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private EditText mEtAdress;
    private EditText mEtAdress1;
    private EditText mEtAdress2;
    private Button myButton;
    private Button myButton1;
    private Button myButton2;
    private TextView cTotalPoints; // 총 포인트를 표시할 TextView
    private int totalPoints = 0;
    private String UserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Intent intent = getActivity().getIntent();
        // 인텐트로부터 데이터 가져오기
        UserId = intent.getStringExtra("userId");

        mEtAdress = view.findViewById(R.id.et_address);
        mEtAdress1 = view.findViewById(R.id.et_address1);
        mEtAdress2 = view.findViewById(R.id.et_address2);

        myButton = view.findViewById(R.id.open_website_button);
        myButton1 = view.findViewById(R.id.open_website_button1);
        myButton2 = view.findViewById(R.id.open_website_button2);

        // block touch
        mEtAdress.setFocusable(false);
        mEtAdress1.setFocusable(false);
        mEtAdress2.setFocusable(false);

        cTotalPoints = view.findViewById(R.id.total_points);

        myButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(getActivity(), SearchActivity2.class);
            // 필요한 경우 추가 데이터 전달
            intent1.putExtra("URL_KEY", "https://homepushup.netlify.app/"); // 첫 번째 URL
            getSearchResult.launch(intent1);
        });

        myButton1.setOnClickListener(v -> {
            Intent intent2 = new Intent(getActivity(), SearchActivity2.class);
            // 필요한 경우 추가 데이터 전달
            intent2.putExtra("URL_KEY", "https://homesquat.netlify.app/"); // 두 번째 URL
            getSearchResult.launch(intent2);
        });

        myButton2.setOnClickListener(v -> {
            Intent intent3 = new Intent(getActivity(), SearchActivity2.class);
            // 필요한 경우 추가 데이터 전달
            intent3.putExtra("URL_KEY", "https://hometraise.netlify.app/"); // 두 번째 URL
            getSearchResult.launch(intent3);
        });

        mEtAdress.setOnClickListener(v -> {
            Intent intent4 = new Intent(getActivity(), SearchActivity2.class);
            intent4.putExtra("requestFrom", "address");
            getSearchResult.launch(intent4);
        });

        mEtAdress1.setOnClickListener(v -> {
            Intent intent5 = new Intent(getActivity(), SearchActivity2.class);
            intent5.putExtra("requestFrom", "address");
            getSearchResult.launch(intent5);
        });

        mEtAdress2.setOnClickListener(v -> {
            Intent intent6 = new Intent(getActivity(), SearchActivity2.class);
            intent6.putExtra("requestFrom", "address");
            getSearchResult.launch(intent6);
        });

        return view;
    }

    OkHttpClient client = new OkHttpClient();

    //서버로 보내는 코드

    @SuppressLint("RestrictedApi")
    private void sendDatatoServer(int completedCount, String UserId) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put("point", completedCount);
            jsonObject.put("userid", UserId);
            Log.i(TAG, "post완료");
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("http://52.79.239.35:4000/item/point")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 네트워크 오류 처리...
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    Log.i(TAG, "response 완료");
                    // 서버로부터의 응답 처리...

                } else {
                    // 서버 오류 처리...
                }
            }
        });
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("MainActivity", "ActivityResult: " + result.getResultCode());
                // Search Activity 로부터의 결과 값이 이곳으로 전달 된다.. (setResult에 의해..)
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        String data = result.getData().getStringExtra("data");
                        String requestFrom = result.getData().getStringExtra("requestFrom");

                        Log.d("MainActivity", "Received data: " + data); // 받은 데이터 로그
                        Log.d("MainActivity", "Request from: " + requestFrom); // requestFrom 값 로그


                        showAlertWithData(data);


                        if ("address".equals(requestFrom)) {
                            mEtAdress.setText(data);
                        } else if ("address1".equals(requestFrom)) {
                            mEtAdress1.setText(data);
                        } else if ("address2".equals(requestFrom)) {
                            mEtAdress2.setText(data);
                        }

                        // 받아온 데이터를 정수로 변환하여 totalPoints에 추가
                        try {
                            int points = Integer.parseInt(data);
                            totalPoints += points * 100;
                            // 총 포인트를 TextView에 업데이트
                            cTotalPoints.setText("총 포인트: " + totalPoints);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            // 데이터 변환 오류 처리
                        }

                    }
                }


            }
    );

    // 결과창을 띄우는 코드

    private void showAlertWithData(String data) {
        // 문자열 data를 정수로 변환
        int completedCount;
        try {
            completedCount = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            completedCount = 0; // 변환에 실패한 경우, 기본값으로 0 설정
        }

        // 포인트 계산
        int pointsEarned = completedCount * 100;

        // 메시지 구성
        String message = "완료 횟수: " + completedCount + "\n오늘 얻은 포인트: " + pointsEarned;

        new AlertDialog.Builder(getContext())
                .setTitle("오늘 완료한 횟수")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    String userId = "사용자의 고유 아이디"; // 여기에 실제 유저 아이디를 가져오는 로직을 구현합니다.
                    sendDatatoServer(pointsEarned, UserId); // userId를 매개변수로 전달
                })
                .show();
    }

}