package com.example.simhortus_master;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MenuFragment extends Fragment {


    Button btnLogout, btnDispName, btnContact, btnEmail, btnPass, btnLinked;
    TextView txtEmail, txtDispName, txtPhone;
    String uid;

    GridLayout layoutLink, layoutUnlink;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        btnLogout = rootView.findViewById(R.id.btnMenuLogOut);
        btnDispName = rootView.findViewById(R.id.btn_disp_name);
        btnContact = rootView.findViewById(R.id.btnContact);
        btnEmail = rootView.findViewById(R.id.btnEmail);
        btnPass = rootView.findViewById(R.id.btnPass);
        btnLinked = rootView.findViewById(R.id.btnLinkedDevice);

        txtEmail = rootView.findViewById(R.id.txtEmail);
        txtDispName = rootView.findViewById(R.id.txtDispName);
        txtPhone = rootView.findViewById(R.id.txtPhone);

        layoutLink = rootView.findViewById(R.id.layoutLink);
        layoutUnlink = rootView.findViewById(R.id.layoutUnlink);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference getRef = firebaseDatabase.getReference("Users");

        //getting email
        if (user != null) {
            String email = user.getEmail();
            txtEmail.setText(email);
            uid = user.getUid();
        }




        //getting data
        getRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String firstName = dataSnapshot.child("first_name").getValue(String.class);
                String lastName = dataSnapshot.child("last_name").getValue(String.class);
                String phone = "0" + dataSnapshot.child("phone_number").getValue(String.class).substring(3);

                txtDispName.setText(firstName + " " + lastName);

                if(phone.equals("")){
                    txtPhone.setText("No phone number");
                    btnContact.setText("Add");
                }
                else{
                    txtPhone.setText(phone);
                    btnContact.setText("Edit");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //repeated
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), Login.class));
            }
        });

        btnDispName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDisplayName();
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePass();
            }
        });

        btnLinked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToScanActivity();
            }
        });

        return rootView;
    }

    public void updateDisplayName() {
        UpdateDisplayName updateDisplayName = new UpdateDisplayName();
        updateDisplayName.show(getFragmentManager(), "Update display name");
    }

    public void updateContact() {
        UpdateContact updateContact = new UpdateContact();
        updateContact.show(getFragmentManager(), "Update contact");
    }

    public void updateEmail() {
        UpdateEmail updateEmail = new UpdateEmail();
        updateEmail.show(getFragmentManager(), "Update email");
    }

    public void updatePass() {
        UpdatePassword updatePassword = new UpdatePassword();
        updatePassword.show(getFragmentManager(), "Update password");
    }

    public void goToScanActivity(){
        startActivity(new Intent(getContext(), ScanGarden.class));
    }


    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference getRef = firebaseDatabase.getReference("Users");

        getRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("deviceID")){
                    layoutLink.setVisibility(View.GONE);
                    layoutUnlink.setVisibility(View.VISIBLE);
                } else {
                    layoutLink.setVisibility(View.VISIBLE);
                    layoutUnlink.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
