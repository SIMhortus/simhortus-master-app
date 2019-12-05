package com.example.simhortus_master;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DialogAddPlant extends AppCompatDialogFragment implements View.OnClickListener{

    Button button;
    TextView addHerb;
    AppCompatButton addBtnCancel, addBtnProceed;
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
        View view = inflater.inflate(R.layout.dialog_add_plant, null);

        builder.setView(view);

        addBtnCancel = view.findViewById(R.id.add_btn_cancel);
        addBtnProceed = view.findViewById(R.id.add_btn_proceed);
        addHerb = view.findViewById(R.id.add_herb);
        addBtnCancel.setOnClickListener(this);
        addBtnProceed.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        //getting userID
        if (user != null) {
            uid = user.getUid();
        }

        addHerb.setText("Add " + herbName + "?");


        firebaseDatabase = FirebaseDatabase.getInstance();
        potReference = firebaseDatabase.getReference().child("Pot").child(gardenID);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if(v == addBtnProceed){
            addProceed();
        }
        else if(v == addBtnCancel){
            dismiss();
        }
    }

    private void addProceed(){
        potReference.child(potPosition).child("type_of_plant").setValue(herbName);
        potReference.child(potPosition).child("days").setValue(0);
        potReference.child(potPosition).child("owner").setValue(uid);
        //getdate
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c);

        potReference.child(potPosition).child("status").child("height").child(formattedDate).setValue(0);
        potReference.child(potPosition).child("status").child("soil_moisture").setValue(0);
        HerbFragment fragment = new HerbFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        dismiss();
    };
}
