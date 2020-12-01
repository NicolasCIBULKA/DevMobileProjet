package com.example.devmobileprojet.treatment;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.devmobileprojet.dataclass.MusicList;
import com.example.devmobileprojet.dataclass.Player;
import com.example.devmobileprojet.dataclass.Status;

public class MusicPlayer {

    //Attributs
    private MediaPlayer musicplayer;
    private Player player;
    private Status status;
    private MusicList list;
    private DataBaseHandler dbhandler;
    // Methods

    public MusicPlayer(Context context){
        this.musicplayer = new MediaPlayer();
        this.player = new Player(musicplayer);
        this.status = new Status();
        this.list = new MusicList();
        this.dbhandler = new DataBaseHandler(context);
    }

    public void chargePlaylist(int idPlaylist){

    }

    // METHODS TO DO
    // nextMusic
    // PreviousMusic
    // chargeMusic
    // Play
    // Pause
    // randomizePlaying
    // OrderPlaying
    // TriggerRandom
    // TriggerCircular


}
