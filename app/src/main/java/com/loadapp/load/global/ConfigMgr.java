package com.loadapp.load.global;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.google.gson.Gson;
import com.loadapp.load.api.Api;
import com.loadapp.load.bean.BaseConfigBean;
import com.loadapp.load.bean.BaseResponseBean;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ConfigMgr {
    private static final String TAG = "ConfigMgr";

    public final static HashSet<Pair<String, String>> mMonthSalaryList = new HashSet<>();
    public final static HashSet<Pair<String, String>> mRelativeShipList = new HashSet<>();
    public final static HashSet<Pair<String, String>> mWorkingYearList = new HashSet<>();
    public final static HashMap<String, HashSet<String>> mStateCityList = new HashMap<>();

    public static void getAllConfig() {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("access_token", Constant.mToken);
            jsonObject.put("account_id", Constant.mAccountId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.GET_CONFIG).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        BaseResponseBean responseBean = gson.fromJson(response.body(), BaseResponseBean.class);
                        if (responseBean != null && responseBean.isRequestSuccess()) {
                            ConfigBean configBean = gson.fromJson(responseBean.getBodyStr(), ConfigBean.class);
                            if (configBean != null) {
                                mMonthSalaryList.clear();
                                mMonthSalaryList.addAll(parseItem(configBean.month_salary));
                                mRelativeShipList.clear();
                                mRelativeShipList.addAll(parseItem(configBean.relationship));
                                mWorkingYearList.clear();
                                mWorkingYearList.addAll(parseItem(configBean.working_year));
                                mStateCityList.clear();
                                mStateCityList.putAll(parseCityItem(configBean.state_city));
                                Log.i(TAG, " get config success .");
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "get config failure = " + response.body());
                    }
                });
    }

    private static HashSet<Pair<String, String>> parseItem(Object obj) {
        HashSet<Pair<String, String>> set = new HashSet<>();
        try {
            JSONObject jsonObject = new JSONObject(GsonUtils.toJson(obj));
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String str = jsonObject.optString(key);
                if (!TextUtils.isEmpty(str)) {
                    set.add(new Pair<>(str, key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return set;
    }

    private static HashMap<String, HashSet<String>> parseCityItem(Object obj) {
        HashMap<String, HashSet<String>> map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(GsonUtils.toJson(obj));
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String cityKey = iterator.next();
                JSONArray array = jsonObject.optJSONArray(cityKey);
                HashSet<String> set = new HashSet<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject temp = array.optJSONObject(i);
                    String city = temp.optString("City");
                    set.add(city);
                }
                map.put(cityKey, set);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static class ConfigBean {
        //月收入
        private Object month_salary;
        //联系人关系
        private Object relationship;
        //工作年限
        private Object working_year;
        //所在州，市
        private Object state_city;
        //返回时间
        private Object server_time;

        public Object getMonth_salary() {
            return month_salary;
        }

        public void setMonth_salary(Object month_salary) {
            this.month_salary = month_salary;
        }

        public Object getRelationship() {
            return relationship;
        }

        public void setRelationship(Object relationship) {
            this.relationship = relationship;
        }

        public Object getWorking_year() {
            return working_year;
        }

        public void setWorking_year(JSONObject working_year) {
            this.working_year = working_year;
        }

        public Object getState_city() {
            return state_city;
        }

        public void setState_city(Object state_city) {
            this.state_city = state_city;
        }

        public Object getServer_time() {
            return server_time;
        }

        public void setServer_time(Object server_time) {
            this.server_time = server_time;
        }
    }
}
