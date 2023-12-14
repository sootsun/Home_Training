package com.example.home_training.ui.dashboard;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.home_training.R;

import java.util.ArrayList;

public class DashboardDetail extends AppCompatActivity {
    private TextView title, content, id, back3;
    private ListView comments;
    private EditText commentEdit;
    private Button addComment;
    private String serverUrl = "http://52.79.239.35:4000/community/inquiry";

    @SuppressLint({"SetTextI18n", "RestrictedApi"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard_detail);

        Intent intent = getIntent();
        ArrayList<String> selectedPost = intent.getStringArrayListExtra("selectedPost");

        title = findViewById(R.id.titleName);
        content = findViewById(R.id.contentText);
        id = findViewById(R.id.idName);
        comments = findViewById(R.id.commentList);
        commentEdit = findViewById(R.id.commentEdit);
        addComment = findViewById(R.id.addCommentButton);

        back3 = findViewById(R.id.back);
        back3.setOnClickListener(v -> onBackPressed());

        String Title = getIntent().getStringExtra("clickedTitle");
        String Id = getIntent().getStringExtra("clickedId");
        String Content = getIntent().getStringExtra("clickedContent");

        //게시글 표시
        title.setText(Title);
        id.setText("작성자 : " + Id);
        content.setText(Content);


        /*
        //댓글 표시
        ArrayAdapter<String> commentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, post.getComments());
        comments.setAdapter(commentsAdapter);

        //댓글 추가 버튼
        addComment.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String newComment = commentEdit.getText().toString();
                if(!newComment.isEmpty()){

                    commentsAdapter.notifyDataSetChanged();
                    commentEdit.setText("");
                }
            }
        });*/
    }
}
