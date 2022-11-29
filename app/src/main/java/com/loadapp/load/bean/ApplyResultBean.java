package com.loadapp.load.bean;

public class ApplyResultBean {

    private long order_id;
    private int order_create;
    private long server_time;

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public int getOrder_create() {
        return order_create;
    }

    public void setOrder_create(int order_create) {
        this.order_create = order_create;
    }

    public long getServer_time() {
        return server_time;
    }

    public void setServer_time(long server_time) {
        this.server_time = server_time;
    }
}
