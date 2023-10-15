package com.example.home_training.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.home_training.databinding.FragmentMypageBinding;
import com.example.home_training.databinding.FragmentNotificationsBinding;
import com.example.home_training.ui.notifications.NotificationsViewModel;

public class MypageFragment extends Fragment {

    private @NonNull FragmentMypageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MypageViewModel mypageViewModel =
                new ViewModelProvider(this).get(MypageViewModel.class);

        binding = FragmentMypageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}