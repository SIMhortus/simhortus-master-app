package com.example.simhortus_master;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView ScannerView;
    private FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 1011);
    }

    public void onDestroy() {
        super.onDestroy();

        ScannerView = null;
        ScannerView.stopCamera();

    }

    @Override
    public void handleResult(Result result) {
        final String dID = result.getText();
        final String uID = mAuth.getUid();

        final DatabaseReference rootRef = firebaseDatabase.getReference("Garden");
        final DatabaseReference userRef = firebaseDatabase.getReference("UserID");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(dID)) {

                    rootRef.child(dID).child("userID").setValue(uID);
                    userRef.child(uID).child("deviceID").setValue(dID);

                    Toast.makeText(ScanCodeActivity.this, "Device connected!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ScanCodeActivity.this, MainActivity.class));

                }  else {
                    Toast.makeText(ScanCodeActivity.this, "haysssssss" + dID, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();

        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
    @Override
    protected void onPause() {
        super.onPause();

        ScannerView.stopCamera();
    }



}
