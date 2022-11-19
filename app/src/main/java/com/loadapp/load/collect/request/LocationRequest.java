package com.loadapp.load.collect.request;

import java.util.List;

public class LocationRequest {
    public double gpsLongitude;
    public double gpsLatitude;
    public double netWorkLongitude;
    public double netWorkLatitude;
    public String extra;

    public List<String> bssid;
}
