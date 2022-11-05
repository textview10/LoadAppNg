package com.loadapp.load.bean;

public class ServerLiveBean {
    private boolean server_live;
    private long server_time;

    public boolean isServer_live() {
        return server_live;
    }

    public void setServer_live(boolean server_live) {
        this.server_live = server_live;
    }

    public long getServer_time() {
        return server_time;
    }

    public void setServer_time(long server_time) {
        this.server_time = server_time;
    }
}
