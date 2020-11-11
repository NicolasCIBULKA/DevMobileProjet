package com.example.devmobileprojet.dataclass;

public class Playlist extends MusicList {
    // Attributs

    private String playlistname;
    private String description;

    // ---------------------
    // Methods
    // ---------------------

    // Constructor

    public Playlist(){
        this.playlistname = "";
        this.description = "";
    }

    // getters setters

    public String getPlaylistname() {
        return playlistname;
    }

    public void setPlaylistname(String playlistname) {
        this.playlistname = playlistname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
