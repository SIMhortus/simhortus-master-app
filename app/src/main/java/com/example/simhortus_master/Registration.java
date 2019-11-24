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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class Registration extends AppCompatActivity  implements View.OnClickListener  {

    private EditText fName, lName, email, password;
    private Button button;

    //paano ko ba tong gagawin dynamic? pauliulit ko siyang ginamit
    private FirebaseAuth mAuth;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        button = findViewById(R.id.btnSubmit);

        awesomeValidation.addValidation(this, R.id.fName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.fName);
        awesomeValidation.addValidation(this, R.id.lName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.lName);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.email);
        awesomeValidation.addValidation(this, R.id.password, "^.{8,}$", R.string.password);

        mAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(this);
    }

    public void goToLogIn(View v){
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //repeating code
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, AccountSettings.class));
        }

    }


    private void submitForm() {
        //first validate the form then move ahead
        //if this becomes true that means validation is successfull
        if (awesomeValidation.validate()) {


            String user_email = email.getText().toString().trim();
            String user_pass = password.getText().toString().trim();
            final String firstName = fName.getText().toString().trim();
            final String lastName = lName.getText().toString().trim();
            Toast.makeText(Registration.this, "Validation Successfull", Toast.LENGTH_LONG).show();

            mAuth.createUserWithEmailAndPassword(user_email, user_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        UserInfo userInfo = new UserInfo(
                          firstName,
                          lastName
                        );

                        FirebaseDatabase.getInstance().getReference("UserID")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Registration.this, "Validation Successfull", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }  else {
                        Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            //process the data further
        }
    }


    @Override
    public void onClick(View v) {
        if (v == button) {
            submitForm();
        }
    }
}
