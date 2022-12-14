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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
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
import com.loadapp.load.ui.profile.fragment.popupwindow.SelectTypePopUp;
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
    private SelectContainer selectBackName, selectType;
    private FrameLayout flCommit;

    public static final int TYPE_BANK = 1;
    public static final int TYPE_WALLET = 2;
    private int mCurType = TYPE_BANK;
    private LinearLayout llBank, llWallet;
    private EditTextContainer etWalletAccount, etWalletAccountConfirm;

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
        selectType = view.findViewById(R.id.select_container_type);
        llBank = view.findViewById(R.id.ll_bank_info_bank);
        llWallet = view.findViewById(R.id.ll_bank_info_wallet);

        etWalletAccount = view.findViewById(R.id.edittext_container_bank_wallet_account);
        etWalletAccountConfirm = view.findViewById(R.id.edittext_container_bank_wallet_account_confirm);
        selectBackName = view.findViewById(R.id.select_container_bank_name);
        etAccountNum = view.findViewById(R.id.edittext_container_bank_account_num);
        flCommit = view.findViewById(R.id.fl_bank_info_commit);

        selectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectTypePopUp popUp = new SelectTypePopUp(getContext(), new SelectTypePopUp.OnPopUpClickListener() {
                    @Override
                    public void onClick(int type) {
                        mCurType = type;
                        updateType();
                    }
                });
                popUp.show(selectType);
            }
        });

        selectBackName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkClickFast()){
                    return;
                }
                Intent intent = new Intent(getActivity(), BankListActivity.class);
                startActivity(intent);
            }
        });
        flCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCommitAvailable()){
                    if (mCurType == TYPE_BANK) {
                        uploadBank();
                    } else if (mCurType == TYPE_WALLET) {
                        uploadWallet();
                    }
                }
            }
        });
        updateType();
    }

    @Override
    public void setProfileBean(AccountProfileBean.AccountProfile profileBean) {
        super.setProfileBean(profileBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onEvent(BankListEvent event) {
        Pair<String, String> data = event.getData();
        if (selectBackName != null) {
            selectBackName.setData(data.second);
        }
    }

    private boolean checkCommitAvailable(){
//        if (etVerifyNum == null || etVerifyNum.isEmptyText()) {
//            ToastUtils.showShort(" verify num null");
//            return false;
//        }
        if (mCurType == TYPE_BANK) {
            if (etAccountNum == null || etAccountNum.isEmptyText()) {
                ToastUtils.showShort("account name is null");
                return false;
            }
            if (selectBackName == null || TextUtils.isEmpty(selectBackName.getData())) {
                ToastUtils.showShort("account name is null");
                return false;
            }
        } else if (mCurType == TYPE_WALLET) {
            if (etWalletAccount == null || etWalletAccount.isEmptyText()){
                ToastUtils.showShort("wallet account is null");
                return false;
            }
            if (etWalletAccountConfirm == null || etWalletAccountConfirm.isEmptyText()){
                ToastUtils.showShort("wallet account confirm is null");
                return false;
            }
            String text1 = etWalletAccount.getText();
            String text2 = etWalletAccountConfirm.getText();
            if (!TextUtils.equals(text1, text2)){
                ToastUtils.showShort("wallet account not equal");
                return false;
            }
        }
        return true;
    }

    private void uploadBank(){
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            //wallet ?????? bank
            jsonObject.put("receive_type", "bank");
            //????????????
            jsonObject.put("bank_name", selectBackName.getData());
            //????????????????????????
            jsonObject.put("branch_code", selectBackName.getData());
            //????????????
            jsonObject.put("bank_account", etAccountNum.getText().trim());
            //??????????????????
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
                        checkAndToPageByPhaseCode(phaseBean.getCurrent_phase());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "upload bank info = " + response.body());
                    }
                });
    }


    private void uploadWallet() {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            //wallet ?????? bank
            jsonObject.put("receive_type", "wallet");
            //??????????????????
            jsonObject.put("wallet_account", etWalletAccount.getText());
            //????????????????????????
            jsonObject.put("wallet_account_confirm", etWalletAccountConfirm.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.UPLOAD_WALLET).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        PhaseBean phaseBean = checkResponseSuccess(response, PhaseBean.class);
                        if (phaseBean == null) {
                            Log.e(TAG, " upload wallet info ." + response.body());
                            return;
                        }
                        checkAndToPageByPhaseCode(phaseBean.getCurrent_phase());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, " upload wallet info = " + response.body());
                    }
                });
    }

    private void updateType(){
        @StringRes int title = -1;
        if (mCurType == TYPE_BANK){
            llBank.setVisibility(View.VISIBLE);
            llWallet.setVisibility(View.GONE);
            selectType.setData(getResources().getString(R.string.load_person_profile_type_bank));
            title = R.string.load_person_profile_bank_name_title;
        } else if (mCurType == TYPE_WALLET) {
            llBank.setVisibility(View.GONE);
            llWallet.setVisibility(View.VISIBLE);
            selectType.setData(getResources().getString(R.string.load_person_profile_type_wallet));
            title = R.string.load_person_profile_wallet_name_title;
        }
        if (getActivity() instanceof CommitProfileActivity && title != -1) {
            ((CommitProfileActivity) getActivity()).setTitle(title);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
