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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ConfigMgr {
    private static final String TAG = "ConfigMgr";

    public final static ArrayList<Pair<String, String>> mMonthSalaryList = new ArrayList<>();
    public final static ArrayList<Pair<String, String>> mRelativeShipList = new ArrayList<>();
    public final static ArrayList<Pair<String, String>> mWorkingYearList = new ArrayList<>();
    public final static HashMap<String, ArrayList<String>> mStateCityList = new HashMap<>();

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

    private static ArrayList<Pair<String, String>> parseItem(Object obj) {
        ArrayList<Pair<String, String>> list = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(GsonUtils.toJson(obj));
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String str = jsonObject.optString(key);
                if (!TextUtils.isEmpty(str)) {
                    list.add(new Pair<>(str, key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(list, new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> t1, Pair<String, String> t2) {
                String first = t1.second;
                String second = t2.second;
                int firstInt = 0;
                int secondInt = 0;
                try {
                    if (!TextUtils.isEmpty(first)) {
                        firstInt = Integer.parseInt(first);
                    }
                    if (!TextUtils.isEmpty(second)) {
                        secondInt = Integer.parseInt(second);
                    }
                } catch (Exception e){

                }
                return firstInt - secondInt;
            }
        });
        return list;
    }

    private static HashMap<String, ArrayList<String>> parseCityItem(Object obj) {
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(GsonUtils.toJson(obj));
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String cityKey = iterator.next();
                JSONArray array = jsonObject.optJSONArray(cityKey);
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject temp = array.optJSONObject(i);
                    String city = temp.optString("City");
                    list.add(city);
                }
                map.put(cityKey, list);
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
