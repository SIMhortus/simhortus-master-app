package com.example.simhortus_master;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HerbFragment extends Fragment implements View.OnClickListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    TextView hrbTxtHerbName, hrbTxtHeight, hrbTxtGrowth, hrbTxtAge, hrbTxtMaturity;
    MaterialButton hrbBtnRemove;
    ImageButton hrbBtnBack;
    String[] hrbHeightArr;
    Integer selectionCount = 0;
    String herbName;
    public String uid, gardenID, potPosition;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_herb, container, false);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_items);

        hrbTxtHerbName = rootView.findViewById(R.id.hrb_txt_herb_name);
        hrbTxtHeight = rootView.findViewById(R.id.hrb_txt_height);
        hrbTxtGrowth = rootView.findViewById(R.id.hrb_txt_growth);
        hrbTxtAge = rootView.findViewById(R.id.hrb_txt_age);
        hrbTxtMaturity = rootView.findViewById(R.id.hrb_txt_maturity);
        hrbBtnRemove = rootView.findViewById(R.id.hrb_btn_remove);
        hrbBtnBack = rootView.findViewById(R.id.btn_back);
        hrbBtnRemove.setOnClickListener(this);
        hrbBtnBack.setOnClickListener(this);

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
                final DatabaseReference potReference = firebaseDatabase.getReference().child("Pot").child(gardenID).child(potPosition).child("status").child("height");

                //getting and displaying soil moisture of each pot
                potReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            hrbHeightArr[selectionCount] = snapshot.getKey().toString();
//                            selectionCount++;
//                            //getdate
                            Date c = Calendar.getInstance().getTime();

                            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                            String formattedDate = df.format(c);

//                            getting and displaying pot number and soil moisture
                            if(snapshot.getKey().equals(formattedDate)){
                                hrbTxtHeight.setText(snapshot.getValue().toString());
                            }

                            //getting and displaying herbs
//                            final DatabaseReference herbReference = firebaseDatabase.getReference("Herbs").child(herb);
//                            herbReference.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot1) {
//                                    final String herbImage = dataSnapshot1.child("image").getValue().toString();
//                                    Glide.with(getActivity()).load(herbImage).into(imgView);
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//                                }
//                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                final DatabaseReference herbReference = firebaseDatabase.getReference().child("Pot").child(gardenID).child(potPosition);

                //getting herbname
                herbReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        herbName = dataSnapshot.child("type_of_plant").getValue().toString();
                        hrbTxtHerbName.setText(herbName);
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

    @Override
    public void onClick(View v) {
        if(v == hrbBtnRemove){
            removeHerb();
        }
        else if(v == hrbBtnBack){
            GardenFragment fragment = new GardenFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void removeHerb(){
        DialogRemovePlant dialogRemovePlant = new DialogRemovePlant();
        dialogRemovePlant.herbName = herbName;
        dialogRemovePlant.potPosition = potPosition;
        dialogRemovePlant.gardenID = gardenID;
        dialogRemovePlant.show(getFragmentManager(), "Remove Herb");
    }

}
