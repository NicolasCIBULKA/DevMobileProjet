package com.example.devmobileprojet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;

import com.example.devmobileprojet.treatment.PlayingService;

/*
    Activity that display the current music played
    FRONT TODO
 */
public class Musicplayer extends AppCompatActivity {
    // attributs

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //if()
    }

    // functions todo
    /*
    - play music
    - pause music
    - next music
    - previous music
    - go to MusicList Activity
     */




}