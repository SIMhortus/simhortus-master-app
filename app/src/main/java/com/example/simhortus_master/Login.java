package com.example.simhortus_master;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button buttonL;
    private EditText email, password;

    private AwesomeValidation awesomeValidation;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonL = findViewById(R.id.btnLogin);
        email = findViewById(R.id.lEmail);
        password  = findViewById(R.id.lPassword);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        mAuth =  FirebaseAuth.getInstance();
        awesomeValidation.addValidation(this, R.id.lEmail, Patterns.EMAIL_ADDRESS, R.string.email);

        buttonL.setOnClickListener(this);
    }

    private void userLogin() {

        String lEmail = email.getText().toString().trim();
        final String lPassword = password.getText().toString().trim();
        final LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.show(getSupportFragmentManager(), "Loading");
        mAuth.signInWithEmailAndPassword(lEmail, lPassword)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            loadingScreen.dismiss();
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

    public void goToReg(View v){
        startActivity(new Intent(this, Registration.class));
    }

    public void gotoForgotPass(View v){
        startActivity(new Intent(this, ResetPassword.class));
    }

    @Override
    public void onClick(View v) {
        if (v == buttonL) {
            submitForm();
        }
    }

    protected void onStart() {
        super.onStart();

        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            startActivity(new Intent(Login.this, MainActivity.class));

        }

    }
}
