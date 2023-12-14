package com.example.home_training.ui.dashboard;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.home_training.R;
import com.example.home_training.ui.login.Signup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.http.POST;

public class DashboardFragment extends Fragment {

    private String UserId;

    private ListView listView;
    private List<DashboardPost> posts;
    private ArrayList<String> postTitles;
    private ArrayList<String> displayList;
    private String selectedPost;
    private String comTitle, comContent, comId;
    private OkHttpClient client;
    private Button textWrite;
    private ArrayList<ArrayList<String>> postContent = new ArrayList<>();
    private String serverUrl = "http://52.79.239.35:4000/community/inquiry";

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        textWrite = view.findViewById(R.id.writeText);

        listView = view.findViewById(R.id.communityBoard);
        postTitles = new ArrayList<>();

        Intent intent = getActivity().getIntent();

        // 인텐트로부터 데이터 가져오기
        UserId = intent.getStringExtra("userId");

        Log.i(TAG, "시작");
        FetchPosts();

        // 리스트뷰의 항목 클릭 이벤트 설정

        textWrite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DashboardWrite.class);
                intent.putExtra("userId", UserId);
                startActivity(intent);
            }
        });
        return view;
    }

    @SuppressLint("RestrictedApi")
    private void FetchPosts() {
        Log.i(TAG, "FetchPosts 시작");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(serverUrl) // 실제 API의 엔드포인트로 변경
                .build();

        Log.i(TAG, "request 작동");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "통신 실패");
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

                        // JSONArray에서 title, id, content 추출
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String title = jsonObject.getString("title");
                            String id = jsonObject.getString("id");
                            String content = jsonObject.getString("content");
                            postTitles.add(title + "\n" + id);
                            ArrayList<String> postItem = new ArrayList<>();
                            postItem.add(title);
                            postItem.add(id);
                            postItem.add(content);
                            postContent.add(postItem);

                            ArrayList<String> displayList = new ArrayList<>();
                            for (ArrayList<String> item : postContent) {
                                displayList.add(item.get(0)); // title만 추가
                            }

                            new Handler(Looper.getMainLooper()).post(() -> {
                                // 데이터를 가져온 후 리스트뷰에 표시
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                                        android.R.layout.simple_list_item_1, displayList);
                                listView.setAdapter(adapter);
                            });
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse는 작동 되나 배열 파싱 실패");
                        e.printStackTrace();
                    }

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> clickedItem = postContent.get(position);
                String clickedTitle = clickedItem.get(0);
                String clickedId = clickedItem.get(1);
                String clickedContent = clickedItem.get(2);

                // DashboardDetail 액티비티로 이동
                Intent intent = new Intent(getActivity(), DashboardDetail.class);
                intent.putExtra("clickedTitle", clickedTitle);
                intent.putExtra("clickedId", clickedId);
                intent.putExtra("clickedContent", clickedContent);
                startActivity(intent);
            }
        });
    }

    public void updateData() {
        // 여기에서 데이터를 다시 불러오거나 갱신하는 작업 수행
        FetchPosts();
    }
    @SuppressLint("RestrictedApi")
    private void parseJsonData(String jsonData){
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            Log.i(TAG, "받아오기 성공");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject post = jsonArray.getJSONObject(i);
                String title = post.getString("title");
                String content = post.getString("content");
                String author = post.getString("id");  // 서버에서 제공하는 경우에만 사용

                // 여기서 필요한 정보를 가지고 작업 (예: 리스트에 추가)
                postTitles.add(title + "\n" + author);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}