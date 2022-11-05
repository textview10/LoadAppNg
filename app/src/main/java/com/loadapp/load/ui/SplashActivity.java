package com.loadapp.load.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.loadapp.load.R;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.ui.home.HomeActivity;
import com.loadapp.load.ui.login.SignInActivity;
import com.loadapp.load.ui.login.SignUpActivity;

/**
 * splash 
 */
public class SplashActivity extends BaseActivity {

    private RelativeLayout rlContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        rlContainer = findViewById(R.id.rl_welcome_container);
        findViewById(R.id.rl_welcome_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            }
        });
        findViewById(R.id.rl_welcome_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
            }
        });

        Button btn = new Button(SplashActivity.this);
        btn.setText("测试进入首页");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            }
        });
        rlContainer.addView(btn);
    }
}
