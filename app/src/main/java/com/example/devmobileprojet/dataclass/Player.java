package com.example.devmobileprojet.dataclass;

public class Player {
    // Attributs

    private boolean playing;
    private  Music currentMusic;
    private MusicList currentList;
    private Status playerStatus;

    // ---------------------
    // Methods
    // ---------------------

    // Constructor

    public Player(){
        this.playing = false;
        this.currentMusic = null;
        this.currentList = new MusicList();
        this.playerStatus = new Status();
    }

    // getters and setters

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public void setCurrentMusic(Music currentMusic) {
        this.currentMusic = currentMusic;
    }

    public MusicList getCurrentList() {
        return currentList;
    }

    public void setCurrentList(MusicList currentList) {
        this.currentList = currentList;
    }

    public Status getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(Status playerStatus) {
        this.playerStatus = playerStatus;
    }
}
