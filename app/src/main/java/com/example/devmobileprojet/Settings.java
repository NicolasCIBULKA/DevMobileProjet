package com.example.devmobileprojet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

/*
    Activity to change the sensor option
 */
public class Settings extends AppCompatActivity {
    private static final String TAG = "Settings Activity"; // TAG for logs

    // Attributs

    boolean activateSensorSetting;
    private SharedPreferences sharedPref = null;
    private SharedPreferences.Editor editor;
    Switch setSensor;
    // Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // SharePreferences, to change the settings of the sensor
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();
        activateSensorSetting = sharedPref.getBoolean("enableSensor", true);
        setSensor = findViewById(R.id.setSensor);
        // set the switch to the current setting, ie true or false
        setSensor.setChecked(activateSensorSetting);

    }

    // Method to change the option of the sensor
    public void sensorSetting(View view){
        if(activateSensorSetting == false){
            activateSensorSetting = true;
            editor.putBoolean("enableSensor", true);
            Log.d(TAG, "sensorSetting: Sensor is enabled");
        }
        else{
            activateSensorSetting = false;
            editor.putBoolean("enableSensor", false);
            Log.d(TAG, "sensorSetting: Sensor is disabled");
        }
        editor.commit();
        setSensor.setChecked(activateSensorSetting);
    }
}