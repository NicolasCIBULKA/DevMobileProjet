package com.example.devmobileprojet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.devmobileprojet.dataclass.ActionPlaying;
import com.example.devmobileprojet.dataclass.Music;
import com.example.devmobileprojet.dataclass.MusicController;
import com.example.devmobileprojet.treatment.PlayingService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.devmobileprojet.MusicListActivity.PREFS;

/*
    Activity that display the current music played

 */
public class Musicplayer extends AppCompatActivity implements ActionPlaying, ServiceConnection{
    // attributs
    private static final String TAG = "Music Player";

    // elements displayed and that have interactions with service
    TextView song_name, artist_name, duration_played, duration_total;
    SeekBar seekbar;
    ImageView nextBtn, prevBtn;
    FloatingActionButton playpause;
    View root;

    // Service elements
    PlayingService musicSrv;
    SensorManager sensorManager;
    Sensor lightSensor;
    SensorEventListener lightEventListener;
    float maxValue;
    static boolean existingSensor;
    static boolean enabledSensor;
    // data structures
    static ArrayList<Music> musicList = new ArrayList<Music>();
    static int position = -1;
    static Uri uri;
    static Uri url;
    // Thread elements
    private Handler handler = new Handler();
    private Thread playThread, previousThread, nextThread;
    // SharedPreferences
    static SharedPreferences prefs ;
    static SharedPreferences.Editor editor;

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);
        musicList = musicList;
        getSupportActionBar().hide();
        prefs = getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        enabledSensor = prefs.getBoolean("SensorPref", true);
        Log.d(TAG, "onCreate: is sensor enabled ?" + enabledSensor);
        // Initialisation du service du sensor
        root = findViewById(R.id.root);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor == null){
            existingSensor = false;
            Log.d(TAG, "onCreate: Light sensor does not exists");
        }
        else{
            Log.d(TAG, "onCreate: Light Sensor exists");
            existingSensor = true;
        }
        maxValue = lightSensor.getMaximumRange();

        // Initialisation of the Activity
        initViews();
        getIntentMethod();
        // Listener of Seekbar
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(musicSrv != null && fromUser){
                    musicSrv.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Thread to move the Seekbar according to the music progress
        Musicplayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(musicSrv != null){
                    int currentPos = musicSrv.getCurrentPosition() / 1000 ;
                    seekbar.setProgress(currentPos);
                    duration_played.setText(formattedTime(currentPos));
                }
                handler.postDelayed(this, 1000);
            }
        });

        // Listener du sensor
        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(enabledSensor) {
                    float value = event.values[0];
                    float limitSensor = maxValue / 3;
                    if (value < limitSensor) {
                        // going to soft eye mod
                        root.setBackgroundColor(0xff646767);
                        song_name.setTextColor(0xffffffff);
                        artist_name.setTextColor(0xffffffff);
                        duration_played.setTextColor(0xffffffff);
                        duration_total.setTextColor(0xffffffff);

                    } else {
                        root.setBackgroundColor(0xffffffff);
                        song_name.setTextColor(0xff000000);
                        artist_name.setTextColor(0xff000000);
                        duration_played.setTextColor(0xff000000);
                        duration_total.setTextColor(0xff000000);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

    }

    private String formattedTime(int currentPos) {
        String totalout = "";
        String totalNew = "";
        String seconds = String.valueOf(currentPos % 60);
        String minutes = String      .valueOf(currentPos / 60);
        totalout = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if(seconds.length() == 1){
            return totalNew;
        }
        else{
            return totalout;
        }
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        Log.d(TAG, "getIntentMethod: Song picked is : " + position);
        musicList = MusicListActivity.musicList;
        Log.d(TAG, "getIntentMethod: MusicList size is : " + musicList.size());
        url = null;
        if(musicList != null){
            playpause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            uri = Uri.parse(musicList.get(position).getPosition());
            url = Uri.parse("file://"+musicList.get(position).getPosition());
        }
        Intent i = new Intent(this, PlayingService.class);
        i.putExtra("servicePos", position);
        startService(i);
    }

    private void initViews() {
        song_name = findViewById(R.id.titleMusic);
        artist_name = findViewById(R.id.artist);
        duration_played = findViewById(R.id.currentduration);
        duration_total = findViewById(R.id.totalduration);
        nextBtn = findViewById(R.id.nextBtn);
        prevBtn = findViewById(R.id.prevBtn);
        playpause = findViewById(R.id.playpause);
        seekbar = findViewById(R.id.seekBar);
    }

    @Override
    protected void onResume() {
        Intent i = new Intent(this, PlayingService.class);
        bindService(i, this, BIND_AUTO_CREATE);
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);

        super.onResume();

    }



    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
        sensorManager.unregisterListener(lightEventListener);
    }

    // Button Threads
    private void prevThreadBtn() {
        previousThread = new Thread(){
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }

        };
        previousThread.start();
    }

    public void prevBtnClicked() {
        if(musicSrv.isPlaying()){
            musicSrv.stop();
            musicSrv.release();
            position = ((position-1) < 0 ? (musicList.size()-1) : (position-1) );
            uri = Uri.parse(musicList.get(position).getPosition());
            url = Uri.parse("file://"+musicList.get(position).getPosition());
            musicSrv.createPlayer(position);
            metaData(uri);
            song_name.setText(musicList.get(position).getTitle());
            artist_name.setText(musicList.get(position).getArtist());
            seekbar.setMax(musicSrv.getDuration()/1000);
            Musicplayer.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicSrv != null){
                        int mCurrentPos = musicSrv.getCurrentPosition() / 1000;
                        seekbar.setProgress(mCurrentPos);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicSrv.onCompleted();
            //showNotification(R.drawable.ic_pause_circle);
            playpause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            musicSrv.start();
        }
        else{
            musicSrv.stop();
            musicSrv.release();
            position = ((position-1) < 0 ? (musicList.size()-1) : (position-1) );
            uri = Uri.parse(musicList.get(position).getPosition());
            url = Uri.parse("file://"+musicList.get(position).getPosition());
            musicSrv.createPlayer(position);
            metaData(uri);
            song_name.setText(musicList.get(position).getTitle());
            artist_name.setText(musicList.get(position).getArtist());
            seekbar.setMax(musicSrv.getDuration()/1000);
            Musicplayer.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicSrv != null){
                        int mCurrentPos = musicSrv.getCurrentPosition() / 1000;
                        seekbar.setProgress(mCurrentPos);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicSrv.onCompleted();
            //showNotification(R.drawable.ic_baseline_play);
            playpause.setImageResource(R.drawable.play);
        }
    }


    public void playThreadBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                playpause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }

        };
        playThread.start();
    }

    public void playPauseBtnClicked() {
        if(musicSrv.isPlaying()){
            playpause.setImageResource(R.drawable.play);
            //showNotification(R.drawable.ic_baseline_play);
            musicSrv.pause();
            seekbar.setMax(musicSrv.getDuration()/1000);
            Musicplayer.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicSrv != null){
                        int mCurrentPos = musicSrv.getCurrentPosition() / 1000;
                        seekbar.setProgress(mCurrentPos);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
        else{
            //showNotification(R.drawable.ic_pause_circle);
            playpause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            musicSrv.start();
            seekbar.setMax(musicSrv.getDuration()/1000);
            Musicplayer.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicSrv != null){
                        int mCurrentPos = musicSrv.getCurrentPosition() / 1000;
                        seekbar.setProgress(mCurrentPos);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    public void nextThreadBtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }

        };
        nextThread.start();
    }

    public void nextBtnClicked() {
        if(musicSrv.isPlaying()){
            musicSrv.stop();
            musicSrv.release();
            position = ((position++) % musicList.size());
            uri = Uri.parse(musicList.get(position).getPosition());
            url = Uri.parse("file://"+musicList.get(position).getPosition());
            musicSrv.createPlayer(position);
            metaData(uri);
            song_name.setText(musicList.get(position).getTitle());
            artist_name.setText(musicList.get(position).getArtist());
            seekbar.setMax(musicSrv.getDuration()/1000);
            Musicplayer.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicSrv != null){
                        int mCurrentPos = musicSrv.getCurrentPosition() / 1000;
                        seekbar.setProgress(mCurrentPos);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicSrv.onCompleted();
            //showNotification(R.drawable.ic_pause_circle);
            playpause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            musicSrv.start();
        }
        else{
            musicSrv.stop();
            musicSrv.release();
            position = ((position++) % musicList.size());
            uri = Uri.parse(musicList.get(position).getPosition());
            url = Uri.parse("file://"+musicList.get(position).getPosition());
            musicSrv.createPlayer(position);
            metaData(uri);
            song_name.setText(musicList.get(position).getTitle());
            artist_name.setText(musicList.get(position).getArtist());
            seekbar.setMax(musicSrv.getDuration()/1000);
            Musicplayer.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicSrv != null){
                        int mCurrentPos = musicSrv.getCurrentPosition() / 1000;
                        seekbar.setProgress(mCurrentPos);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicSrv.onCompleted();
            //showNotification(R.drawable.ic_baseline_play);
            playpause.setImageResource(R.drawable.play);
        }
    }



    private void metaData(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        Log.d(TAG, "metaData: size of list"+ musicList.size());
        Log.d(TAG, "metaData: "+ musicList.get(position).getPosition());
        int durationtotal = Integer.parseInt(musicList.get(position).getDuration()) / 1000;
        duration_total.setText(formattedTime(durationtotal));
    }



    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        PlayingService.MyBinder binder = (PlayingService.MyBinder)service;
        musicSrv = binder.getService();
        if(musicSrv == null){
            Log.d(TAG, "onServiceConnected: service null - " + musicSrv.getDuration());
        }
        Log.d(TAG, "onServiceConnected: "+ musicList.get(position).getTitle());
        Log.d(TAG, "onServiceConnected: Service Connected");
        seekbar.setMax(musicSrv.getDuration() / 1000);
        metaData(uri);
        song_name.setText(musicList.get(position).getTitle());
        artist_name.setText(musicList.get(position).getArtist());
        musicSrv.onCompleted();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicSrv = null;
        Log.d(TAG, "onServiceConnected: Service disonnected");
    }


}