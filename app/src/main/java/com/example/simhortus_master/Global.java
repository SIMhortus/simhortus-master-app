package com.example.simhortus_master;

import android.content.Context;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class Global {

    public static FirebaseAuth mAuth;

    public static FirebaseDatabase ref = FirebaseDatabase.getInstance();

    public static FirebaseAuth mAuthInstance = mAuth.getInstance();

    public static FirebaseUser getmAuth = mAuthInstance.getCurrentUser();

    public static DatabaseReference getRef = ref.getReference("Users");

    public static DatabaseReference gardenRef = ref.getReference("Garden");

    public static DatabaseReference potRef = ref.getReference("Pot");

    public static DatabaseReference herbsRef = ref.getReference("Herbs");

    public static DatabaseReference notifRef = ref.getReference("notification");

    public static String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());




    public static void showToast(String msg, Context ctx) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
    public static float pxFromDp(float dp, Context mContext) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }


}
