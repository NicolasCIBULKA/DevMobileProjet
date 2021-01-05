package com.example.devmobileprojet.treatment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.devmobileprojet.MusicListActivity;
import com.example.devmobileprojet.Musicplayer;
import com.example.devmobileprojet.R;
import com.example.devmobileprojet.dataclass.ActionPlaying;
import com.example.devmobileprojet.dataclass.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.example.devmobileprojet.MusicListActivity.musicList;


public class PlayingService extends Service implements MediaPlayer.OnCompletionListener {

    private static final String TAG = " Playing Service" ;
    // Attributs
    IBinder mBinder = new MyBinder();
    MediaPlayer player;
    ArrayList<Music> musicFiles = new ArrayList<>();
    Uri url;
    int position = -1;
    ActionPlaying actionPlaying;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    // Called when we click on an item of the ListView
    // play the music on the position of the listview
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myposition = intent.getIntExtra("servicePos", -1);
        if(myposition != -1){
            playMedia(myposition);
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public PlayingService getService(){
            return PlayingService.this;
        }
    }

    public class MusicBinder extends Binder {
        public PlayingService getService() {
            return PlayingService.this;
        }
    }

    // Method to play music
    private void playMedia(int Startposition) {
        musicFiles = musicList;
        position = Startposition;
        if(player != null){
            player.stop();
            player.release();
            if(musicFiles != null){
                createPlayer(position);
                player.start();
            }
        }
        else{
            createPlayer(position);
            player.start();
        }
    }

    public void start(){
        player.start();
    }

    public void pause(){
        player.pause();
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void stop(){
        player.stop();
    }

    public void release(){
        player.release();
    }

    public int getDuration(){
        return player.getDuration();
    }

    public void seekTo(int position){
        player.seekTo(position);
    }

    public void createPlayer(int position){
        Log.d(TAG, "createPlayer: "+musicFiles.get(position).getPosition());
        url = Uri.parse("file://"+musicFiles.get(position).getPosition());
        player = MediaPlayer.create(getBaseContext(), url);
    }
    public int getCurrentPosition(){
        return player.getCurrentPosition();
    }

    public void onCompleted(){
        player.setOnCompletionListener(this);
    }

    @Override
    // Called when the music has been fully played
    // Go to next music
    public void onCompletion(MediaPlayer mp) {
        if(actionPlaying != null){
            actionPlaying.nextBtnClicked();
        }
        createPlayer(position);
        player.start();
        onCompleted();

    }
}


