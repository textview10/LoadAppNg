package com.loadapp.load.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.loadapp.load.R;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.ui.home.HomeActivity;

/**
 * splash 
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_spalish);

        findViewById(R.id.btn_test_to_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            }
        });
    }
}
