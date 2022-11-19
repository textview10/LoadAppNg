package com.loadapp.load.ui.profile.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.bean.AccountProfileBean;
import com.loadapp.load.bean.BankNameBean;
import com.loadapp.load.bean.PhaseBean;
import com.loadapp.load.bean.event.BankListEvent;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.profile.BankListActivity;
import com.loadapp.load.ui.profile.CommitProfileActivity;
import com.loadapp.load.ui.profile.widget.EditTextContainer;
import com.loadapp.load.ui.profile.widget.SelectContainer;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BankInfoFragment extends BaseCommitFragment{

    private static final String TAG = "BankInfoFragment";

    private EditTextContainer etVerifyNum, etAccountNum;
    private SelectContainer selectBackName;
    private FrameLayout flCommit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_info, container ,false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof CommitProfileActivity) {
            ((CommitProfileActivity) getActivity()).setTitle(R.string.load_person_profile_bank_name_title);
        }
        etVerifyNum = view.findViewById(R.id.edittext_container_bank_verify_num);
        selectBackName = view.findViewById(R.id.select_container_bank_name);
        etAccountNum = view.findViewById(R.id.edittext_container_bank_account_num);
        flCommit = view.findViewById(R.id.fl_bank_info_commit);

        selectBackName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BankListActivity.class);
                startActivity(intent);
            }
        });
        flCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCommitAvailable()){
                    uploadBank();
                }
            }
        });
    }

    @Override
    public void setProfileBean(AccountProfileBean profileBean) {
        super.setProfileBean(profileBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onEvent(BankListEvent event) {
        Pair<String, String> data = event.getData();
        selectBackName.setData(data.second);
    }

    private boolean checkCommitAvailable(){
        if (etVerifyNum == null || etVerifyNum.isEmptyText()) {
            ToastUtils.showShort(" verify num null");
            return false;
        }
        if (etAccountNum == null || etAccountNum.isEmptyText()) {
            ToastUtils.showShort("account name is null");
            return false;
        }
        if (selectBackName == null || TextUtils.isEmpty(selectBackName.getData())) {
            ToastUtils.showShort("account name is null");
            return false;
        }
        return true;
    }

    private void uploadBank(){
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            //wallet 或者 bank
            jsonObject.put("receive_type", "bank");
            //银行名字
            jsonObject.put("bank_name", selectBackName.getData());
            //银行分支结构代码
            jsonObject.put("branch_code", Constant.mAccountId);
            //银行账号
            jsonObject.put("bank_account", etAccountNum.getText().trim());
            //确认银行账号
            jsonObject.put("bank_account_confirm", etAccountNum.getText().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.UPLOAD_BANK).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        PhaseBean phaseBean = checkResponseSuccess(response, PhaseBean.class);
                        if (phaseBean == null) {
                            Log.e(TAG, " upload bank info ." + response.body());
                            return;
                        }
                        if (getActivity() instanceof CommitProfileActivity) {
                            ((CommitProfileActivity) getActivity()).switchFragment(4);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "upload bank info = " + response.body());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
