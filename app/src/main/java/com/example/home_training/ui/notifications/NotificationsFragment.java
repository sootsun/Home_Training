package com.example.home_training.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.home_training.R;
import com.example.home_training.databinding.FragmentNotificationsBinding;
import com.example.home_training.ui.dashboard.DashboardWrite;

public class NotificationsFragment extends Fragment {
    private String UserId;

    private TextView notification, event, pointShop;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        notification = view.findViewById(R.id.floatingActionButton);
        event = view.findViewById(R.id.event);
        pointShop = view.findViewById(R.id.point_shop);

        Intent intent = getActivity().getIntent();

        // 인텐트로부터 데이터 가져오기
        UserId = intent.getStringExtra("userId");

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getActivity(), NotificationNotice.class);
                startActivity(intent4);
            }
        });

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(getActivity(), NotificationEvent.class);
                startActivity(intent5);
            }
        });

        pointShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationPointShop.class);
                intent.putExtra("userId", UserId);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}