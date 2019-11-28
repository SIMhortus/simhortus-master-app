package com.example.simhortus_master;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateDisplayName extends AppCompatDialogFragment {

    public EditText edtNewFirstName, edtNewLastName;

    public String uid, firstName, lastName;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;

    public UpdateDisplayName() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_display_name, null);

        builder.setView(view).setTitle("Update Display Name")

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })

                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        edtNewFirstName = view.findViewById(R.id.newFirstName);
        edtNewLastName = view.findViewById(R.id.newLastName);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //getting userID
        if (mAuth.getCurrentUser() != null) {
            uid = mAuth.getCurrentUser().getUid();
        }

        //getting firstname and lastname

        Global.getRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                firstName = dataSnapshot.child("first_name").getValue(String.class);
                lastName = dataSnapshot.child("last_name").getValue(String.class);
                edtNewFirstName.setText(firstName);
                edtNewLastName.setText(lastName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return builder.create();
    }

    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean wantToCloseDialog = false;
                    //Do stuff, possibly set wantToCloseDialog to true then...
                    String newFirstName = edtNewFirstName.getText().toString();
                    String newLastName = edtNewLastName.getText().toString();

                    if(TextUtils.isEmpty(edtNewFirstName.getText()))
                    {
                        edtNewFirstName.setError( "First Name is required!" );
                    }
                    else if(TextUtils.isEmpty(edtNewLastName.getText()))
                    {
                        edtNewLastName.setError( "Last Name is required!" );
                    }
                    else{
                        if(newFirstName.equals(firstName) && newLastName.equals(lastName)){
                            Toast.makeText(getActivity(), "Current and new name are the same!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            try {

                                UserInfo userInfo = new UserInfo(
                                        newFirstName,
                                        newLastName
                                );

                                firebaseDatabase.getReference("Users").child(uid).setValue(userInfo);

                                Toast.makeText(getActivity(), "Display name is updated.", Toast.LENGTH_LONG).show();

                                dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            });
        }
    }

}


