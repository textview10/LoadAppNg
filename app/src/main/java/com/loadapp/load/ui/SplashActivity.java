package com.loadapp.load.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.bean.ServerLiveBean;
import com.loadapp.load.ui.home.HomeActivity;
import com.loadapp.load.ui.login.SignInActivity;
import com.loadapp.load.ui.login.SignUpActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * splash 
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";

    private RelativeLayout rlContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        rlContainer = findViewById(R.id.rl_welcome_container);
        findViewById(R.id.rl_welcome_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkServerAvailable(new CallBack() {
                    @Override
                    public void onEnd() {
                        startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                        finish();
                    }
                });
            }
        });
        findViewById(R.id.rl_welcome_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkServerAvailable(new CallBack() {
                    @Override
                    public void onEnd() {
                        startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
                        finish();
                    }
                });
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

    private void checkServerAvailable(CallBack callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request_time", System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.e(TAG, " = " + jsonObject.toString());
        OkGo.<String>post(Api.CHECK_SERVER_ALIVE).tag(TAG)
                .params("data",jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ServerLiveBean serverLiveBean = checkResponseSuccess(response, ServerLiveBean.class);
                        if (serverLiveBean != null && serverLiveBean.isServer_live()){
                            Log.d(TAG, "the server is alive");
                        }
                        if (callBack != null){
                            callBack.onEnd();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "the server is not alive");
                        ToastUtils.showShort("server is not alive .");
                    }
                });

    }

    public interface CallBack {
        void onEnd();
    }
}
