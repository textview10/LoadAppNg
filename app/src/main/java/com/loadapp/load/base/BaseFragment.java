package com.loadapp.load.base;

import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.loadapp.load.bean.BaseResponseBean;
import com.lzy.okgo.model.Response;

public abstract class BaseFragment extends Fragment {
    protected final Gson gson = new Gson();

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

    private static long lastClickMillions;
    protected boolean checkClickFast(){
        long delay = (System.currentTimeMillis() - lastClickMillions);
        if ( delay < 4000){
            ToastUtils.showShort("click too fast. please wait a monment");
            return true;
        }
        lastClickMillions = System.currentTimeMillis();
        return false;
    }

}
