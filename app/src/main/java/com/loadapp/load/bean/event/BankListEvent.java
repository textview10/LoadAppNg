package com.loadapp.load.bean.event;


import android.util.Pair;

public class BankListEvent {
    private Pair<String, String> mData;

    public BankListEvent(Pair<String, String> mData) {
        this.mData = mData;
    }

    public Pair<String, String> getData() {
        return mData;
    }

}
