package com.loadapp.load.ui.login;

import android.content.Intent;
import android.os.Bundle;
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
import com.loadapp.load.ui.login.fragment.SetPwdFragment;
import com.loadapp.load.ui.login.fragment.VerifySmsCodeFragment;
import com.loadapp.load.ui.webview.AgreeTermFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends BaseActivity {
    private static final String TAG = "SignUpActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        verifySmsCode("18518511461", "6666");
//        register("18518511461","aa123456");
        AgreeTermFragment agreeTermFragment = new AgreeTermFragment();
        toFragment(agreeTermFragment);
    }

    @Override
    protected int getFragmentContainerRes() {
        return R.id.fl_sign_up_container;
    }

    public void toInputPhoneNum(){
        InputPhoneNumFragment inputPhoneNumFragment = new InputPhoneNumFragment();
        toFragment(inputPhoneNumFragment);
    }

    public void toVerifySmsCodePage(String phoneNum){
        VerifySmsCodeFragment verifySmsCodeFragment = new VerifySmsCodeFragment();
        verifySmsCodeFragment.setPhoneNum(phoneNum);
        toFragment(verifySmsCodeFragment);
    }

    public void toSetPwdPage(String phoneNum){
        SetPwdFragment setPwdFragment = new SetPwdFragment();
        setPwdFragment.setPhoneNum(phoneNum);
        toFragment(setPwdFragment);
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
//        super.onBackPressed();
        backPress();
    }
}
