package com.example.simhortus_master;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ScanGarden extends AppCompatActivity {
    public static EditText resultTextView;
    Button scan_btn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_garden);

        scan_btn = findViewById(R.id.btn_scan);
        mAuth = FirebaseAuth.getInstance();

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),  ScanCodeActivity.class);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ScanGarden.this, MainActivity.class));
    }

    protected void onStart() {
        super.onStart();

        final FirebaseUser user =  mAuth.getInstance().getCurrentUser();

        if (user == null) {
            startActivity(new Intent(ScanGarden.this, Login.class));
        } else {
            replaceLayout();
        }
    }

    public void replaceLayout() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String uID = firebaseUser.getUid();

        DatabaseReference databaseReference = Global.ref.getReference("Users").child(uID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("garden_id")) {
                    startActivity(new Intent(ScanGarden.this, MainActivity.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


}