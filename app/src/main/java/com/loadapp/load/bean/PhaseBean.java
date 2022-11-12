package com.loadapp.load.bean;

public class PhaseBean {

    private int current_phase;
    private int next_phase;
    private int progress;
    private int server_time;

    public int getCurrent_phase() {
        return current_phase;
    }

    public void setCurrent_phase(int current_phase) {
        this.current_phase = current_phase;
    }

    public int getNext_phase() {
        return next_phase;
    }

    public void setNext_phase(int next_phase) {
        this.next_phase = next_phase;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getServer_time() {
        return server_time;
    }

    public void setServer_time(int server_time) {
        this.server_time = server_time;
    }
}
