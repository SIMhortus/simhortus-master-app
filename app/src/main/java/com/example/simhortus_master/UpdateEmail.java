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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateEmail extends AppCompatDialogFragment {

    private EditText edtNewEmail, edtEmailPassword;
    String email;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_email, null);

        builder.setView(view).setTitle("Update Email")

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

        edtNewEmail = view.findViewById(R.id.newEmail);
        edtEmailPassword = view.findViewById(R.id.emailPassword);

        //getting email
        if (Global.getmAuth != null) {
            email = Global.getmAuth.getEmail();
            edtNewEmail.setText(email);
        }

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
                    String emailPassword = edtEmailPassword.getText().toString();
                    final String newEmail = edtNewEmail.getText().toString();
                    if(TextUtils.isEmpty(edtEmailPassword.getText()))
                    {
                        edtEmailPassword.setError( "Password is required!" );
                    }
                    else if(TextUtils.isEmpty(edtNewEmail.getText()))
                    {
                        edtNewEmail.setError( "New email is required!" );
                    }
                    else if(newEmail.equals(email)){
                        Toast.makeText(getActivity(), "Current and new email are the same!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        // Get auth credentials from the user for re-authentication. The example below shows
                        // email and password credentials but there are multiple possible providers,
                        // such as GoogleAuthProvider or FacebookAuthProvider.
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), emailPassword);

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            user.updateEmail(edtNewEmail.getText().toString().trim())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getActivity(), "Email address is updated.", Toast.LENGTH_LONG).show();
                                                                dismiss();
                                                            } else {
                                                                Toast.makeText(getActivity(), "Failed to update email!", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "Wrong password!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                }
            });
        }
    }
}
