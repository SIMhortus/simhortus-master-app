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

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    Button button;
    EditText editText;
    private AwesomeValidation awesomeValidation;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        button = findViewById(R.id.btnResetPass);
        editText = findViewById(R.id.resetEmail);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        mAuth = FirebaseAuth.getInstance();
        awesomeValidation.addValidation(this, R.id.resetEmail, Patterns.EMAIL_ADDRESS, R.string.email);

        button.setOnClickListener(this);
    }

    public void resetPassword() {

        String email = editText.getText().toString().trim();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent (ResetPassword.this, MainActivity.class));
                        } else {
                            Toast.makeText(ResetPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void submitForm() {
        //first validate the form then move ahead
        //if this becomes true that means validation is successful
        if (awesomeValidation.validate()) {
            resetPassword();
        }
    }


    @Override
    public void onClick(View v) {
        if (v == button) {
            submitForm();
        }
    }
}