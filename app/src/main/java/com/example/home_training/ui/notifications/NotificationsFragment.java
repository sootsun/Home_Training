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

import com.example.home_training.R;
import com.example.home_training.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        TextView floatingActionButton = view.findViewById(R.id.floatingActionButton);
        TextView event = getView().findViewById(R.id.event);


 /*       floatingActionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationsFragment.this, notification.class);
                startActivity(intent);
            }
        });*/

        return null;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}