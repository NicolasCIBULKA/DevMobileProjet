package com.example.devmobileprojet.dataclass;

public class Playlist extends MusicList {
    // Attributs

    private String playlistname;
    private String description;
    private int idPlaylist;
    // ---------------------
    // Methods
    // ---------------------

    // Constructor

    public Playlist(int idPlaylist){
        this.idPlaylist = idPlaylist;
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

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }
}
