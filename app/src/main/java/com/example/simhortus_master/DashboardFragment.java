package com.example.simhortus_master;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class DashboardFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout dshLnr;
    public GridLayout grdPot1, grdPot2, grdPot3, grdPot4, grdPot5, grdPot6;
    public TextView txtSmPot1, txtSmPot2, txtSmPot3, txtSmPot4, txtSmPot5, txtSmPot6, txtTemp, txtHum, txtTank;
    public ImageView imgIcPot1, imgIcPot2, imgIcPot3, imgIcPot4, imgIcPot5, imgIcPot6;
    public String uid, gardenID;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseAuth mAuth;
    public int potCounter = 0;


    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        final View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_items);
//        grdPot1 = rootView.findViewById(R.id.grd_pot1);
//        grdPot2 = rootView.findViewById(R.id.grd_pot2);
//        grdPot3 = rootView.findViewById(R.id.grd_pot3);
//        grdPot4 = rootView.findViewById(R.id.grd_pot4);
//        grdPot5 = rootView.findViewById(R.id.grd_pot5);
//        grdPot6 = rootView.findViewById(R.id.grd_pot6);
//        imgIcPot1 = rootView.findViewById(R.id.img_ic_pot1);
//        imgIcPot2 = rootView.findViewById(R.id.img_ic_pot2);
//        imgIcPot3 = rootView.findViewById(R.id.img_ic_pot3);
//        imgIcPot4 = rootView.findViewById(R.id.img_ic_pot4);
//        imgIcPot5 = rootView.findViewById(R.id.img_ic_pot5);
//        imgIcPot6 = rootView.findViewById(R.id.img_ic_pot6);
//        txtSmPot1 = rootView.findViewById(R.id.txt_sm_pot1);
//        txtSmPot2 = rootView.findViewById(R.id.txt_sm_pot2);
//        txtSmPot3 = rootView.findViewById(R.id.txt_sm_pot3);
//        txtSmPot4 = rootView.findViewById(R.id.txt_sm_pot4);
//        txtSmPot5 = rootView.findViewById(R.id.txt_sm_pot5);
//        txtSmPot6 = rootView.findViewById(R.id.txt_sm_pot6);
        txtTemp = rootView.findViewById(R.id.txt_temp);
        txtHum = rootView.findViewById(R.id.txt_hum);
        txtTank = rootView.findViewById(R.id.txt_tank);
        dshLnr = rootView.findViewById(R.id.dsh_lnr);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference("Users");

        //getting userID
        if (user != null) {
            uid = user.getUid();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);

                        }
                    }
                }, 1000);
            }
        });

        //getting garden ID
        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting garden ID
                gardenID = dataSnapshot.child("garden_id").getValue().toString();
                final DatabaseReference gardenReference = firebaseDatabase.getReference().child("Garden").child(gardenID).child("status");

                //getting and displaying garden status
                gardenReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //displaying garden status
                        txtTemp.setText(dataSnapshot.child("temperature").getValue().toString() + "°C");
                        txtHum.setText(dataSnapshot.child("humidity").getValue().toString() + "%");
                        txtTank.setText(dataSnapshot.child("water_tank").getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                final DatabaseReference potReference = firebaseDatabase.getReference().child("Pot").child(gardenID);

                //getting and displaying soil moisture of each pot
                potReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //create grid in every pot
                            GridLayout grdLayout = new GridLayout(getActivity());
                            grdLayout.setLayoutParams(rootView.getLayoutParams());
                            dshLnr.addView(grdLayout);

                            //create imageview
                            final ImageView imgView = new ImageView(getActivity());
                            imgView.setLayoutParams(new LinearLayout.LayoutParams(31, 31));
                            grdLayout.addView(imgView);

                            //create textview
                            final TextView txtView = new TextView(getActivity());
                            txtView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            txtView.setTextSize(23);
                            txtView.setPadding(10,0,0,0);
                            grdLayout.addView(txtView);

                            //get herb
                            final String herb = snapshot.child("type_of_plant").getValue().toString();
                            //get soil moisture
                            final String soilMoisture = snapshot.child("status").child("soil_moisture").getValue().toString();
                            //get pot number
                            final String potNumber = snapshot.getKey().toString();
                            //getting and displaying herbs
                            final DatabaseReference herbReference = firebaseDatabase.getReference().child("Herbs").child(herb);
                            herbReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot1) {
                                    final String herbImage = dataSnapshot1.child("image").getValue().toString();
                                    Glide.with(getActivity()).load(herbImage).into(imgView);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                            //getting and displaying pot number and soil moisture
                            txtView.setText(potNumber + ": " + soilMoisture);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

//    private void getGardenID(){
//    }


}
