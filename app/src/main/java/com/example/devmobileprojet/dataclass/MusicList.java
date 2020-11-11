package com.example.devmobileprojet.dataclass;
/*

 */
import java.util.ArrayList;

public class MusicList {
    // Attributs

    private ArrayList<Music> musiclist;
    private int musicnumber;
    private int length;

    // ---------------------
    // Methods
    // ---------------------

    // Constructors

    public MusicList(){
        this.musicnumber = 0;
        this.length = 0;
        this.musiclist = new ArrayList<Music>();
    }

    // getters and setters

    public ArrayList<Music> getMusiclist() {
        return musiclist;
    }

    public void setMusiclist(ArrayList<Music> musiclist) {
        this.musiclist = musiclist;
    }

    public int getMusicnumber() {
        return musicnumber;
    }

    public void setMusicnumber(int musicnumber) {
        this.musicnumber = musicnumber;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
