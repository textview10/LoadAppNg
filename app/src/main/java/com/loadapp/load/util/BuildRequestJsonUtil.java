package com.loadapp.load.util;

import com.loadapp.load.global.Constant;

import org.json.JSONException;
import org.json.JSONObject;

public class BuildRequestJsonUtil {

    public static JSONObject buildRequestJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request_time", System.currentTimeMillis());
            jsonObject.put("access_token", Constant.mToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return jsonObject;
    }
}
