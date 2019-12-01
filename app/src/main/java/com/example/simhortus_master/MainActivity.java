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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Boolean isValid = true;
    private static FragmentManager fragmentManager;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.clearAnimation();


        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
        } else  {
            loadLayout();
        }
    }

    public Boolean replaceLayout() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String uID = firebaseUser.getUid();

        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(uID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("garden_id")) isValid = false;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        return isValid;
    }

    public Boolean loadLayout() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String uID = firebaseUser.getUid();

        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(uID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("garden_id")) {
                    Fragment  selectedFragment = new DashboardFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                } else {
                    Fragment  selectedFragment = new EmptyFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
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

                    Fragment  selectedFragment = null;

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


