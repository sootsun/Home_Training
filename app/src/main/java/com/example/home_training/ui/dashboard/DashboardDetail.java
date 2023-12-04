package com.example.home_training.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
    private TextView title, content, id;
    private ListView comments;
    private EditText commentEdit;
    private Button addComment;
    private String serverUrl = "http://13.124.143.232:4000/community/inquiry";

    @SuppressLint("SetTextI18n")
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

        //게시글 표시
        title.setText(selectedPost.get(0));
        id.setText("작성자 : " + selectedPost.get(1));
        content.setText(selectedPost.get(2));


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
