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
import com.loadapp.load.util.CheckResponseUtils;
import com.lzy.okgo.model.Response;

import java.math.BigDecimal;
import java.util.Map;


public abstract class BaseFragment extends Fragment {

    public static <T> T checkResponseSuccess(Response<String> response, Class<T> clazz) {
        return CheckResponseUtils.checkResponseSuccess(response, clazz);
    }

    public static String checkResponseSuccess(Response<String> response) {
        return CheckResponseUtils.checkResponseSuccess(response);
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
