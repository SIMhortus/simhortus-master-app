package com.example.simhortus_master;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.DateFormat;
import java.util.Date;

public class try_your_code_here extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_your_code_here);
        button = findViewById(R.id.press_me);

        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("button", "onClick: "+ currentDateTimeString);
            }
        });

    }
}
