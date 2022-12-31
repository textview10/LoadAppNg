package com.loadapp.load.ui.profile.fragment;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.bean.AccountProfileBean;
import com.loadapp.load.bean.ContactBean;
import com.loadapp.load.bean.PhaseBean;
import com.loadapp.load.dialog.CustomDialog;
import com.loadapp.load.dialog.SelectDataDialog;
import com.loadapp.load.dialog.contact.SettingContactAdapter;
import com.loadapp.load.global.ConfigMgr;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.profile.CommitProfileActivity;
import com.loadapp.load.ui.profile.widget.EditSelectContainer;
import com.loadapp.load.ui.profile.widget.EditTextContainer;
import com.loadapp.load.ui.profile.widget.SelectContainer;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class PersonProfile2Fragment extends BaseCommitFragment {
    private static final String TAG = "PersonProfile2Fragment";

    private FrameLayout flCommit;
    private SelectContainer selectRelationShip1, selectRelationShip2;
    private SelectContainer editSelectMobile1, editSelectMobile2;
    private SelectContainer editTextName1 ,editTextName2;

    private static final int TYPE_SELECT_RELATIVE_1 = 114;
    private static final int TYPE_SELECT_RELATIVE_2 = 15;
    private static final int TYPE_SELECT_PHONE_1 = 117;
    private static final int TYPE_SELECT_PHONE_2 = 118;

    //本地联系人
    private final ArrayList<ContactBean> mContactList = new ArrayList<>();

    private Pair<String, String> mRelativeShip1;
    private Pair<String, String> mRelativeShip2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_profile2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flCommit = view.findViewById(R.id.fl_profile2_commit);
        selectRelationShip1 = view.findViewById(R.id.select_container_profile2_relationship1);
        editSelectMobile1 = view.findViewById(R.id.edittext_container_profile2_mobile1);
        editTextName1 = view.findViewById(R.id.edittext_container_profile2_name1);

        selectRelationShip2 = view.findViewById(R.id.select_container_profile2_relationship2);
        editSelectMobile2 = view.findViewById(R.id.edittext_container_profile2_mobile2);
        editTextName2 = view.findViewById(R.id.edittext_container_profile2_name2);
        initListener();
        initializePermission();
    }

    private void initListener() {
        selectRelationShip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog(ConfigMgr.mRelativeShipList, new SelectDataDialog.Observer() {
                    @Override
                    public void onItemClick(Pair<String, String> content, int pos) {
                        mRelativeShip1 = content;
                        if (selectRelationShip1 != null) {
                            selectRelationShip1.setData(content.first);
                        }
                    }
                });
            }
        });
        selectRelationShip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog(ConfigMgr.mRelativeShipList, new SelectDataDialog.Observer() {
                    @Override
                    public void onItemClick(Pair<String, String> content, int pos) {
                        mRelativeShip2 = content;
                        if (selectRelationShip2 != null) {
                            selectRelationShip2.setData(content.first);
                        }
                    }
                });
            }
        });
        editSelectMobile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactDialog(TYPE_SELECT_PHONE_1);
            }
        });
        editTextName1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactDialog(TYPE_SELECT_PHONE_1);
            }
        });
        editSelectMobile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactDialog(TYPE_SELECT_PHONE_2);
            }
        });
        editTextName2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactDialog(TYPE_SELECT_PHONE_2);
            }
        });
