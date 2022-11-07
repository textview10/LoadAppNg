package com.loadapp.load.base;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.loadapp.load.R;
import com.loadapp.load.bean.BaseResponseBean;
import com.loadapp.load.global.AppManager;
import com.lzy.okgo.model.Response;

public abstract class BaseActivity extends AppCompatActivity {

    protected final Gson gson = new Gson();

    public void toFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        transaction.replace(getFragmentContainerRes(), fragment);
        transaction.commitAllowingStateLoss();
    }

    protected @IdRes
    int getFragmentContainerRes() {
        return R.id.fl_sign_up_container;
    }

    protected <T> T checkResponseSuccess(Response<String> response, Class<T> clazz) {
        String body = checkResponseSuccess(response);
        if (TextUtils.isEmpty(body)){
            return null;
        }
        return gson.fromJson(body, clazz);
    }

    protected String checkResponseSuccess(Response<String> response) {
        BaseResponseBean responseBean = gson.fromJson(response.body().toString(), BaseResponseBean.class);
        if (responseBean == null) {
            ToastUtils.showShort("request failure.");
            return null;
        }
        if (!responseBean.isRequestSuccess()) {
            ToastUtils.showShort(responseBean.getMessage());
            return null;
        }
        if (responseBean.getData() == null){
            ToastUtils.showShort("request failure 2.");
            return null;
        }
        return responseBean.getBodyStr();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppManager.getAppManager().addActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }
}
