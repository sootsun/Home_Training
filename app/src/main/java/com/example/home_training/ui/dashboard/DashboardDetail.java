package com.example.home_training.ui.dashboard;

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

public class DashboardDetail extends AppCompatActivity {
    private TextView title, content;
    private ListView comments;
    private EditText commentEdit;
    private Button addComment;
    private DashboardPost post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard_detail);

        Intent intent = getIntent();
        post = (DashboardPost) intent.getSerializableExtra("post");

        title = findViewById(R.id.titleName);
        content = findViewById(R.id.contentText);
        comments = findViewById(R.id.commentList);
        commentEdit = findViewById(R.id.commentEdit);
        addComment = findViewById(R.id.addCommentButton);

        //게시글 표시
        title.setText(post.getTitle());
        content.setText(post.getContent());

        //댓글 표시
        ArrayAdapter<String> commentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, post.getComments());
        comments.setAdapter(commentsAdapter);

        //댓글 추가 버튼
        addComment.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String newComment = commentEdit.getText().toString();
                if(!newComment.isEmpty()){
                    post.addComment(newComment);
                    commentsAdapter.notifyDataSetChanged();
                    commentEdit.setText("");
                }
            }
        });
    }
}
