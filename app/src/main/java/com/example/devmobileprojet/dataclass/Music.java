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
    private int duration;

    // ---------------------
    // Methods
    // ---------------------

    // Construstor
    public Music(long id, String title, String artist){
        this.id = id;
        this.title = title;
        this.artist = artist;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
