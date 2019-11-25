package com.example.simhortus_master;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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



public class AccountSettings extends AppCompatActivity implements View.OnClickListener {

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
        btnEmail = findViewById(R.id.updEmail);
        btnPass = findViewById(R.id.updPass);

        mAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        initClick();

    }

    protected void onStart() {
        super.onStart();

       final FirebaseUser user = mAuth.getCurrentUser();


        if (user == null) {
            startActivity(new Intent(AccountSettings.this, MainActivity.class));

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
                            Toast.makeText(AccountSettings.this,
                                    "Verification Email sent",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

    }

    public void initClick() {
        btnLogout.setOnClickListener(this);
        btnEmail.setOnClickListener(this);
    }

    public void ShowPopupEmail() {
        Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.mod_email);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button update = findViewById(R.id.btnEMUpdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AccountSettings.this,
                        " Email sent",
                        Toast.LENGTH_LONG).show();

            }
        });

        myDialog.show();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.logout:
                mAuth.getInstance().signOut();
                startActivity(new Intent(AccountSettings.this, MainActivity.class));
                break;

            case R.id.updEmail:
                    ShowPopupEmail();
                break;

        }
    }

}
