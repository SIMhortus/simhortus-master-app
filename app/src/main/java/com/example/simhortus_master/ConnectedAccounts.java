package com.example.simhortus_master;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import static android.graphics.BlendMode.COLOR;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static com.example.simhortus_master.R.color.*;

public class ConnectedAccounts extends AppCompatActivity  implements View.OnClickListener{

    ImageButton imageButton;
    public static TextView textView, passID;
    LinearLayout linearLayout;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_accounts);

        imageButton = findViewById(R.id.btnBack);
        textView = findViewById(R.id.toolbarText);
         linearLayout = findViewById(R.id.accountLayout);

        textView.setText("Connected Accounts");
        imageButton.setOnClickListener(this);

        passID = findViewById(R.id.passID);



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("Users");

        final String uID = firebaseUser.getUid();


        if (firebaseUser != null) {

            final DatabaseReference getID = reference.child(uID).child("garden_id");

            getID.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String id = dataSnapshot.getValue().toString();
                    textView.setText("Device ID: "+id);
                    passID.setText(id);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        String gIG = passID.getText().toString();
        DatabaseReference gardenRef = firebaseDatabase.getReference("Garden");
        gardenRef.orderByChild(gIG).equalTo("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Global.showToast(dataSnapshot.getValue().toString(), ConnectedAccounts.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Global.showToast(uID, ConnectedAccounts.this);



//        TextView displayName = new TextView(ConnectedAccounts.this);
//        TextView displayEmail = new TextView(ConnectedAccounts.this);


//        displayEmail.setText("johndoe@gmail.com");
////        linearLayout.addView(txt1);
////        linearLayout.addView(txt);
//
//        LinearLayout layoutForGrid = new LinearLayout(this);
//
//        layoutForGrid.setOrientation(LinearLayout.VERTICAL);
//        layoutForGrid.addView(displayName);
//        layoutForGrid.addView(displayEmail);



//        AppCompatButton btnRemove = new AppCompatButton(this);
//        btnRemove.setText("remove");
//        btnRemove.setBackgroundColor(getResources().getColor(transparent));
//        btnRemove.setTextColor(getResources().getColor(colorPrimary));
//        btnRemove.setAllCaps(false);
//
//        GridLayout gridLayout1 = new GridLayout(this);
//
//        gridLayout1.setOrientation(GridLayout.HORIZONTAL);
//
//        int paddingDp = 20;
//        float density = getApplication().getResources().getDisplayMetrics().density;
//        int paddingPixel = (int)(paddingDp * density);
//
//        linearLayout.addView(gridLayout1);
//        gridLayout1.setPadding(paddingPixel,0,paddingPixel,0);
//
//
//
//        String[] textArray = {"Roselle Tabuena", "Two", "Three", "Four"};
//        final String[] textArrayE = {"roselle@gmail.com", "Two", "Three", "Four"};
//
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//
//        TextView displayName  =new TextView(this);
//        displayName.setTextSize(Global.pxFromDp(6, this));
//        displayName.setTextColor(getResources().getColor(greyDarker));
//
//        for( int i = 0; i < textArray.length; i++ )
//        {
//            TextView text = new TextView(this);
//
//            displayName.setText(textArray[i]);
//
//
//            displayName.setText(textArray[i]);
//            text.setText(textArrayE[i]);
//
//
//            layoutForGrid.addView(textView);
//            layoutForGrid.addView(text);
//
//        }
//
//        gridLayout1.addView(layoutForGrid);

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnResetPass:


                break;
            case R.id.btnBack:

                onBackPressed();
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//                startActivity(new Intent(this, MainActivity.class));


                break;

        }
    }
}
