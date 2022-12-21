package com.loadapp.load.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.loadapp.load.BuildConfig;
import com.loadapp.load.bean.BaseResponseBean;
import com.lzy.okgo.model.Response;

public class CheckResponseUtils {

//    public static final Gson gson = new GsonBuilder()
//////             # 将DEFAULT改为STRING
//            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
//            .create();
    ;

    public static <T> T checkResponseSuccess(Response<String> response, Class<T> clazz) {
        String body = checkResponseSuccess(response);
        if (TextUtils.isEmpty(body)) {
            return null;
        }
        return JSONObject.parseObject(body, clazz);
    }

    public static String checkResponseSuccess(Response<String> response) {
        BaseResponseBean responseBean = null;
        try {
            responseBean = JSONObject.parseObject(response.body().toString(), BaseResponseBean.class);
        }catch (Exception e){
            if (BuildConfig.DEBUG){
                throw e;
            }
        }
//        BaseResponseBean responseBean = gson.fromJson(response.body().toString(), BaseResponseBean.class);
        if (responseBean == null) {
            ToastUtils.showShort("request failure.");
            return null;
        }
        if (!responseBean.isRequestSuccess()) {
            ToastUtils.showShort(responseBean.getMessage());
            return null;
        }
        if (responseBean.getData() == null) {
            ToastUtils.showShort("request failure 2.");
            return null;
        }
        return JSONObject.toJSONString(responseBean.getData());
    }
}
