package com.example.simhortus_master;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button buttonL, btnSwitch, btnLoginVCode;
    private EditText email, password, loginPhone, loginCode;
    private TextInputLayout tilEmail, tilPassword, tilPhone, tilCode;
    private String checkLoginWith = "email", codeSent, checkRegisteredPhoneNumber = "false";;
    private TextView forgotPassword;

    private AwesomeValidation awesomeValidation;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonL = findViewById(R.id.btnLogin);
        btnSwitch = findViewById(R.id.btnSwitch);
        btnLoginVCode = findViewById(R.id.btnLoginVCode);
        email = findViewById(R.id.lEmail);
        password  = findViewById(R.id.lPassword);
        forgotPassword  = findViewById(R.id.forgot_password);
        loginPhone = findViewById(R.id.loginPhone);
        loginCode  = findViewById(R.id.loginCode);
        tilEmail = findViewById(R.id.text_input_layout);
        tilPassword = findViewById(R.id.loginPassword);
        tilCode = findViewById(R.id.tilCode);
        tilPhone = findViewById(R.id.tilPhone);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        mAuth =  FirebaseAuth.getInstance();
        awesomeValidation.addValidation(this, R.id.lEmail, Patterns.EMAIL_ADDRESS, R.string.email);

        buttonL.setOnClickListener(this);
        btnSwitch.setOnClickListener(this);
        btnLoginVCode.setOnClickListener(this);

    }

    private void userLogin() {

        String lEmail = email.getText().toString().trim();
        final String lPassword = password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(lEmail, lPassword)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void submitForm() {

        if (awesomeValidation.validate()) {
            userLogin();
        }
    }

    private void switchForm() {

        if (checkLoginWith == "email") {
            email.setVisibility(View.GONE);
            tilEmail.setVisibility(View.GONE);
            tilPassword.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            forgotPassword.setVisibility(View.GONE);
            loginPhone.setVisibility(View.VISIBLE);
            loginCode.setVisibility(View.VISIBLE);
            tilPhone.setVisibility(View.VISIBLE);
            tilCode.setVisibility(View.VISIBLE);
            btnLoginVCode.setVisibility(View.VISIBLE);
            btnSwitch.setText("USE EMAIL INSTEAD");
            checkLoginWith = "phone";
        }
        else{
            email.setVisibility(View.VISIBLE);
            tilEmail.setVisibility(View.VISIBLE);
            tilPassword.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            forgotPassword.setVisibility(View.VISIBLE);
            loginPhone.setVisibility(View.GONE);
            loginCode.setVisibility(View.GONE);
            tilPhone.setVisibility(View.GONE);
            tilCode.setVisibility(View.GONE);
            btnLoginVCode.setVisibility(View.GONE);
            btnSwitch.setText("USE PHONE NUMBER INSTEAD");
            checkLoginWith = "email";
        }
    }

    public void goToReg(View v){
        startActivity(new Intent(this, Registration.class));
    }

    public void gotoForgotPass(View v){
        startActivity(new Intent(this, ResetPassword.class));
    }

    @Override
    public void onClick(View v) {
        if (v == buttonL) {
            if(checkLoginWith == "email"){
                submitForm();
            }
            else{
                if(TextUtils.isEmpty(loginPhone.getText()))
                {
                    loginPhone.setError( "Phone number is required!" );
                }
                else if(TextUtils.isEmpty(loginCode.getText()))
                {
                    loginCode.setError( "Verification code is required!" );
                }
                else {
                    reference.child("Users").addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if(snapshot.child("phone_number").getValue().toString().equals("+63" + loginPhone.getText().toString().substring(1))){
                                    if (dataSnapshot.exists()) {
                                        verifyCode();
                                    }
                                }
                                else {
                                    Toast.makeText(Login.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Login.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
        else if(v == btnSwitch){
            switchForm();
        }
        else if(v == btnLoginVCode){
            sendVerificationCode();
        }
    }

    protected void onStart() {
        super.onStart();

        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            startActivity(new Intent(Login.this, MainActivity.class));

        }

    }

    //sending code
    private void sendVerificationCode(){
        if(TextUtils.isEmpty(loginPhone.getText()))
        {
            loginPhone.setError( "Phone number is required!" );
        }
        else if (((isValid(loginPhone.getText().toString())) && (loginPhone.getText().toString().length() == 11))) {
            reference.child("Users").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.child("phone_number").getValue().toString().equals("+63" + loginPhone.getText().toString().substring(1))){
                            if (dataSnapshot.exists()) {
                                checkRegisteredPhoneNumber = "true";
                                String sendPhoneNumber = "+63" + loginPhone.getText().toString().trim().substring(1);

                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        sendPhoneNumber,        // Phone number to verify
                                        60,                 // Timeout duration
                                        TimeUnit.SECONDS,   // Unit of timeout
                                        Login.this,               // Activity (for callback binding)
                                        mCallbacks);        // OnVerificationStateChangedCallbacks
                                break;
                            }
                        }
                        else {
                            checkRegisteredPhoneNumber = "false";
                        }
                    }
                    if(checkRegisteredPhoneNumber == "false"){
                        Toast.makeText(Login.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        else{
            loginPhone.setError( "Please enter a valid phone number" );
        }
    }

    //For mCallbacks
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(Login.this, "Verifying", Toast.LENGTH_SHORT).show();
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Login.this, "Verification failed due to: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            String sendPhoneNumber = "+63" + loginPhone.getText().toString().trim().substring(1);
            Toast.makeText(Login.this, "Sending code to " + sendPhoneNumber, Toast.LENGTH_SHORT).show();
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };

    //submitting
    private void verifyCode(){
        Toast.makeText(Login.this, "Verifying", Toast.LENGTH_SHORT).show();
        String code = loginCode.getText().toString();
        PhoneAuthCredential phoneCredential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(phoneCredential);
    }

    //updating phone
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(Login.this, "Wrong verification code", Toast.LENGTH_LONG).show();
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
        Pattern p = Pattern.compile("(0)(9)?[0-9]{9}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }
}
