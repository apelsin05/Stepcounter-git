package com.stepcounter.stepcounter;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG="SensorActivity"; //tagul clasei, indiferent de cate obiecte ar avea, tagul ramane acelasi (static)
// private static final String TAG=SensorActivity.class.getSimpleName();

    //step counter sensor
    //i want to acces androids's sensors by SensorManager class
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    public SensorActivity() {

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "Accuracy "+accuracy);
    }

    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "Event "+event.toString());
    }


    TextView mText;
    TextView totalStepsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); //ii seteaza designul preluat din activity_main.xml
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mText=(TextView) findViewById(R.id.text);
        totalStepsText=(TextView) findViewById(R.id.text);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }



    /////

}
