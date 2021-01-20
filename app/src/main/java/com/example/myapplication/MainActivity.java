package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String uniqueID = UUID.randomUUID().toString();
        Button scan = findViewById(R.id.btn_scan);
        scan.setOnClickListener(this);
        tv = findViewById(R.id.textView);
        tv.setText(uniqueID);

        ImageView img = findViewById(R.id.imageView);
        Button button = findViewById(R.id.btn_encrypt);
        TextView textView = findViewById(R.id.edt_qrdcode);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeGenerator(textView.getText().toString(), img);
            }
        });

    }

    @Override
    public void onClick(View v) {
        scan();
    }

    private void scan() {
        IntentIntegrator integrator =  new IntentIntegrator(this);
        integrator.setCaptureActivity(CapActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() != null){
                tv.setText(result.getContents());
            }else{
                Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void QRCodeGenerator(String texto, ImageView imgQRCode) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter
                    .encode(texto, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            imgQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}