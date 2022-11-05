package com.loadapp.load.ui.login;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseActivity;
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

//        testCheckPhoneNum();
    }

    private void testCheckPhoneNum() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.CHECK_MOBILE).tag(TAG)
                .params("mobile", System.currentTimeMillis())
                .upJson(jsonObject).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e(TAG, "response 1 = " + response.body().toString());
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }


}
