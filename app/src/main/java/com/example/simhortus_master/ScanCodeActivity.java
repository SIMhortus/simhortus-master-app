package com.example.simhortus_master;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView ScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

        this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 1011);
    }

    public void onDestroy() {
        super.onDestroy();

        ScannerView = null;
        ScannerView.stopCamera();

    }

    @Override
    public void handleResult(Result result) {
        final String dID = result.getText();

        //ScanGarden.resultTextView.setText(result.getText());
        // put your code here


    }

    @Override
    protected void onResume() {
        super.onResume();

        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
    @Override
    protected void onPause() {
        super.onPause();

        ScannerView.stopCamera();
    }



}
