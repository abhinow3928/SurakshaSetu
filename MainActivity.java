package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    Button sosButton;

    String phoneNumber = "9309239307"; // Replace with family number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sosButton = findViewById(R.id.btnSOS);

        // Request SMS Permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    100);
        }

        sosButton.setOnClickListener(view -> sendSOSMessage());
    }

    private void sendSOSMessage() {

        String message = "Emergency! I need help. Please contact me immediately.";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,
                    null,
                    message,
                    null,
                    null);

            Toast.makeText(this,
                    "SOS Message Sent!",
                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Toast.makeText(this,
                    "Failed to send message",
                    Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == 100) {

            if (!(grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)) {

                Toast.makeText(this,
                        "SMS Permission Denied",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}