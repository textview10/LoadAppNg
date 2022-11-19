package com.loadapp.load.collect.request;

public class AppInfoRequest {
    public String name;
    public String packageName;
    public long it; // INSTALL TIME
    public long lu; // LAST UPDATE TIME
    public int type;    //0 系统app, 1第三方app

}
