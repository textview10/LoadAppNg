package com.loadapp.load.ui.login.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseFragment;
import com.loadapp.load.global.Constant;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyPwdFragment extends BaseFragment {
    private static final String TAG = "ModifyPwdFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_pwd, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
