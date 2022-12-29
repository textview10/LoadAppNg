package com.loadapp.load.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.BarUtils;
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
import com.loadapp.load.ui.webview.WebViewFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends BaseActivity {
    private static final String TAG = "SignUpActivity";
    private WebViewFragment webViewFragment;
    private FrameLayout flWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarVisibility(this, false);
        setContentView(R.layout.activity_sign_up);
        flWebView = findViewById(R.id.fl_sign_up_container2);
//        verifySmsCode("18518511461", "6666");
//        register("18518511461","aa123456");
        AgreeTermFragment agreeTermFragment = new AgreeTermFragment();
        toFragment(agreeTermFragment);
    }

    @Override
    protected int getFragmentContainerRes() {
        return R.id.fl_sign_up_container;
    }

    public void toInputPhoneNum() {
        InputPhoneNumFragment inputPhoneNumFragment = new InputPhoneNumFragment();
        toFragment(inputPhoneNumFragment);
    }

    public void toVerifySmsCodePage(String phoneNum) {
        VerifySmsCodeFragment verifySmsCodeFragment = new VerifySmsCodeFragment();
        verifySmsCodeFragment.setPhoneNum(phoneNum);
        toFragment(verifySmsCodeFragment);
    }

    public void toSetPwdPage(String phoneNum) {
        SetPwdFragment setPwdFragment = new SetPwdFragment();
        setPwdFragment.setPhoneNum(phoneNum);
        toFragment(setPwdFragment);
    }

    public void toHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void toWebView() {
        flWebView.setVisibility(View.VISIBLE);
        if (webViewFragment == null) {
            webViewFragment = new WebViewFragment();
        }
        webViewFragment.setUrl(Api.WEB_VIEW_POLICY);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        transaction.replace(R.id.fl_sign_up_container2, webViewFragment);
        transaction.commitAllowingStateLoss();
    }

    public void backPress() {
        if (flWebView != null) {
            if (flWebView.getVisibility() == View.VISIBLE) {
                flWebView.setVisibility(View.GONE);
                return;
            }
        }
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
