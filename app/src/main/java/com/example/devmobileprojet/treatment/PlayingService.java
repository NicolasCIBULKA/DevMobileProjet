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
import java.util.Random;


public class PlayingService extends Service {

    // Attributs

    private MediaPlayer player;
    private ArrayList<Music> musiclist;
    private int musicposition;
    private final IBinder musicBind = new MusicBinder();
    private boolean shuffle=false;
    private Random rand;

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

    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
    }

    public void playMusic(){
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

    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition() < 0){
            mp.reset();
            playNext();
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

    public void playPrev(){
        musicposition--;
        if(musicposition == 0){
            musicposition = musiclist.size()-1;
        }
        playMusic();
    }

    public void playNext(){
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
        player.setOnPreparedListener((MediaPlayer.OnPreparedListener) this);
        player.setOnCompletionListener((MediaPlayer.OnCompletionListener) this);
        player.setOnErrorListener((MediaPlayer.OnErrorListener) this);
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