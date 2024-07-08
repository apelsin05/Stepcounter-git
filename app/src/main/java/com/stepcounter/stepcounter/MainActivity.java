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

public class MainActivity extends AppCompatActivity  implements SensorEventListener {

    TextView mText;
    TextView totalStepsText;








    @Override //onCreate(bundle) initializing my acivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); //ii seteaza designul preluat din activity_main.xml
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mText=(TextView) findViewById(R.id.title);
        totalStepsText=(TextView) findViewById(R.id.targetText);
        totalStepsText=(TextView) findViewById(R.id.stepsTakenCount);

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
      //  Log.d(TAG, "Accuracy "+accuracy);
    }

    public void onSensorChanged(SensorEvent event) {
       // Log.d(TAG, "Event "+event.toString());
    }



}