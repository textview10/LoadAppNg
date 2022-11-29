package com.loadapp.load.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.bean.OrderInfoBean;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.SplashActivity;
import com.loadapp.load.ui.home.HomeActivity;
import com.loadapp.load.ui.login.fragment.LoginFragment;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.loadapp.load.util.CheckResponseUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class LauncherActivity extends BaseActivity {

    private static final String KEY_GUIDE = "key_guide";

    private static final String TAG = "LauncherActivity";

    private static final int TO_WELCOME_PAGE = 111;

    private static final int TO_MAIN_PAGE = 112;
    private long requestTime;

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case TO_WELCOME_PAGE:
                    if (mHandler != null) {
                        mHandler.removeCallbacksAndMessages(null);
                    }
                    OkGo.getInstance().cancelTag(TAG);
                    Intent welcomeIntent = new Intent(LauncherActivity.this, SplashActivity.class);
                    startActivity(welcomeIntent);
                    finish();
                    break;
                case TO_MAIN_PAGE:
                    Intent mainIntent = new Intent(LauncherActivity.this, HomeActivity.class);
                    startActivity(mainIntent);
                    finish();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
        setContentView(R.layout.activity_launcher);
//        boolean openGuide = SPUtils.getInstance().getBoolean(KEY_GUIDE, true);
//        boolean openGuide = false;
//        if (openGuide) {
//            SPUtils.getInstance().put(KEY_GUIDE, false);
//        }
        long accountId = SPUtils.getInstance().getLong(Constant.KEY_ACCOUNT_ID, 0);
        String token = SPUtils.getInstance().getString(Constant.KEY_TOKEN);
        if (accountId == 0 || TextUtils.isEmpty(token)) {
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(TO_WELCOME_PAGE, 1000);
            }
        } else {
            Constant.mAccountId = accountId;
            Constant.mToken = token;
            requestDetail();
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(TO_WELCOME_PAGE, 3000);
            }
        }
    }

    private void requestDetail() {
        requestTime = System.currentTimeMillis();
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            jsonObject.put("access_token", Constant.mToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.GET_ORDER_INFO).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        boolean successEnter = false;
                        String body = response.body();
                        if (!TextUtils.isEmpty(body)) {
                            OrderInfoBean orderInfo = CheckResponseUtils.checkResponseSuccess(response, OrderInfoBean.class);
                            if (orderInfo != null) {
                                Constant.mLaunchOrderInfo = orderInfo;
                                successEnter = true;
                            }
                        }
                        if (mHandler != null) {
                            mHandler.sendEmptyMessage(successEnter ? TO_MAIN_PAGE : TO_WELCOME_PAGE);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        if (mHandler != null) {
                            mHandler.sendEmptyMessage(TO_WELCOME_PAGE);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
