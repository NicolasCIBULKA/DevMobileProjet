package com.example.devmobileprojet.treatment;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import com.example.devmobileprojet.dataclass.Music;
import java.util.ArrayList;


public class PlayingService extends Service {
    // TODO TODO TODO
    private MediaPlayer player;
    private ArrayList<Music> musiclist;
    private int musicposition;
    private final IBinder musicBind = new MusicBinder();
    public PlayingService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicposition = 0;
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
    }

    @Override
    public void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }

    public void playSong(){
        player.reset();
        Music playMusic = musiclist.get(musicposition);
        long currentmusic = playMusic.getId();
        Uri trackuri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentmusic);
        try{
            player.setDataSource(getApplicationContext(), trackuri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Erreur dans la source de media", e);
        }
        player.prepareAsync();
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener((MediaPlayer.OnPreparedListener) this);
        player.setOnCompletionListener((MediaPlayer.OnCompletionListener) this);
        player.setOnErrorListener((MediaPlayer.OnErrorListener) this);
    }

    public void setList(ArrayList<Music> musiclist){
        this.musiclist=musiclist;
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
        PlayingService getService() {
            return PlayingService.this;
        }
    }

    // getters setters
    public void setMusicposition(int musicposition){
        this.musicposition = musicposition;
    }


}