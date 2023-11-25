package com.example.home_training.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.home_training.R;
import com.example.home_training.databinding.FragmentDashboardBinding;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.http.POST;

public class DashboardFragment extends Fragment {

    private ListView listView;
    private List<DashboardPost> posts;
    private OkHttpClient client;
    private String serverUrl = "http://15.164.103.132:4000/user/community";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(serverUrl)
                .build();

        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "통신 오류" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String responseBody = response.body().string();
                    posts = parsePostJson(responseBody);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView = view.findViewById(R.id.communityBoard);
                            PostAdapter adapter = new PostAdapter(getActivity(), posts);
                            listView.setAdapter(adapter);

                            //리스트뷰 아이템 클릭 이벤트 처리
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String postId = posts.get(position).getId();
                                    Request postDetailRequest = new Request.Builder()
                                            .url(serverUrl+ "/" + postId)
                                            .build();

                                    client.newCall(postDetailRequest).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(), "통신 오류" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if(response.isSuccessful()){
                                                String postDetailBody = response.body().string();
                                                DashboardPost selectedPost = (DashboardPost) parsePostJson(postDetailBody);

                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent = new Intent(getActivity(), DashboardDetail.class);
                                                        intent.putExtra("post", (CharSequence) selectedPost);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }else{
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), "게시글을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "게시글 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private List<DashboardPost> parsePostJson(String json){
        return null;
    }
}