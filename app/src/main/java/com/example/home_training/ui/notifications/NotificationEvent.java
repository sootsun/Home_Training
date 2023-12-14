package com.example.home_training.ui.notifications;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.home_training.R;

public class NotificationEvent extends AppCompatActivity {
    private TextView back3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_notifications_events);

        back3 = findViewById(R.id.back3);
        back3.setOnClickListener(v -> onBackPressed());
    }
}
