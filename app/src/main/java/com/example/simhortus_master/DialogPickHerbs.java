package com.example.simhortus_master;

import android.animation.ArgbEvaluator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DialogPickHerbs extends AppCompatDialogFragment implements View.OnClickListener {

    ImageButton imgClose;
    String[] herbImage = new String[500];
    String[] herbName = new String[40];
    Integer herbCount = 0, selectionCount = 0;
    AppCompatImageButton pckImgBtnHerb;
    TextView pckTxtHerb;
    MaterialButton pckBtnPick;
    AppCompatButton pckBtnBack, pckBtnNext;

    public String potPosition, gardenID;
    public FirebaseDatabase firebaseDatabase;

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pick_herbs, null);
        builder.setView(view);

        imgClose = view.findViewById(R.id.close);
        pckImgBtnHerb = view.findViewById(R.id.pck_img_btn_herb);
        pckTxtHerb = view.findViewById(R.id.pck_txt_herb);
        pckBtnPick = view.findViewById(R.id.pck_btn_pick);
        pckBtnBack = view.findViewById(R.id.pck_btn_back);
        pckBtnNext = view.findViewById(R.id.pck_btn_next);
        pckBtnPick.setOnClickListener(this);
        pckBtnBack.setOnClickListener(this);
        pckBtnNext.setOnClickListener(this);
        pckBtnBack.setEnabled(false);
        pckBtnNext.setEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference("Herbs");

        //getting and displaying herbs
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //check if pot is empty
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    herbImage[herbCount] = snapshot.child("image").getValue().toString();
                    herbName[herbCount] = snapshot.getKey();
                    herbCount++;
                }

                Glide.with(getActivity()).load(herbImage[0]).into(pckImgBtnHerb);
                pckTxtHerb.setText(herbName[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        pckBtnBack.setEnabled(false);
        pckBtnBack.setTextColor(getResources().getColor(R.color.greyLight));
        pckBtnNext.setEnabled(true);
        pckBtnNext.setTextColor(getResources().getColor(R.color.colorPrimary));

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if(v == pckBtnNext){
            selectionCount++;
            setCount();
            Glide.with(getActivity()).load(herbImage[selectionCount]).into(pckImgBtnHerb);
            pckTxtHerb.setText(herbName[selectionCount]);
        }
        else if(v == pckBtnBack){
            selectionCount--;
            setCount();
            Glide.with(getActivity()).load(herbImage[selectionCount]).into(pckImgBtnHerb);
            pckTxtHerb.setText(herbName[selectionCount]);
        }
        else if(v == imgClose){
            dismiss();
        }
        else if(v == pckBtnPick){
            pickHerbs();
        }
    }

    private void setCount(){
        if(selectionCount == 0){
            pckBtnBack.setEnabled(false);
            pckBtnBack.setTextColor(getResources().getColor(R.color.greyLight));
            pckBtnNext.setEnabled(true);
            pckBtnNext.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else if(selectionCount+1 == herbCount){
            pckBtnBack.setEnabled(true);
            pckBtnBack.setTextColor(getResources().getColor(R.color.colorPrimary));
            pckBtnNext.setEnabled(false);
            pckBtnNext.setTextColor(getResources().getColor(R.color.greyLight));
        }
        else{
            pckBtnBack.setEnabled(true);
            pckBtnBack.setTextColor(getResources().getColor(R.color.colorPrimary));
            pckBtnNext.setEnabled(true);
            pckBtnNext.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    };

    private void pickHerbs(){
        DialogPlantingInstructions dialogPlantingInstructions = new DialogPlantingInstructions();
        dialogPlantingInstructions.herbName = herbName[selectionCount];
        dialogPlantingInstructions.potPosition = potPosition;
        dialogPlantingInstructions.gardenID = gardenID;
        dialogPlantingInstructions.show(getFragmentManager(), "Planting Instructions");
        dismiss();
    };
}
