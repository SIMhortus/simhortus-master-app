package com.example.simhortus_master;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GardenFragment extends Fragment implements View.OnClickListener{

    private static FragmentManager fragmentManager;
    //garden visual pots
    private ImageButton gdnPot1, gdnPot2, gdnPot3, gdnPot4, gdnPot5, gdnPot6;
    //garden information
    private LinearLayout gdnLnrEmpty, gdnLnrPot1, gdnLnrPot2, gdnLnrPot3, gdnLnrPot4, gdnLnrPot5, gdnLnrPot6;
    private ImageView gdnImgPot1, gdnImgPot2, gdnImgPot3, gdnImgPot4, gdnImgPot5, gdnImgPot6;
    private TextView gdnTxtPot1, gdnTxtPot2, gdnTxtPot3, gdnTxtPot4, gdnTxtPot5, gdnTxtPot6;
//    private
    public String uid, gardenID;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseAuth mAuth;
    public Integer potNumber, potEmptyCounter = 0;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        final View rootView = inflater.inflate(R.layout.fragment_garden, container, false);

        gdnPot1 = rootView.findViewById(R.id.gdn_pot1);
        gdnPot2 = rootView.findViewById(R.id.gdn_pot2);
        gdnPot3 = rootView.findViewById(R.id.gdn_pot3);
        gdnPot4 = rootView.findViewById(R.id.gdn_pot4);
        gdnPot5 = rootView.findViewById(R.id.gdn_pot5);
        gdnPot6 = rootView.findViewById(R.id.gdn_pot6);

        gdnPot1.setOnClickListener(this);
        gdnPot2.setOnClickListener(this);
        gdnPot3.setOnClickListener(this);
        gdnPot4.setOnClickListener(this);
        gdnPot5.setOnClickListener(this);
        gdnPot6.setOnClickListener(this);

        gdnLnrEmpty = rootView.findViewById(R.id.gdn_lnr_empty);
        gdnLnrPot1 = rootView.findViewById(R.id.gdn_lnr_pot1);
        gdnLnrPot2 = rootView.findViewById(R.id.gdn_lnr_pot2);
        gdnLnrPot3 = rootView.findViewById(R.id.gdn_lnr_pot3);
        gdnLnrPot4 = rootView.findViewById(R.id.gdn_lnr_pot4);
        gdnLnrPot5 = rootView.findViewById(R.id.gdn_lnr_pot5);
        gdnLnrPot6 = rootView.findViewById(R.id.gdn_lnr_pot6);

        gdnImgPot1 = rootView.findViewById(R.id.gdn_img_pot1);
        gdnImgPot2 = rootView.findViewById(R.id.gdn_img_pot2);
        gdnImgPot3 = rootView.findViewById(R.id.gdn_img_pot3);
        gdnImgPot4 = rootView.findViewById(R.id.gdn_img_pot4);
        gdnImgPot5 = rootView.findViewById(R.id.gdn_img_pot5);
        gdnImgPot6 = rootView.findViewById(R.id.gdn_img_pot6);

        gdnTxtPot1 = rootView.findViewById(R.id.gdn_txt_pot1);
        gdnTxtPot2 = rootView.findViewById(R.id.gdn_txt_pot2);
        gdnTxtPot3 = rootView.findViewById(R.id.gdn_txt_pot3);
        gdnTxtPot4 = rootView.findViewById(R.id.gdn_txt_pot4);
        gdnTxtPot5 = rootView.findViewById(R.id.gdn_txt_pot5);
        gdnTxtPot6 = rootView.findViewById(R.id.gdn_txt_pot6);

        gdnLnrPot1.setVisibility(View.GONE);
        gdnLnrPot2.setVisibility(View.GONE);
        gdnLnrPot3.setVisibility(View.GONE);
        gdnLnrPot4.setVisibility(View.GONE);
        gdnLnrPot5.setVisibility(View.GONE);
        gdnLnrPot6.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference("Users");

        //getting userID
        if (user != null) {
            uid = user.getUid();
        }

//        gdnPot1.setBackground(getActivity().getDrawable(R.drawable.img_pot_empty));
//        gdnPot2.setBackground(getActivity().getDrawable(R.drawable.img_pot_empty));
//        gdnPot3.setBackground(getActivity().getDrawable(R.drawable.img_pot_empty));
//        gdnPot4.setBackground(getActivity().getDrawable(R.drawable.img_pot_empty));
//        gdnPot5.setBackground(getActivity().getDrawable(R.drawable.img_pot_empty));
//        gdnPot6.setBackground(getActivity().getDrawable(R.drawable.img_pot_empty));
//        gdnLnrPot1.setVisibility(View.GONE);
//        gdnLnrPot2.setVisibility(View.GONE);
//        gdnLnrPot3.setVisibility(View.GONE);
//        gdnLnrPot4.setVisibility(View.GONE);
//        gdnLnrPot5.setVisibility(View.GONE);
//        gdnLnrPot6.setVisibility(View.GONE);

        //getting garden ID
        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting garden ID
                gardenID = dataSnapshot.child("garden_id").getValue().toString();
                final DatabaseReference gardenReference = firebaseDatabase.getReference().child("Garden").child(gardenID).child("status");
                final DatabaseReference potReference = firebaseDatabase.getReference().child("Pot").child(gardenID);

                //getting and displaying plant of each pot
                potReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if pot is empty
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //get pot number
                            potNumber = Integer.parseInt(snapshot.getKey().toString().substring(4));

                            switch(potNumber){
                                case 1:
                                    final DatabaseReference potReference1 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("Pot 1");
                                    potReference1.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //display type of plant
                                            if (dataSnapshot.hasChild("type_of_plant")) {
                                                potEmptyCounter++;
                                                String typeOfPlant = (dataSnapshot.child("type_of_plant").getValue().toString());
                                                if(typeOfPlant.equals("Sweet Basil")){
                                                    gdnPot1.setBackground(getActivity().getDrawable(R.drawable.img_pot_sweet_basil));
                                                    gdnImgPot1.setImageResource(R.drawable.img_pot_sweet_basil);
                                                }
                                                else if(typeOfPlant.equals("Thyme")){
                                                    gdnPot1.setBackground(getActivity().getDrawable(R.drawable.img_pot_thyme));
                                                    gdnImgPot1.setImageResource(R.drawable.img_pot_thyme);
                                                }
                                                else if(typeOfPlant.equals("Oregano")){
                                                    gdnPot1.setBackground(getActivity().getDrawable(R.drawable.img_pot_oregano));
                                                    gdnImgPot1.setImageResource(R.drawable.img_pot_oregano);
                                                }
                                                gdnTxtPot1.setText(getString(R.string.gdn_pot_planted1) + typeOfPlant + getString(R.string.gdn_pot_planted2) +  "1");
                                                gdnLnrPot1.setVisibility(View.VISIBLE);
                                            }
                                            else{
                                                gdnPot1.setBackground(getActivity().getDrawable(R.drawable.img_pot_empty));
                                                gdnLnrPot1.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                case 2:
                                    final DatabaseReference potReference2 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("Pot 2");
                                    potReference2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //display type of plant
                                            if (dataSnapshot.hasChild("type_of_plant")) {
                                                potEmptyCounter++;
                                                String typeOfPlant = (dataSnapshot.child("type_of_plant").getValue().toString());
                                                if (typeOfPlant.equals("Sweet Basil")) {
                                                    gdnPot2.setBackground(getActivity().getDrawable(R.drawable.img_pot_sweet_basil));
                                                    gdnImgPot2.setImageResource(R.drawable.img_pot_sweet_basil);
                                                } else if (typeOfPlant.equals("Thyme")) {
                                                    gdnPot2.setBackground(getActivity().getDrawable(R.drawable.img_pot_thyme));
                                                    gdnImgPot2.setImageResource(R.drawable.img_pot_thyme);
                                                } else if (typeOfPlant.equals("Oregano")) {
                                                    gdnPot2.setBackground(getActivity().getDrawable(R.drawable.img_pot_oregano));
                                                    gdnImgPot2.setImageResource(R.drawable.img_pot_oregano);
                                                }
                                                gdnTxtPot2.setText(getString(R.string.gdn_pot_planted1) + typeOfPlant + getString(R.string.gdn_pot_planted2) +  "2");
                                                gdnLnrPot2.setVisibility(View.VISIBLE);
                                            }
                                            else{
                                                gdnPot2.setBackground(getActivity().getDrawable(R.drawable.img_pot_empty));
                                                gdnLnrPot2.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                case 3:
                                    final DatabaseReference potReference3 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("Pot 3");
                                    potReference3.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //display type of plant
                                            if (dataSnapshot.hasChild("type_of_plant")) {
                                                potEmptyCounter++;
                                                String typeOfPlant = (dataSnapshot.child("type_of_plant").getValue().toString());
                                                if (typeOfPlant.equals("Sweet Basil")) {
                                                    gdnPot3.setBackground(getActivity().getDrawable(R.drawable.img_pot_sweet_basil));
                                                    gdnImgPot3.setImageResource(R.drawable.img_pot_sweet_basil);
                                                } else if (typeOfPlant.equals("Thyme")) {
                                                    gdnPot3.setBackground(getActivity().getDrawable(R.drawable.img_pot_thyme));
                                                    gdnImgPot3.setImageResource(R.drawable.img_pot_thyme);
                                                } else if (typeOfPlant.equals("Oregano")) {
                                                    gdnPot3.setBackground(getActivity().getDrawable(R.drawable.img_pot_oregano));
                                                    gdnImgPot3.setImageResource(R.drawable.img_pot_oregano);
                                                }
                                                gdnTxtPot3.setText(getString(R.string.gdn_pot_planted1) + typeOfPlant + getString(R.string.gdn_pot_planted2) +  "3");
                                                gdnLnrPot3.setVisibility(View.VISIBLE);
                                            }
                                            else{
                                                gdnPot3.setBackground(getActivity().getDrawable(R.drawable.img_pot_empty));
                                                gdnLnrPot3.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                case 4:
                                    final DatabaseReference potReference4 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("Pot 4");
                                    potReference4.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //display type of plant
                                            if (dataSnapshot.hasChild("type_of_plant")) {
                                                potEmptyCounter++;
                                                String typeOfPlant = (dataSnapshot.child("type_of_plant").getValue().toString());
                                                if (typeOfPlant.equals("Sweet Basil")) {
                                                    gdnPot4.setBackground(getActivity().getDrawable(R.drawable.img_pot_sweet_basil));
                                                    gdnImgPot4.setImageResource(R.drawable.img_pot_sweet_basil);
                                                } else if (typeOfPlant.equals("Thyme")) {
                                                    gdnPot4.setBackground(getActivity().getDrawable(R.drawable.img_pot_thyme));
                                                    gdnImgPot4.setImageResource(R.drawable.img_pot_thyme);
                                                } else if (typeOfPlant.equals("Oregano")) {
                                                    gdnPot4.setBackground(getActivity().getDrawable(R.drawable.img_pot_oregano));
                                                    gdnImgPot4.setImageResource(R.drawable.img_pot_oregano);
                                                }
                                                gdnTxtPot4.setText(getString(R.string.gdn_pot_planted1) + typeOfPlant + getString(R.string.gdn_pot_planted2) + "4");
                                                gdnLnrPot4.setVisibility(View.VISIBLE);
                                            }
                                            else{
                                                gdnPot4.setBackground(getActivity().getDrawable(R.drawable.img_pot_empty));
                                                gdnLnrPot4.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                case 5:
                                    final DatabaseReference potReference5 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("Pot 5");
                                    potReference5.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //display type of plant
                                            if (dataSnapshot.hasChild("type_of_plant")) {
                                                potEmptyCounter++;
                                                String typeOfPlant = (dataSnapshot.child("type_of_plant").getValue().toString());
                                                if (typeOfPlant.equals("Sweet Basil")) {
                                                    gdnPot5.setBackground(getActivity().getDrawable(R.drawable.img_pot_sweet_basil));
                                                    gdnImgPot5.setImageResource(R.drawable.img_pot_sweet_basil);
                                                } else if (typeOfPlant.equals("Thyme")) {
                                                    gdnPot5.setBackground(getActivity().getDrawable(R.drawable.img_pot_thyme));
                                                    gdnImgPot5.setImageResource(R.drawable.img_pot_thyme);
                                                } else if (typeOfPlant.equals("Oregano")) {
                                                    gdnPot5.setBackground(getActivity().getDrawable(R.drawable.img_pot_oregano));
                                                    gdnImgPot5.setImageResource(R.drawable.img_pot_oregano);
                                                }
                                                gdnTxtPot5.setText(getString(R.string.gdn_pot_planted1) + typeOfPlant + getString(R.string.gdn_pot_planted2) +  "5");
                                                gdnLnrPot5.setVisibility(View.VISIBLE);
                                            }
                                            else{
                                                gdnPot5.setBackground(getActivity().getDrawable(R.drawable.img_pot_empty));
                                                gdnLnrPot5.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                case 6:
                                    final DatabaseReference potReference6 = firebaseDatabase.getReference().child("Pot").child(gardenID).child("Pot 6");
                                    potReference6.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //display type of plant
                                            if (dataSnapshot.hasChild("type_of_plant")) {
                                                potEmptyCounter++;
                                                String typeOfPlant = (dataSnapshot.child("type_of_plant").getValue().toString());
                                                if (typeOfPlant.equals("Sweet Basil")) {
                                                    gdnPot6.setBackground(getActivity().getDrawable(R.drawable.img_pot_sweet_basil));
                                                    gdnImgPot6.setImageResource(R.drawable.img_pot_sweet_basil);
                                                } else if (typeOfPlant.equals("Thyme")) {
                                                    gdnPot6.setBackground(getActivity().getDrawable(R.drawable.img_pot_thyme));
                                                    gdnImgPot6.setImageResource(R.drawable.img_pot_thyme);
                                                } else if (typeOfPlant.equals("Oregano")) {
                                                    gdnPot6.setBackground(getActivity().getDrawable(R.drawable.img_pot_oregano));
                                                    gdnImgPot6.setImageResource(R.drawable.img_pot_oregano);
                                                }
                                                gdnTxtPot6.setText(getString(R.string.gdn_pot_planted1) + typeOfPlant + getString(R.string.gdn_pot_planted2) +  "6");
                                                gdnLnrPot6.setVisibility(View.VISIBLE);
                                            }
                                            else{
                                                gdnPot6.setBackground(getActivity().getDrawable(R.drawable.img_pot_empty));
                                                gdnLnrPot6.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                            }
                            if(gdnLnrPot1.getVisibility() == View.VISIBLE && gdnLnrPot2.getVisibility() == View.VISIBLE && gdnLnrPot3.getVisibility() == View.VISIBLE
                                    && gdnLnrPot4.getVisibility() == View.VISIBLE && gdnLnrPot5.getVisibility() == View.VISIBLE && gdnLnrPot6.getVisibility() == View.VISIBLE){
                                gdnLnrEmpty.setVisibility(View.GONE);
                            }
                            else{
                                gdnLnrEmpty.setVisibility(View.VISIBLE);
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


    @Override
    public void onClick(View v) {
        if (v == gdnPot1) {
            if(gdnLnrPot1.getVisibility() != View.VISIBLE){
                DialogPickHerbs dialogPickHerbs = new DialogPickHerbs();
                dialogPickHerbs.potPosition = "Pot 1";
                dialogPickHerbs.gardenID = gardenID;
                dialogPickHerbs.show(getFragmentManager(), "Pick Herbs");
            }
            else{
                HerbFragment fragment = new HerbFragment();
                fragment.potPosition = "Pot 1";
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (v == gdnPot2) {
            if(gdnLnrPot2.getVisibility() != View.VISIBLE){
                DialogPickHerbs dialogPickHerbs = new DialogPickHerbs();
                dialogPickHerbs.potPosition = "Pot 2";
                dialogPickHerbs.gardenID = gardenID;
                dialogPickHerbs.show(getFragmentManager(), "Pick Herbs");
            }
            else{
                HerbFragment fragment = new HerbFragment();
                fragment.potPosition = "Pot 2";
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (v == gdnPot3) {
            if(gdnLnrPot3.getVisibility() != View.VISIBLE){
                DialogPickHerbs dialogPickHerbs = new DialogPickHerbs();
                dialogPickHerbs.potPosition = "Pot 3";
                dialogPickHerbs.gardenID = gardenID;
                dialogPickHerbs.show(getFragmentManager(), "Pick Herbs");
            }
            else{
                HerbFragment fragment = new HerbFragment();
                fragment.potPosition = "Pot 3";
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (v == gdnPot4) {
            if(gdnLnrPot4.getVisibility() != View.VISIBLE){
                DialogPickHerbs dialogPickHerbs = new DialogPickHerbs();
                dialogPickHerbs.potPosition = "Pot 4";
                dialogPickHerbs.gardenID = gardenID;
                dialogPickHerbs.show(getFragmentManager(), "Pick Herbs");
            }
            else{
                HerbFragment fragment = new HerbFragment();
                fragment.potPosition = "Pot 4";
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (v == gdnPot5) {
            if(gdnLnrPot5.getVisibility() != View.VISIBLE){
                DialogPickHerbs dialogPickHerbs = new DialogPickHerbs();
                dialogPickHerbs.potPosition = "Pot 5";
                dialogPickHerbs.gardenID = gardenID;
                dialogPickHerbs.show(getFragmentManager(), "Pick Herbs");
            }
            else{
                HerbFragment fragment = new HerbFragment();
                fragment.potPosition = "Pot 5";
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (v == gdnPot6) {
            if(gdnLnrPot6.getVisibility() != View.VISIBLE){
                DialogPickHerbs dialogPickHerbs = new DialogPickHerbs();
                dialogPickHerbs.potPosition = "Pot 6";
                dialogPickHerbs.gardenID = gardenID;
                dialogPickHerbs.show(getFragmentManager(), "Pick Herbs");
            }
            else{
                HerbFragment fragment = new HerbFragment();
                fragment.potPosition = "Pot 6";
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }
}
