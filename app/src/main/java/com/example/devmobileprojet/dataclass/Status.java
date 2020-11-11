package com.example.devmobileprojet.dataclass;

public class Status {
    // Attributs

    private boolean continuousPlaying;
    private boolean circularPlaying;
    private boolean randomizePlaying;

    // ---------------------
    // Methods
    // ---------------------

    // Constructor

    public Status(){
        this.continuousPlaying = false;
        this.circularPlaying = false;
        this.randomizePlaying = false;
    }

    // Getters and setters

    public boolean isContinuousPlaying() {
        return continuousPlaying;
    }

    public void setContinuousPlaying(boolean continuousPlaying) {
        this.continuousPlaying = continuousPlaying;
    }

    public boolean isCircularPlaying() {
        return circularPlaying;
    }

    public void setCircularPlaying(boolean circularPlaying) {
        this.circularPlaying = circularPlaying;
    }

    public boolean isRandomizePlaying() {
        return randomizePlaying;
    }

    public void setRandomizePlaying(boolean randomizePlaying) {
        this.randomizePlaying = randomizePlaying;
    }
}
