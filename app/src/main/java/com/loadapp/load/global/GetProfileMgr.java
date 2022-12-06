package com.loadapp.load.global;

import android.util.Log;

import com.loadapp.load.api.Api;
import com.loadapp.load.bean.AccountProfileBean;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.loadapp.load.util.CheckResponseUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class GetProfileMgr {

    private static final String TAG = "GetProfileMgr";

    private static AccountProfileBean.AccountProfile mProfileBean;

    private HashSet<Observer> mSet = new HashSet();
    private static GetProfileMgr instance;

    public static GetProfileMgr getInstance() {
        if (instance == null) {
            instance = new GetProfileMgr();
        }
        return instance;
    }

    public void requestProfile() {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.GET_PROFILE).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        AccountProfileBean profileBean = CheckResponseUtils.checkResponseSuccess(response, AccountProfileBean.class);
                        if (profileBean == null || profileBean.getAccount_profile() == null) {
                            Log.e(TAG, " get profile error ." + response.body());
                            return;
                        }
                        mProfileBean = profileBean.getAccount_profile();
                        onExecuteGetData(mProfileBean);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "get profile failure = " + response.body());
                    }
                });
    }

    public void getProfile(Observer observer) {
        if (mProfileBean != null) {
            if (observer != null) {
                observer.onGetData(mProfileBean);
            }
            return;
        }
        requestProfile();
    }

    private void onExecuteGetData(AccountProfileBean.AccountProfile profile){
        Iterator<Observer> iterator = mSet.iterator();
        while (iterator.hasNext()){
            Observer observer = iterator.next();
            if (observer != null){
                observer.onGetData(profile);
            }
        }
    }

    public interface Observer {
        void onGetData(AccountProfileBean.AccountProfile profileBean);
    }

    public void addObserver(Observer observer){
        mSet.add(observer);
    }

    public void removeObserver(Observer observer){
        mSet.remove(observer);
    }
}
