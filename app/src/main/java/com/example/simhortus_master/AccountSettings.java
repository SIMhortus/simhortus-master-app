package com.example.simhortus_master;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class AccountSettings extends AppCompatActivity {

    TextView textView;
    private FirebaseAuth mAuth;
    private Button btnLogout, btnEmail, btnPass;
    private AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        textView = findViewById(R.id.textViewVerified);
        btnLogout = findViewById(R.id.logout);
        btnPass = findViewById(R.id.updPass);
        mAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut();
                startActivity(new Intent(AccountSettings.this, Login.class));
            }
        });

        //UPDATE PASSWORD
        btnPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openDialogUpdPass();
            }
        });

    }

    public void openDialogUpdPass(){

        UpdatePassword updatePassword = new UpdatePassword();
        updatePassword.show(getSupportFragmentManager(), "Change Password");

    }

    protected void onStart() {
        super.onStart();

       final FirebaseUser user = mAuth.getCurrentUser();


        if (user == null) {
            startActivity(new Intent(AccountSettings.this, Login.class));

        } else if (user.isEmailVerified()) {

            textView.setText("Email  verified");

        } else {
            String userName = user.getEmail();

            textView.setText("Email not verified (Click to verify) "+userName);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AccountSettings.this, "Verification Email sent", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

    }

}
