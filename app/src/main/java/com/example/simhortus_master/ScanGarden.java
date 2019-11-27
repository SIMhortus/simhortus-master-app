package com.example.simhortus_master;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ScanGarden extends AppCompatActivity {
    public static EditText resultTextView;
    Button scan_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_garden);

        scan_btn = findViewById(R.id.btn_scan);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),  ScanCodeActivity.class);
                startActivity(intent);
            }
        });

    }

}