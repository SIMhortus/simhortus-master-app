package com.example.simhortus_master;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Boolean isValid = true;
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
        } else {

        }
    }

    public Boolean replaceLayout() {

        Global.getRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("deviceID")) isValid = false;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        return isValid;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.dashboard:
                            if (replaceLayout() == true) {
                                selectedFragment = new EmptyFragment();
                            } else  {
                                selectedFragment = new DashboardFragment();
                            }

                            break;
                        case R.id.herbs:
                            if (replaceLayout() == true) {
                                selectedFragment = new EmptyFragment();
                            } else  {
                                selectedFragment = new GardenFragment();
                            }

                            break;
                        case R.id.notifications:
                            if (replaceLayout() == true) {
                                selectedFragment = new EmptyFragment();
                            } else  {
                                selectedFragment = new NotificationsFragment();
                            }
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


