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
import com.google.common.net.InternetDomainName;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class UpdateContact extends AppCompatDialogFragment {


    public EditText edtNewPhoneNumber, edtCodePhone;
    public Button btnVerificationCode, btnPhoneCancel, btnPhoneSubmit;

    public String uid, phoneNumber, codeSent;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

    public UpdateContact() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_contact, null);

        builder.setView(view).setTitle("Update Contact")

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })

                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {




            }
        });

        edtNewPhoneNumber = view.findViewById(R.id.newPhoneNumber);
        edtCodePhone = view.findViewById(R.id.codePhone);

        btnVerificationCode = view.findViewById(R.id.btnVerificationCode);
//        btnPhoneCancel = view.findViewById(R.id.btnPhoneCancel);
//        btnPhoneSubmit = view.findViewById(R.id.btnPhoneSubmit);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //getting userID
        if (mAuth.getCurrentUser() != null) {
            uid = mAuth.getCurrentUser().getUid();
        }

        //getting current phone number
        firebaseDatabase.getReference("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                phoneNumber = dataSnapshot.child("phone_number").getValue(String.class);
                if(phoneNumber.equals("")){
                    edtNewPhoneNumber.setText("");
                }
                else{
                    edtNewPhoneNumber.setText(phoneNumber.substring(3));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
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
                    String newPhoneNumber = edtNewPhoneNumber.getText().toString();
                    String verificationCode = edtCodePhone.getText().toString();

                    if(TextUtils.isEmpty(edtNewPhoneNumber.getText()))
                    {
                        edtNewPhoneNumber.setError( "Phone number is required!" );
                    }
                    else if(TextUtils.isEmpty(edtCodePhone.getText()))
                    {
                        edtCodePhone.setError( "Verification code is required!" );
                    }
                    else{
                        if(phoneNumber.equals("")){
                            try {
                                verifyCode();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            if(newPhoneNumber.equals(phoneNumber.substring(3))){
                                Toast.makeText(getActivity(), "Please enter a new phone number", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {
                                    verifyCode();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }
            });
        }
    }

    //sending code
    private void sendVerificationCode(){
        if(TextUtils.isEmpty(edtNewPhoneNumber.getText()))
        {
            edtNewPhoneNumber.setError( "Phone number is required!" );
        }
        else if (((isValid(edtNewPhoneNumber.getText().toString())) && (edtNewPhoneNumber.getText().toString().length() == 10))) {
            String newPhoneNumber = edtNewPhoneNumber.getText().toString();
            if(phoneNumber.equals("")){
                String sendPhoneNumber = "+63" + edtNewPhoneNumber.getText().toString().trim();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        sendPhoneNumber,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        getActivity(),               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks
            }
            else{
                if(newPhoneNumber.equals(phoneNumber.substring(3))){
                    Toast.makeText(getActivity(), "Please enter a new phone number", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Unlinking +63" + phoneNumber.substring(3), Toast.LENGTH_SHORT).show();
//                    reference.child(uid).child("phone_number").setValue("");
                    mAuth.getCurrentUser().unlink(PhoneAuthProvider.PROVIDER_ID);
                    String sendPhoneNumber = "+63" + edtNewPhoneNumber.getText().toString().trim();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            sendPhoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            getActivity(),               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        }
        else{
            edtNewPhoneNumber.setError( "Please enter a valid phone number" );
        }
    }

    //Submitting
    private void verifyCode(){
        String code = edtCodePhone.getText().toString();
        Toast.makeText(getActivity(), "Verifying", Toast.LENGTH_SHORT).show();
        PhoneAuthCredential phoneCredential = PhoneAuthProvider.getInstance().getCredential(codeSent, code);
//        Toast.makeText(getActivity(), "codeSent: " + codeSent, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), "code: " + code, Toast.LENGTH_SHORT).show();
        linkWithPhoneAuthCredential(phoneCredential);
    };

    //For mCallbacks
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(getActivity(), "Verifying", Toast.LENGTH_SHORT).show();
            linkWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getActivity(), "Verification failed due to: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            String sendPhoneNumber = "+63" + edtNewPhoneNumber.getText().toString().trim();
            Toast.makeText(getActivity(), "Sending code to " + sendPhoneNumber, Toast.LENGTH_SHORT).show();
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
//            String code = edtCodePhone.getText().toString();
//            Toast.makeText(getActivity(), "Verifying", Toast.LENGTH_SHORT).show();
//            PhoneAuthCredential phoneCredential = PhoneAuthProvider.getCredential(codeSent, code);
//            linkWithPhoneAuthCredential(phoneCredential);
        }
    };

    //updating phone
    private void linkWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getActivity(), "Linking +63" + edtNewPhoneNumber.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            String newPhoneNumber = "+63" + edtNewPhoneNumber.getText().toString();
                            reference.child(uid).child("phone_number").setValue(newPhoneNumber);
                            Toast.makeText(getActivity(), "Phone number is updated.", Toast.LENGTH_LONG).show();
                            dismiss();
                        }
                        else {
                            Toast.makeText(getActivity(), "Linking failed. Wrong verification code", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //For checking of number
    public static boolean isValid(String s)
    {
        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        Pattern p = Pattern.compile("(9)?[0-9]{9}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

}


