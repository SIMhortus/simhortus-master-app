package com.example.simhortus_master;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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

import java.util.EventListener;
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

        final BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.clearAnimation();


        FirebaseAuth mAuthInstance = mAuth.getInstance();
        final FirebaseUser user = mAuthInstance.getCurrentUser();
        final String uid = user.getUid();

        if (user == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
        } else  {
            replaceLayout();

            Global.getRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("pending")){
                        bottomNav.getMenu().getItem(0).setEnabled(false);
                        bottomNav.getMenu().getItem(1).setEnabled(false);

                        MenuItem menuItem = (MenuItem)bottomNav.getMenu().findItem(R.id.menu);
                        menuItem.setChecked(true);

                        Fragment selectedFragment = new MenuFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    } else {

                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            if (child.getValue().toString().equals("user")){


                            } else {

                                bottomNav.getMenu().getItem(0).setEnabled(true);
                                bottomNav.getMenu().getItem(1).setEnabled(true);

                                MenuItem menuItem = (MenuItem)bottomNav.getMenu().findItem(R.id.dashboard);
                                menuItem.setChecked(true);

                                Fragment selectedFragment = new DashboardFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                            }
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }




        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        replaceLayout();

    }

    public Boolean replaceLayout() {

        FirebaseAuth mAuthInstance = mAuth.getInstance();
        FirebaseUser user = mAuthInstance.getCurrentUser();
        String uID= user.getUid();

        Global.showToast(uID, MainActivity.this);

        DatabaseReference databaseReference = Global.getRef.child(uID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!(dataSnapshot.hasChild("garden_id"))){
                        startActivity(new Intent(MainActivity.this, ScanGarden.class));
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


