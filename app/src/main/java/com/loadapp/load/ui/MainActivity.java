package com.loadapp.load.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.loadapp.load.R;
import com.loadapp.load.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}