package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;

    Button sosButton;

    String phoneNumber = "9876543210";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sosButton = findViewById(R.id.btnSOS);

        // SMS Permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    100);
        }

        // Button Click
        sosButton.setOnClickListener(v -> sendSOS());

        // Shake Detection Setup
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        acceleration = 10f;
        currentAcceleration = SensorManager.GRAVITY_EARTH;
        lastAcceleration = SensorManager.GRAVITY_EARTH;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (accelerometer != null) {
            sensorManager.registerListener(this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        lastAcceleration = currentAcceleration;

        currentAcceleration = (float) Math.sqrt(
                (double) (x * x + y * y + z * z)
        );

        float delta = currentAcceleration - lastAcceleration;

        acceleration = acceleration * 0.9f + delta;

        // SHAKE DETECTED
        if (acceleration > 12) {

            Toast.makeText(this,
                    "Shake Detected!",
                    Toast.LENGTH_SHORT).show();

            sendSOS();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void sendSOS() {

        String message = "Emergency! I need help immediately.";

        try {

            SmsManager smsManager = SmsManager.getDefault();

            smsManager.sendTextMessage(
                    phoneNumber,
                    null,
                    message,
                    null,
                    null
            );

            Toast.makeText(this,
                    "SOS Message Sent!",
                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Toast.makeText(this,
                    "Failed to send message",
                    Toast.LENGTH_LONG).show();
        }
    }
}
