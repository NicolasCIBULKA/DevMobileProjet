package com.example.devmobileprojet.dataclass;

/*
    Class for all the music of the application
    Will contain the main informations of the music
 */
public class Music {
    // Attributs
    private int idMusic;
    private String position;
    private int size;
    private float length;
    private String name;
    private String artist;
    // ---------------------
    // Methods
    // ---------------------

    // Construstor
    public Music(int idMusic, String position, int size, float length, String name, String artist){
        this.idMusic = idMusic;
        this.position = position;
        this.size = size;
        this.length = length;
        this.name = name;
        this.artist = artist;
    }

    // getters setters

    public int getIdMusic() {
        return idMusic;
    }

    public void setIdMusic(int idMusic) {
        this.idMusic = idMusic;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }


}
