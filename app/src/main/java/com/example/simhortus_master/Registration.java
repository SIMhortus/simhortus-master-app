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

    private EditText fName, lName, phone, email, password;
    private Button button;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;

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

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        button = findViewById(R.id.btnSubmit);

        awesomeValidation.
                addValidation(this, R.id.fName,
                "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$",
                R.string.fName);

        awesomeValidation.
                addValidation(this, R.id.lName,
                        "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$",
                        R.string.lName);

        awesomeValidation.addValidation(this,
                R.id.email, Patterns.EMAIL_ADDRESS,
                R.string.email);

        awesomeValidation.addValidation(this, R.id.password,
                "^.{8,}$",
                R.string.password);


        button.setOnClickListener(this);
    }

    public void goToLogIn(View v){
        startActivity(new Intent(this, Login.class));
    }

    private void submitForm() {
        if (((isValid(phone.getText().toString())) && (phone.getText().toString().length() == 10) ) || (phone.getText().toString().equals(""))) {
            if (awesomeValidation.validate()) {

                String user_email = email.getText().toString().trim();
                String user_pass = password.getText().toString().trim();
                final String firstName = fName.getText().toString().trim();
                final String lastName = lName.getText().toString().trim();
                String getPhoneNumber = phone.getText().toString().trim();
                final String phoneNumber;

                if(getPhoneNumber.equals("")){
                    phoneNumber = "";
                }
                else {
                    phoneNumber = "+63" + phone.getText().toString().trim();
                }


                final DatabaseReference ref = firebaseDatabase.getReference("Users");

                mAuth.createUserWithEmailAndPassword(user_email, user_pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    UserInfo userInfo = new UserInfo(
                                            firstName,
                                            lastName,
                                            phoneNumber
                                    );

                                    final String uID = mAuth.getCurrentUser().getUid();
                                    ref.child(uID)
                                            .setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(Registration.this, MainActivity.class));
                                            } else {
                                                Global.showToast(task.getException().getMessage(), Registration.this);
                                            }
                                        }
                                    });
                                } else
                                    Global.showToast(task.getException().getMessage(), Registration.this);
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