//        editSelectMobile1.setOnBgClickListener(new EditSelectContainer.OnBgClickListener() {
//            @Override
//            public void onClick() {
//                showContactDialog(TYPE_SELECT_PHONE_1);
//            }
//        });
//        editSelectMobile2.setOnBgClickListener(new EditSelectContainer.OnBgClickListener() {
//            @Override
//            public void onClick() {
//                showContactDialog(TYPE_SELECT_PHONE_2);
//            }
//        });
        flCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCommitAvailable()){
                    uploadContact();
                }
            }
        });
    }

    private void initializePermission() {
        boolean hasReadContactPermission = PermissionUtils.isGranted(Manifest.permission.READ_CONTACTS);
        if (!hasReadContactPermission) {
            PermissionUtils.permission(PermissionConstants.CONTACTS).callback(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    readContact();
                }

                @Override
                public void onDenied() {

                }
            }).request();
        } else {
            readContact();
        }
    }

    private void readContact() {
        //调用并获取联系人信息
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            if (cursor != null) {
                mContactList.clear();
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                    String ringtone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CUSTOM_RINGTONE));
//                    Log.e(TAG, " number = " + number + "  displayName = " + displayName);

                    mContactList.add(new ContactBean(id, number, displayName, photoUri, ringtone));
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
    }

    private void showListDialog(ArrayList<Pair<String, String>> set, SelectDataDialog.Observer observer){
        SelectDataDialog dialog = new SelectDataDialog(getContext());
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        list.addAll(set);
        dialog.setList(list, observer);
        dialog.show();
    }

    //显示选择联系人的Dialog.
    public void showContactDialog(int type) {
        CustomDialog customDialog = new CustomDialog(getContext());
        customDialog.setView(R.layout.dialog_contact);
        RecyclerView rv = customDialog.findViewById(R.id.rv_dialog_contact);
        TextView tv = customDialog.findViewById(R.id.tv_contact_no_data);
        if (mContactList.size() == 0){
            rv.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        }
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        SettingContactAdapter contactAdapter = new SettingContactAdapter(mContactList);
        contactAdapter.setOnItemClickListener(new SettingContactAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                try {
                    switch (type) {
                        case TYPE_SELECT_PHONE_1: {
                            String number = mContactList.get(pos).number;
                            if (editSelectMobile1 != null) {
                                editSelectMobile1.setData(number);
                            }
                            String contactName = mContactList.get(pos).contactName;
                            if (editTextName1 != null) {
                                editTextName1.setData(contactName);
                            }
                            break;
                        }
                        case TYPE_SELECT_PHONE_2: {
                            String number = mContactList.get(pos).number;
                            if (editSelectMobile2 != null) {
                                editSelectMobile2.setData(number);
                            }
                            String contactName = mContactList.get(pos).contactName;
                            if (editTextName2 != null) {
                                editTextName2.setData(contactName);
                            }
                            break;
                        }
                    }
                } catch (Exception e) {

                }
                if (customDialog != null) {
                    customDialog.dismiss();
                }
            }
        });
        rv.setAdapter(contactAdapter);
        customDialog.show();
    }

    private boolean checkCommitAvailable(){
        if (editTextName1 == null || editTextName1.isEmptyText()) {
            ToastUtils.showShort("contact name 1 is null");
            return false;
        }
        if (editSelectMobile1 == null || editSelectMobile1.isEmptyText()) {
            ToastUtils.showShort("contact mobile 1 is null");
            return false;
        }
        if (mRelativeShip1 == null ) {
            ToastUtils.showShort("relativeShip 1 is null");
            return false;
        }

        if (editTextName2 == null || editTextName2.isEmptyText()) {
            ToastUtils.showShort("contact name 2 is null");
            return false;
        }
        if (editSelectMobile2 == null || editSelectMobile2.isEmptyText()) {
            ToastUtils.showShort("contact mobile 2 is null");
            return false;
        }
        if (mRelativeShip2 == null ) {
            ToastUtils.showShort("relativeShip 2 is null");
            return false;
        }
       return true;
    }

    private void uploadContact() {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("access_token", Constant.mToken);
            jsonObject.put("account_id", Constant.mAccountId + "");
            jsonObject.put("contact1_name", editTextName1.getData());
            jsonObject.put("contact1_mobile", editSelectMobile1.getData());
            jsonObject.put("contact1_relationship", mRelativeShip1.second);
            jsonObject.put("contact2_name", editTextName2.getData());
            jsonObject.put("contact2_mobile", editSelectMobile2.getData());
            jsonObject.put("contact2_relationship", mRelativeShip2.second);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, jsonObject.toString());
        OkGo.<String>post(Api.UPLOAD_CONTACT).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        PhaseBean phaseBean = checkResponseSuccess(response, PhaseBean.class);
                        if (phaseBean == null) {
                            Log.e(TAG, " upload contact error ." + response.body());
                            return;
                        }
                        checkAndToPageByPhaseCode(phaseBean.getCurrent_phase());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "upload contact failure = " + response.body());
                    }
                });
    }

    @Override
    public void setProfileBean(AccountProfileBean.AccountProfile profileBean) {
        super.setProfileBean(profileBean);
//        private SelectContainer selectRelationShip1, selectRelationShip2
        if (editSelectMobile1 != null && !TextUtils.isEmpty(profileBean.getContact1_mobile())){
            editSelectMobile1.setData(profileBean.getContact1_mobile());
        }
        if (editSelectMobile2 != null && !TextUtils.isEmpty(profileBean.getContact2_mobile())){
            editSelectMobile2.setData(profileBean.getContact2_mobile());
        }
        if (editTextName1 != null && !TextUtils.isEmpty(profileBean.getContact1_name())){
            editTextName1.setData(profileBean.getContact1_name());
        }
        if (editTextName2 != null && !TextUtils.isEmpty(profileBean.getContact2_name())){
            editTextName2.setData(profileBean.getContact2_name());
        }
    }
}
