package com.example.simhortus_master;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Global {

    public static FirebaseAuth mAuth;
    public static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static FirebaseDatabase ref = FirebaseDatabase.getInstance();
    public static FirebaseAuth mAuthInstance = mAuth.getInstance();
    public static FirebaseUser getmAuth = mAuthInstance.getCurrentUser();
    public static DatabaseReference getRef = ref.getReference("Users");


    public static void showToast(String msg, Context ctx) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }


}
