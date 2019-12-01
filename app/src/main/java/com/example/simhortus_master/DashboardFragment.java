package com.example.simhortus_master;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
        grdPot1 = rootView.findViewById(R.id.grd_pot1);
        grdPot2 = rootView.findViewById(R.id.grd_pot2);
        grdPot3 = rootView.findViewById(R.id.grd_pot3);
        grdPot4 = rootView.findViewById(R.id.grd_pot4);
        grdPot5 = rootView.findViewById(R.id.grd_pot5);
        grdPot6 = rootView.findViewById(R.id.grd_pot6);
        imgIcPot1 = rootView.findViewById(R.id.img_ic_pot1);
        imgIcPot2 = rootView.findViewById(R.id.img_ic_pot2);
        imgIcPot3 = rootView.findViewById(R.id.img_ic_pot3);
        imgIcPot4 = rootView.findViewById(R.id.img_ic_pot4);
        imgIcPot5 = rootView.findViewById(R.id.img_ic_pot5);
        imgIcPot6 = rootView.findViewById(R.id.img_ic_pot6);
        txtSmPot1 = rootView.findViewById(R.id.txt_sm_pot1);
        txtSmPot2 = rootView.findViewById(R.id.txt_sm_pot2);
        txtSmPot3 = rootView.findViewById(R.id.txt_sm_pot3);
        txtSmPot4 = rootView.findViewById(R.id.txt_sm_pot4);
        txtSmPot5 = rootView.findViewById(R.id.txt_sm_pot5);
        txtSmPot6 = rootView.findViewById(R.id.txt_sm_pot6);
        txtTemp = rootView.findViewById(R.id.txt_temp);
        txtHum = rootView.findViewById(R.id.txt_hum);
        txtTank = rootView.findViewById(R.id.txt_tank);

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
                final DatabaseReference potReference = firebaseDatabase.getReference().child("Pot").child(gardenID);
//                final DatabaseReference potReference2 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("pot2");
//                final DatabaseReference potReference3 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("pot3");
//                final DatabaseReference potReference4 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("pot4");
//                final DatabaseReference potReference5 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("pot5");
//                final DatabaseReference potReference6 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("pot6");

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

                //getting and displaying soil moisture of each pot
                potReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if pot is empty
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(snapshot.child("empty").getValue().toString().equals("false")){
                                potCounter++;
                                switch(potCounter){
                                    case 1: imgIcPot1.setBackgroundResource(R.drawable.bg_green);
                                        final DatabaseReference potReference1 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("pot1");
                                        final DatabaseReference potSMReference1 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("pot1").child("status");
                                        potReference1.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //display type of plant
                                                String typeOfPlant = (dataSnapshot.child("type_of_plant").getValue().toString());
                                                if(typeOfPlant.equals("Sweet Basil")){
                                                    imgIcPot1.setImageResource(R.drawable.ic_sweet_basil);
                                                }
                                                else if(typeOfPlant.equals("Thyme")){
                                                    imgIcPot1.setImageResource(R.drawable.ic_thyme);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        potSMReference1.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //display soil moisture
                                                txtSmPot1.setText((dataSnapshot.child("soil_moisture").getValue().toString()));
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    case 2: imgIcPot2.setBackgroundResource(R.drawable.bg_green);
                                        final DatabaseReference potReference2 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("pot2");
                                        final DatabaseReference potSMReference2 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("pot2").child("status");
                                        potReference2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //display type of plant
                                                String typeOfPlant = (dataSnapshot.child("type_of_plant").getValue().toString());
                                                if(typeOfPlant.equals("Sweet Basil")){
                                                    imgIcPot2.setImageResource(R.drawable.ic_sweet_basil);
                                                }
                                                else if(typeOfPlant.equals("Thyme")){
                                                    imgIcPot2.setImageResource(R.drawable.ic_thyme);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        potSMReference2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //display soil moisture
                                                txtSmPot2.setText((dataSnapshot.child("soil_moisture").getValue().toString()));
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    case 3: imgIcPot3.setBackgroundResource(R.drawable.bg_green);
                                    case 4: imgIcPot4.setBackgroundResource(R.drawable.bg_green);
                                    case 5: imgIcPot5.setBackgroundResource(R.drawable.bg_green);
                                    case 6: imgIcPot6.setBackgroundResource(R.drawable.bg_green);
                                }
                            }
                            else {
                                potCounter++;
                                switch(potCounter){
                                    case 1: imgIcPot1.setBackgroundResource(R.drawable.bg_grey);
                                        imgIcPot1.setImageResource(R.drawable.ic_pot);;
//                                        txtSmPot1.setText("");
                                    case 2: imgIcPot2.setBackgroundResource(R.drawable.bg_grey);
                                        imgIcPot2.setImageResource(R.drawable.ic_pot);;
                                    case 3: imgIcPot3.setBackgroundResource(R.drawable.bg_grey);
                                        imgIcPot3.setImageResource(R.drawable.ic_pot);;
                                    case 4: imgIcPot4.setBackgroundResource(R.drawable.bg_grey);
                                        imgIcPot4.setImageResource(R.drawable.ic_pot);;
                                    case 5: imgIcPot5.setBackgroundResource(R.drawable.bg_grey);
                                        imgIcPot5.setImageResource(R.drawable.ic_pot);;
                                    case 6: imgIcPot6.setBackgroundResource(R.drawable.bg_grey);
                                        imgIcPot6.setImageResource(R.drawable.ic_pot);;
                                }
                            }
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
