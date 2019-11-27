package com.example.simhortus_master;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Registration extends AppCompatActivity  implements View.OnClickListener  {

    private EditText fName, phone, lName, email, password;
    private Button button;

    private FirebaseAuth mAuth;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        phone = findViewById(R.id.phone);
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
        startActivity(new Intent(this, Login.class));
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
        //if this becomes true that means validation is successful
        if (((isValid(phone.getText().toString())) && (phone.getText().toString().length() == 10) ) || (phone.getText().toString().equals(""))) {
            if (awesomeValidation.validate()) {

                String user_email = email.getText().toString().trim();
                String user_pass = password.getText().toString().trim();
                final String firstName = fName.getText().toString().trim();
                final String lastName = lName.getText().toString().trim();
                final String phoneNumber = "+63" + phone.getText().toString().trim();
                //Toast.makeText(Registration.this, "Validation Successfull", Toast.LENGTH_LONG).show();

                mAuth.createUserWithEmailAndPassword(user_email, user_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserID");
                            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("fName").setValue(firstName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("lName").setValue(lastName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child("phone").setValue(phoneNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                startActivity(new Intent(Registration.this, MainActivity.class));
                                                            } else {
                                                                Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
        else{
            phone.setError( "Invalid phone number" );
        }
    }


    @Override
    public void onClick(View v) {
        if (v == button) {
            submitForm();
        }
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
