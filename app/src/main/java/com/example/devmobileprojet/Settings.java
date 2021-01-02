package com.example.devmobileprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import static com.example.devmobileprojet.MusicListActivity.PREFS;

public class Settings extends AppCompatActivity {
    private static final String TAG = "Settings Activity";
    // Attributs

    static boolean activateSensorSetting;
    static SharedPreferences prefs ;
    static SharedPreferences.Editor editor;
    Switch setSensor;
    // Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        prefs = getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        editor = prefs.edit();
        activateSensorSetting = Musicplayer.enabledSensor;
        setSensor = findViewById(R.id.setSensor);
        setSensor.setChecked(activateSensorSetting);

    }

    public void sensorSetting(View view){
        if(activateSensorSetting == false){
            activateSensorSetting = true;
            Log.d(TAG, "sensorSetting: Sensor is enabled");
        }
        else{
            activateSensorSetting = false;
            Log.d(TAG, "sensorSetting: Sensor is disabled");
        }
        editor.putBoolean("SensorPref", activateSensorSetting);
        Musicplayer.enabledSensor = activateSensorSetting;
    }
}