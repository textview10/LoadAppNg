package com.loadapp.load.collect;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loadapp.load.api.Api;
import com.loadapp.load.global.Constant;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FireBaseMgr {

    private static final String TAG = "FireBaseMgr";

    private static FireBaseMgr mManager;

    private FireBaseMgr() {

    }

    public static FireBaseMgr getInstance() {
        if (mManager == null) {
            synchronized (LocationMgr.class) {
                if (mManager == null) {
                    mManager = new FireBaseMgr();
                }
            }
        }
        return mManager;
    }

    public void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Constant.mFirebaseToken = token;
                    }
                });
    }

    public void reportFcmToken(Context context) {
        if (TextUtils.isEmpty(Constant.mFirebaseToken)){
            return;
        }
        if (!Constant.isNewToken){
            return;
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Constant.mAdvertId = getGoogleAdId(context);
                    uploadInfo();
                } catch (Exception e){

                }
            }
        }.start();
    }

    private void uploadInfo() {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            jsonObject.put("access_token", Constant.mToken);
            jsonObject.put("fcm_token", Constant.mFirebaseToken);
            jsonObject.put("google_advertising_id", Constant.mAdvertId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<String>post(Api.UPLOAD_FCM_TOKEN).tag(TAG).headers("token", Constant.mToken).params("fcmToken", Constant.mFirebaseToken)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, " report fcm token success = " + response.body().toString());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, " report fcm token failure = ");
                    }
                });
    }

    public static String getGoogleAdId(Context context) throws Exception {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            return "Cannot call in the main thread, You must call in the other thread";
        }
        AdvertisingIdClient.Info idInfo = null;
        try {
            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        String adid = null;
        try {
            if (idInfo != null) {
                adid = idInfo.getId();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return adid;
    }
}
