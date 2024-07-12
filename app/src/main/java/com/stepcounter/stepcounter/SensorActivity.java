package com.stepcounter.stepcounter;


import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private FirebaseAnalytics mFirebaseAnalytics;
    private static final String TAG="SensorActivity"; //tagul clasei, indiferent de cate obiecte ar avea, tagul ramane acelasi (static)
// private static final String TAG=SensorActivity.class.getSimpleName();
    private static final String TAGB="ButtonActivity";
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
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); //ii seteaza designul preluat din activity_main.xml
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        totalStepsCount=(TextView) findViewById(R.id.stepsTakenCount);
        targetNumber=(TextView) findViewById(R.id.targetNumber);
        changeTargetButton=(Button) findViewById(R.id.changeTargetButton);
        changeTargetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stepno=10000;
                Button buttonOK;
                Log.d(TAGB, "BUTTON PRESSED");
                Dialog dialog=new Dialog(SensorActivity.this);
                dialog.setContentView(R.layout.dialog_layout);
                buttonOK=(Button) dialog.findViewById(R.id.buttonOK);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                dialog.show();
                Spinner spinner_steps = dialog.findViewById(R.id.spinner_steps);
                ArrayList<Integer> arrayList=new ArrayList<>(); //making an array of possibile step goal values
                while(stepno>=5000){
                    arrayList.add(stepno);
                    stepno-=1000;
                }

                ArrayAdapter<Integer> adapter=new ArrayAdapter<>(SensorActivity.this, android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                spinner_steps.setAdapter(adapter);
                //making the actual spinner
                spinner_steps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        String item = adapterView.getItemAtPosition(position).toString();
                        buttonOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(SensorActivity.this,"New daily steps target: "+ item , Toast.LENGTH_SHORT).show();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        targetNumber.setText(String.valueOf(item));
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
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
}
