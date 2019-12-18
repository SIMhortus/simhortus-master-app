package com.example.simhortus_master;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
        this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 1011);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();


    }

    public void onDestroy() {
        super.onDestroy();
        ScannerView = null;
    }

    @Override
    public void handleResult(Result result) {
        final String dID = result.getText();

        final FirebaseUser user = mAuth.getCurrentUser();
        final String uID = user.getUid();

        Global.gardenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChild(dID)) {
                    long num = snapshot.child(dID).child("users").getChildrenCount();
                    if (num == 0) {
                        Global.showToast("You are the first to scan this device so you will be automatically admin", ScanCodeActivity.this);
                        addDevice(uID, "owner", dID);
                        Global.getRef.child(uID).child("userType_deviceID_pending").setValue("owner_"+dID);
                        finish();
                    }  else if (num < 6) {
                        final Query getOwnerKey =  Global.getRef.orderByChild("userType_deviceID_pending").equalTo("owner_"+dID);

                        Global.showToast("You not the first to scan this garden so you will automatically be user", ScanCodeActivity.this);
                        addDevice(uID, "user", dID);
                        Global.getRef.child(uID).child("pending").setValue(true);
                        Global.getRef.child(uID).child("userType_deviceID_pending").setValue("user_"+dID+"_true");


                        getOwnerKey.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                Log.i("Value", child.getKey());

                                final String ownerID = child.getKey().toString();

                                Global.getRef.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       final String fName = dataSnapshot.child("first_name").getValue().toString();
                                       final String lName = dataSnapshot.child("last_name").getValue().toString();
                                       final String full_name = fName +" "+ lName;
                                       final String desc = "sent you a request";


                                        NotificationInfo notificationInfo = new NotificationInfo(
                                                Global.currentDateTimeString,
                                                desc,
                                                full_name,
                                                1,
                                                false
                                        );

                                    Global.notifRef.child(ownerID).child(Global.currentDateTimeString).setValue(notificationInfo).addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Global.showToast("Hello", ScanCodeActivity.this);
                                                        finish();
                                                    }
                                                }
                                            }
                                    );

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

//                                onBackPressed();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Value", databaseError.toString());
                        }
                    });

                    } else {
                        Global.showToast("Maximum account reached!", ScanCodeActivity.this);
                    }

                }  else {
                    Toast.makeText(ScanCodeActivity.this, "The scanned qr code is not registered in the database", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void addDevice(String uID, String userType, String deviceID) {
        Global.getRef.child(uID).child("garden_id").setValue(deviceID);
        Global.getRef.child(uID).child("user_type").setValue(userType);

        Global.gardenRef.child(deviceID).child("users").child(uID).setValue(userType);
        startActivity(new Intent(ScanCodeActivity.this, MainActivity.class));
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
