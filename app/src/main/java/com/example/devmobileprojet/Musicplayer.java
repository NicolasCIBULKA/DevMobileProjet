package com.example.devmobileprojet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
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
    private PlayingService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);

    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, PlayingService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    private ServiceConnection musicConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayingService.MusicBinder binder = (PlayingService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(musicList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
    */
    // functions todo
    /*
    - play music
    - pause music
    - next music
    - previous music
    - go to MusicList Activity
     */




}