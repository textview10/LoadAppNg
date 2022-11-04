package com.loadapp.load.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.loadapp.load.R;
import com.loadapp.load.base.BaseActivity;

/**
 * splash 
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
