package com.example.simhortus_master;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.clearAnimation();

    }

    protected void onStart() {
        super.onStart();

        final FirebaseUser user = Global.mAuthInstance.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.dashboard:
                            selectedFragment = new DashboardFragment();
                            break;
                        case R.id.herbs:
                            selectedFragment = new GardenFragment();
                            break;
                        case R.id.notifications:
                            selectedFragment = new NotificationsFragment();
                            break;
                        case R.id.menu:
                            selectedFragment = new MenuFragment();

                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}
