package com.example.devmobileprojet.dataclass;

/*
    Class for all the music of the application
    Will contain the main informations of the music
 */
public class Music {
    // Attributs
    private long id;
    private String title;
    private String artist;
    private String duration;
    private String position;
    // ---------------------
    // Methods
    // ---------------------

    // Construstor
    public Music(long id, String title, String artist, String position, String duration){
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.position = position;
        this.duration = duration;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    // getters setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}
