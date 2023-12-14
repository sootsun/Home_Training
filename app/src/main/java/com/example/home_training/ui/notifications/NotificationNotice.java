package com.example.home_training.ui.notifications;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.home_training.R;

public class NotificationNotice extends AppCompatActivity {
    private TextView back4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notifications_notice);

        back4 = findViewById(R.id.back4);
        back4.setOnClickListener(v -> onBackPressed());
    }
}
