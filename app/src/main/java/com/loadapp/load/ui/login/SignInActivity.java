package com.loadapp.load.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.bean.LoginResponseBean;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.SplashActivity;
import com.loadapp.load.ui.home.HomeActivity;
import com.loadapp.load.ui.login.fragment.InputPhoneNumFragment;
import com.loadapp.load.ui.login.fragment.LoginFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends BaseActivity {
    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        LoginFragment loginFragment = new LoginFragment();
        toFragment(loginFragment);
    }

    @Override
    protected int getFragmentContainerRes() {
        return R.id.fl_signin_container;
    }

    public void toHomePage(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void backPress(){
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backPress();
    }
}
