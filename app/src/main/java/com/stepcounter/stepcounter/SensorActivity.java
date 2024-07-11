package com.stepcounter.stepcounter;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG="SensorActivity"; //tagul clasei, indiferent de cate obiecte ar avea, tagul ramane acelasi (static)
// private static final String TAG=SensorActivity.class.getSimpleName();
    private static final String TAGB="ButtonActivity";

    //private TextView mText;
    private TextView totalStepsText;
    private TextView totalStepsCount;
    private TextView targetNumber;
    private Button changeTargetButton; //button to change the steps target
    private float totalSteps=0; //has the total steps until present moment
     //step counter sensor
    //i want to acces androids's sensors by SensorManager class
    private SensorManager mSensorManager;
    private Sensor mSensor;


    public SensorActivity() {

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "Accuracy "+accuracy);
    }

    public void onSensorChanged(SensorEvent event) {
        //Log.d(TAG, "Event "+event.toString());
        totalSteps=event.values[0]; //accesez primul element din array
        Log.d(TAG,"STEPS = " + totalSteps);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                totalStepsCount.setText(String.valueOf(totalSteps));
            }
        });
    }



    @Override //onCreate(bundle) initializing my activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); //ii seteaza designul preluat din activity_main.xml
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //mText=(TextView) findViewById(R.id.title);
        totalStepsText=(TextView) findViewById(R.id.targetText);
        totalStepsCount=(TextView) findViewById(R.id.stepsTakenCount);
        totalStepsText=(TextView) findViewById(R.id.targetNumber);
        changeTargetButton=(Button) findViewById(R.id.changeTargetButton);
        changeTargetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAGB, "BUTTON PRESSED");
                int targetNew;
                //targetNew=user input
                //targetNumber=(TextView) targetNew.
                AlertDialog.Builder builder=new AlertDialog.Builder(SensorActivity.this);
                builder.setTitle(R.string.dialog_title).setPositiveButton("OK",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(mSensor==null){
            Log.e(TAG, " NULL ");
        }
        else {
            Log.d(TAG, " NOT NULL");
        }

        if(ContextCompat.checkSelfPermission(this,
                "android.permission.ACTIVITY_RECOGNITION") == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "ACTIVITY_RECOGNITION not granted!");
            //ask for permission
            requestPermissions(new String[]{"android.permission.ACTIVITY_RECOGNITION"}, 42);
        }




    }



    /////

}
