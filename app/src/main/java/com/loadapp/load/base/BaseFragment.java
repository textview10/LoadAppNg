package com.loadapp.load.base;

import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.reflect.TypeToken;
import com.loadapp.load.bean.BaseResponseBean;
import com.lzy.okgo.model.Response;

import java.math.BigDecimal;
import java.util.Map;


public abstract class BaseFragment extends Fragment {
    protected final Gson gson = new GsonBuilder()
////             # 将DEFAULT改为STRING
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .create();;

    protected <T> T checkResponseSuccess(Response<String> response, Class<T> clazz) {
        String body = checkResponseSuccess(response);
        if (TextUtils.isEmpty(body)){
            return null;
        }
        return JSONObject.parseObject(body, clazz);
    }

    protected String checkResponseSuccess(Response<String> response) {
        BaseResponseBean responseBean =  JSONObject.parseObject(response.body().toString(), BaseResponseBean.class);
//        BaseResponseBean responseBean = gson.fromJson(response.body().toString(), BaseResponseBean.class);
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
        return gson.toJson(responseBean.getData());
    }

    private static long lastClickMillions;
    protected boolean checkClickFast(){
        long delay = (System.currentTimeMillis() - lastClickMillions);
        if ( delay < 3000){
            ToastUtils.showShort("click too fast. please wait a monment");
            return true;
        }
        lastClickMillions = System.currentTimeMillis();
        return false;
    }

}
