package com.example.home_training.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.home_training.MainActivity;
import com.example.home_training.R;
import com.example.home_training.ui.mypage.MypageFragment;

public class NotificationPointShopComplete extends AppCompatActivity {

    private Button moveMypage;
    private String MyId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventshop_purchase_complete);

        MyId = getIntent().getStringExtra("userId");

        moveMypage = findViewById(R.id.moveMypage);

        moveMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationPointShopComplete.this, MainActivity.class);
                intent.putExtra("userId", MyId);
                startActivity(intent);
            }
        });
    }
}
