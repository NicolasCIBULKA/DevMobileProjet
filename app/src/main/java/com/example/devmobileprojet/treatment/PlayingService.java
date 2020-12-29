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

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.devmobileprojet.Musicplayer;
import com.example.devmobileprojet.R;
import com.example.devmobileprojet.dataclass.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class PlayingService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    private static final String TAG = "PLAYING SERVICE";

    // Attributs
    private String songTitle="";
    private static final int NOTIFY_ID=1;
    private MediaPlayer player;
    private ArrayList<Music> musiclist;
    private int musicposition;
    private final IBinder musicBind = new MusicBinder();
    private boolean shuffle=false;
    private Random rand;
    private Music playMusic;

    // Methods

    public PlayingService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicposition = 0;
        player = new MediaPlayer();
        initMusicPlayer();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onPrepared(MediaPlayer mp) {
        songTitle = playMusic.getTitle();
        //start playback
        Log.d(TAG, "onPrepared: ");
        mp.start();
        Log.d(TAG, "playing: ");

        Intent notIntent = new Intent(this, Musicplayer.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setTicker("En cours - ")
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    public void playMusic() throws IOException {
        player.reset();
        playMusic = musiclist.get(musicposition);
        long currentmusic = playMusic.getId();
        Uri trackuri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentmusic);
        Log.d(TAG, "playMusic: " + trackuri);
        try{
            player.setDataSource(getApplicationContext(), trackuri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Erreur dans la source de media", e);
        }
        Log.d("Service Player", "playMusic: "+ currentmusic + " - name : " + playMusic.getTitle());
        //player.setVolume( 0.5, 0.5);
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                Log.d(TAG, "playMusic: Ca joue !!!!!!!!!!!!!!!!!!!!");
                if(mp.isPlaying()){
                    Log.d(TAG, "playMusic: Ca joue vraiment !!!!!!!!!!!!!!!!!!!!");
                    //onPrepared(mp);

                    //Log.d(TAG, "notification qui marche");
                }
            }
        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition() < 0){
            mp.reset();
            try {
                playNext();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setShuffle(){
        if(shuffle){
            shuffle = false;
        }
        else {
            shuffle = true;
        }
    }

    public void playPrev() throws IOException {
        musicposition--;
        if(musicposition == 0){
            musicposition = musiclist.size()-1;
        }
        playMusic();
    }

    public void playNext() throws IOException {
        if(shuffle){
            int newmusic = musicposition;
            while(newmusic == musicposition){
                newmusic = rand.nextInt(musiclist.size());
            }
            musicposition = newmusic;
        }
        else{
            musicposition++;
            if(musicposition == musiclist.size()){
                musicposition = 0;
            }
        }
        playMusic();
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //player.setOnPreparedListener((MediaPlayer.OnPreparedListener) this);
        //player.setOnCompletionListener((MediaPlayer.OnCompletionListener) this);
        //player.setOnErrorListener((MediaPlayer.OnErrorListener) this);
    }

    public void setList(ArrayList<Music> musiclist){
        this.musiclist=musiclist;
    }

    public void setMusic(int musicIndex){
        musicposition=musicIndex;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    public class MusicBinder extends Binder {
        public PlayingService getService() {
            return PlayingService.this;
        }
    }

    // getters setters
    public void setMusicposition(int musicposition){
        this.musicposition = musicposition;
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public ArrayList<Music> getMusiclist() {
        return musiclist;
    }

    public void setMusiclist(ArrayList<Music> musiclist) {
        this.musiclist = musiclist;
    }

    public int getMusicposition() {
        return player.getCurrentPosition();
    }

    public int getDuration(){
        return player.getDuration();
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void startPlayer(){
        player.start();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public IBinder getMusicBind() {
        return musicBind;
    }
}