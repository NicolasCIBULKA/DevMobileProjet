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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    boolean isPlaying = true;
    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        Intent playIntent = (Intent)b.get("ServiceIntent");
        if(playIntent==null){
            playIntent = new Intent(this, PlayingService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }

        TextView title = findViewById(R.id.titleMusic);
        TextView artist = findViewById(R.id.artist);
        //Log.d("TAG", "onCreate: " + musicSrv.getMusiclist().get(musicSrv.getMusicposition()).getTitle());
        //title.setText(musicSrv.getMusiclist().get(musicSrv.getMusicposition()).getTitle());
        //artist.setText(musicSrv.getMusiclist().get(musicSrv.getMusicposition()).getArtist());


    }

    private ServiceConnection musicConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayingService.MusicBinder binder = (PlayingService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void startPause(View view){
        if(isPlaying == true){
            isPlaying = false;
            view.setBackgroundResource(R.drawable.play);
        }
        else{
            isPlaying = true;
            view.setBackgroundResource(R.drawable.end);
        }
    }

    public void nextMusic(View view){

    }

    public void previousMusic(View view){

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