package com.example.simhortus_master;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class DialogRemovePlant extends AppCompatDialogFragment implements View.OnClickListener{

    Button button;
    TextView addHerb;
    AppCompatButton removeBtnCancel, removeBtnProceed;
    public String potPosition;
    public String herbName;
    public String gardenID;
    public String uid;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseAuth mAuth;
    DatabaseReference potReference;

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_remove_plant, null);

        builder.setView(view);

        removeBtnCancel = view.findViewById(R.id.remove_btn_cancel);
        removeBtnProceed = view.findViewById(R.id.remove_btn_proceed);
        addHerb = view.findViewById(R.id.remove_herb);
        removeBtnCancel.setOnClickListener(this);
        removeBtnProceed.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        //getting userID
        if (user != null) {
            uid = user.getUid();
        }

        addHerb.setText("Remove " + herbName + "?");


        firebaseDatabase = FirebaseDatabase.getInstance();
        potReference = firebaseDatabase.getReference().child("Pot").child(gardenID);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if(v == removeBtnProceed){
            addProceed();
        }
        else if(v == removeBtnCancel){
            dismiss();
        }
    }

    private void addProceed(){
        final DatabaseReference reference = firebaseDatabase.getReference("Users");
        //getting garden ID
        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting garden ID
                gardenID = dataSnapshot.child("garden_id").getValue().toString();
                firebaseDatabase.getReference().child("Pot").child(gardenID).child(potPosition).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dismiss();
    };
}
