package com.example.simhortus_master;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView ScannerView;
    private FirebaseDatabase mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

        mAuth = FirebaseDatabase.getInstance();

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

        DatabaseReference rootRef = mAuth.getReference("Garden");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(dID)) {
                    // run some code
                    Toast.makeText(ScanCodeActivity.this, "sana ol gumagana", Toast.LENGTH_LONG).show();
                }  else {
                    Toast.makeText(ScanCodeActivity.this, "haysssssss", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //ScanGarden.resultTextView.setText(result.getText());
        // put your code here



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
