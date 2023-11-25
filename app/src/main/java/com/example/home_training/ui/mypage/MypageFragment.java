package com.example.home_training.ui.mypage;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.home_training.R;
import com.example.home_training.ui.login.Signup;
import com.example.home_training.ui.notifications.NotificationsViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MypageFragment extends Fragment {
    private String serverUrl = "http://15.164.103.132:4000/user/mypage";
    private OkHttpClient client = new OkHttpClient();
    private String UserId;

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView nickname, idCode, level, point;

    private ImageView profileImage;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        idCode = view.findViewById(R.id.yourIDcode);

        Intent intent = getActivity().getIntent();

        // 인텐트로부터 데이터 가져오기
        UserId = intent.getStringExtra("userId");

        Log.i(TAG, UserId);

        // 닉네임, 레벨, 포인트
        nickname = view.findViewById(R.id.yourNickname);
        level = view.findViewById(R.id.yourLevel);
        point = view.findViewById(R.id.yourPoint);

        FormBody formBody = new FormBody.Builder()
                .add("userId", UserId)
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
                    String userNickname = jsonResponse.getString("name");
                    int userPoint = jsonResponse.getInt("point");
                    String uP = String.valueOf(userPoint);
                    String userLevel = jsonResponse.getString("level");

                    Log.i(TAG, UserId);
                    Log.i(TAG, userNickname);

                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            nickname.setText(userNickname);
                            point.setText(uP);
                            level.setText(userLevel);
                            idCode.setText(UserId);
                        }
                    });

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    /*
    public void changeProfileImage(View view){
        //갤러리를 열어 이미지를 선택
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();

            try{
                // 받아온 사진을 프로필사진탭에 게시
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profileImage.setImageBitmap(selectedImage);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImage = view.findViewById(R.id.account_profile);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> onActivityResult(result.getResultCode(), result.getData()));

        profileImage.setOnClickListener(v -> pickImage());

    }
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void onActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                // 선택한 image가 게시가 됨
                InputStream imageStream = requireActivity().getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profileImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}