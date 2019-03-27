package com.example.tiltapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagnetometer;

    private float[] accelerometerData = new float[3];
    private float[] magnetometerData = new float[3];

    private TextView textSensorAzimuth;
    private TextView textSensorPitch;
    private TextView textSensorRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSensorAzimuth = (TextView) findViewById(R.id.label_azimuth);
        textSensorPitch = (TextView) findViewById(R.id.label_pitch);
        textSensorRoll = (TextView) findViewById(R.id.label_roll);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sensorAccelerometer != null){
            sensorManager.registerListener(this,sensorAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(sensorMagnetometer != null){
            sensorManager.registerListener(this,sensorMagnetometer,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        switch (sensorType){
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerData = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetometerData = sensorEvent.values.clone();
                break;
        }
        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,null,accelerometerData,magnetometerData);
        float[] orientationValues = new float[3];
        if(rotationOK){
            SensorManager.getOrientation(rotationMatrix,orientationValues);
        }

        float azimuth = orientationValues[0];
        float pitch = orientationValues[1];
        float roll = orientationValues[2];

        textSensorAzimuth.setText("Azimuth (z) : "+String.format("%.2f",azimuth));
        textSensorPitch.setText("Pitch (y) : "+String.format("%.2f",pitch));
        textSensorRoll.setText("Roll (x) : "+String.format("%.2f",roll));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
