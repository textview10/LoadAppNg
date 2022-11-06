package com.loadapp.load.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
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

public class SignUpActivity extends BaseActivity {
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        checkPhoneNumAvailable("18518511461");
//        sendSmsCode("18518511461");
//        verifySmsCode("18518511461", "6666");
        register("18518511461","aa123456");
    }

    private void checkPhoneNumAvailable(String mobile){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request_time", System.currentTimeMillis());
            jsonObject.put("mobile", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.CHECK_MOBILE).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String responseStr = checkResponseSuccess(response);
                if (TextUtils.isEmpty(responseStr)) {
                    ToastUtils.showShort("mobile num is not correct.");
                    return;
                }
                Log.e(TAG, "check mobile response  = " + response.body().toString());
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }

    private void sendSmsCode(String phoneNum){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request_time", System.currentTimeMillis());
            jsonObject.put("mobile", phoneNum);
            //1是注册
            jsonObject.put("send_type", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.GET_SMS_CODE).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = checkResponseSuccess(response);
                        if (TextUtils.isEmpty(responseStr)) {
                            Log.e(TAG,"send sms code error");
                            return;
                        }
                        Log.e(TAG, "send sms success = " + response.body().toString());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    private void verifySmsCode(String phoneNum, String verifyCode){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request_time", System.currentTimeMillis());
            jsonObject.put("mobile", phoneNum);
            //测试验证码：6666
            jsonObject.put("auth_code", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.CHECK_SMS_CODE).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = checkResponseSuccess(response);
                        if (TextUtils.isEmpty(responseStr)) {
                            Log.e(TAG,"verify sms code error");
                            return;
                        }
                        Log.e(TAG, "verify sms success = " + response.body().toString());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    private void register(String phoneNum, String password){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request_time", System.currentTimeMillis());
            jsonObject.put("mobile", phoneNum);
            //测试验证码：6666
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.REGISTER).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LoginResponseBean loginBean = checkResponseSuccess(response, LoginResponseBean.class);
                        if (loginBean == null) {
                            Log.e(TAG,"register error");
                            return;
                        }
                        Constant.mAccountId = loginBean.getAccount_id();
                        Constant.mToken = loginBean.getAccess_token();
                        Log.e(TAG, "register success = " + response.body().toString());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }
}
