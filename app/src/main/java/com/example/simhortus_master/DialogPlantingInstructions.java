package com.example.simhortus_master;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

public class DialogPlantingInstructions extends AppCompatDialogFragment implements View.OnClickListener{

    ImageButton imgClose;
    Button button;
    ImageView plnImage;
    CheckBox plnChkDone;
    Resources res;
    String[] plnHeadingArr;
    String[] plnDescArr;
    String[] plnThingsArr;
    TextView plnHerb, plnHeading, plnDesc;
    AppCompatButton plnBtnBack, plnBtnNext;
    Integer dataCount = 0, selectionCount = 0;
    public String potPosition;
    public String herbName;
    public String gardenID;

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_planting_instructions, null);

        res = view.getContext().getResources();

        builder.setView(view);

        imgClose = view.findViewById(R.id.close);
        plnBtnBack = view.findViewById(R.id.pln_btn_back);
        plnBtnNext = view.findViewById(R.id.pln_btn_next);
        plnChkDone = view.findViewById(R.id.pln_chk_done);
        plnHerb = view.findViewById(R.id.pln_herb);
        plnImage = view.findViewById(R.id.pln_image);
        plnHeading = view.findViewById(R.id.pln_heading);
        plnDesc = view.findViewById(R.id.pln_desc);
        plnHeadingArr = res.getStringArray(R.array.pln_string_heading);
        plnDescArr = res.getStringArray(R.array.pln_string_desc);
        plnThingsArr = res.getStringArray(R.array.pln_string_things);
        plnBtnBack.setOnClickListener(this);
        plnBtnNext.setOnClickListener(this);
        plnChkDone.setOnClickListener(this);

        plnHerb.setText("Planting " + herbName);

        for(int i=0;i<plnHeadingArr.length;i++){
            dataCount++;
        }
//
        plnHeading.setText(plnHeadingArr[0]);
        plnDesc.setText(plnDescArr[0]);
        plnBtnBack.setEnabled(false);
        plnBtnBack.setTextColor(getResources().getColor(R.color.greyLight));
        plnBtnNext.setEnabled(false);
        plnBtnNext.setTextColor(getResources().getColor(R.color.greyLight));

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if(v == plnBtnNext){
            if(plnBtnNext.getText().equals("next")) {
                selectionCount++;
                plnHeading.setText(plnHeadingArr[selectionCount]);
                plnDesc.setText(plnDescArr[selectionCount]);
                setCount();
                plnChkDone.setChecked(false);
                plnBtnNext.setEnabled(false);
                plnBtnNext.setTextColor(getResources().getColor(R.color.greyLight));
            }
            else{
                addHerbs();
            }
        }
        else if(v == plnBtnBack){
            selectionCount--;
            plnHeading.setText(plnHeadingArr[selectionCount]);
            plnDesc.setText(plnDescArr[selectionCount]);
            plnBtnNext.setText("next");
            setCount();
        }
        else if(v == imgClose){
            dismiss();
        }
        else if(v == plnChkDone){
            plnBtnNext.setEnabled(true);
            plnBtnNext.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void setCount(){
        if(selectionCount == 0){
            plnBtnBack.setEnabled(false);
            plnBtnBack.setTextColor(getResources().getColor(R.color.greyLight));
        }
        else if(selectionCount == dataCount-1){
            plnBtnBack.setEnabled(true);
            plnBtnBack.setTextColor(getResources().getColor(R.color.colorPrimary));
            plnBtnNext.setEnabled(false);
            plnBtnNext.setTextColor(getResources().getColor(R.color.greyLight));
            plnBtnNext.setText("FINISH");
        }
        else{
            plnBtnBack.setEnabled(true);
            plnBtnBack.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    };

    private void addHerbs(){
        DialogAddPlant dialogAddPlant = new DialogAddPlant();
        dialogAddPlant.herbName = herbName;
        dialogAddPlant.potPosition = potPosition;
        dialogAddPlant.gardenID = gardenID;
        dialogAddPlant.show(getFragmentManager(), "Planting Instructions");
        dismiss();
    };
}
