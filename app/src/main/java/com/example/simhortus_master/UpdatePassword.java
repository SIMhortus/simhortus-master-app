package com.example.simhortus_master;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class UpdatePassword extends AppCompatDialogFragment {
    private EditText edtCurrentPassword, edtNewPassword, edtConfirmNewPassword;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_password, null);

        builder.setView(view).setTitle("Change Password").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        edtCurrentPassword = view.findViewById(R.id.currentPassword);
        edtNewPassword = view.findViewById(R.id.newPassword);
        edtConfirmNewPassword = view.findViewById(R.id.confirmNewPassword);

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
                    String currentPassword = edtCurrentPassword.getText().toString();
                    final String newPassword = edtNewPassword.getText().toString();
                    String confirmNewPassword = edtConfirmNewPassword.getText().toString();
                    if(TextUtils.isEmpty(edtCurrentPassword.getText()))
                    {
                        edtCurrentPassword.setError( "Current password is required!" );
                    }
                    else if(TextUtils.isEmpty(edtNewPassword.getText()))
                    {
                        edtNewPassword.setError( "New password is required!" );
                    }
                    else if(TextUtils.isEmpty(edtConfirmNewPassword.getText()))
                    {
                        edtConfirmNewPassword.setError( "Please confirm new password!" );
                    }
                    else{
                        if(!(newPassword.equals(confirmNewPassword))){
                            Toast.makeText(getActivity(), "New password do not match!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                // Get auth credentials from the user for re-authentication. The example below shows
                                // email and password credentials but there are multiple possible providers,
                                // such as GoogleAuthProvider or FacebookAuthProvider.
                                AuthCredential credential = EmailAuthProvider
                                        .getCredential(user.getEmail(), currentPassword);

                                // Prompt the user to re-provide their sign-in credentials
                                user.reauthenticate(credential)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    user.updatePassword(newPassword)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(getActivity(), "Password is updated!", Toast.LENGTH_SHORT).show();
                                                                        dismiss();
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
                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
    }
}
