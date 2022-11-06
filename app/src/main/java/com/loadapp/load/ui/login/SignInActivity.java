package com.loadapp.load.ui.login;

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
        login("18518511461", "aa123456");
    }

    private void login(String phoneNum, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request_time", System.currentTimeMillis());
            jsonObject.put("mobile", phoneNum);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.LOGIN).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LoginResponseBean loginBean = checkResponseSuccess(response, LoginResponseBean.class);
                        if (loginBean == null) {
                            Log.e(TAG,"login error");
                            return;
                        }
                        Constant.mAccountId = loginBean.getAccount_id();
                        Constant.mToken = loginBean.getAccess_token();
                        Log.e(TAG, "login success = " + response.body().toString());
                        modifyPassword("aa123456", "ab123456", "ab123456");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    private void modifyPassword(String oldPwd, String newPwd, String confirmPwd){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request_time", System.currentTimeMillis());
            jsonObject.put("account_id", Constant.mAccountId);
            jsonObject.put("access_token", Constant.mToken);
            jsonObject.put("password_old", oldPwd);
            jsonObject.put("password_new", newPwd);
            jsonObject.put("password_confirm", confirmPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.MODIFY_PSD).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = checkResponseSuccess(response);
                        if (TextUtils.isEmpty(responseStr)) {
                            Log.e(TAG,"modify password error");
                            return;
                        }

                        Log.e(TAG, "modify password success = " + response.body().toString());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }
}
