package com.example.home_training;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.home_training.R;

import com.example.home_training.ui.dashboard.DashboardFragment;
import com.example.home_training.ui.home.HomeFragment;
import com.example.home_training.ui.mypage.MypageFragment;
import com.example.home_training.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    DashboardFragment dashboardFragment;
    MypageFragment mypageFragment;
    NotificationsFragment notificationsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        dashboardFragment = new DashboardFragment();
        mypageFragment = new MypageFragment();
        notificationsFragment = new NotificationsFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.nav_view);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                if (Item.getItemId() == R.id.navigation_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    return true;
                }
                else if (Item.getItemId() == R.id.navigation_dashboard){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, dashboardFragment).commit();
                    return true;
                }
                else if (Item.getItemId() == R.id.navigation_mypage){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mypageFragment).commit();
                    return true;
                }
                else if (Item.getItemId() == R.id.navigation_notifications){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, notificationsFragment).commit();
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

}