package com.loadapp.load.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.BuildConfig;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseFragment;
import com.loadapp.load.bean.AccountProfileBean;
import com.loadapp.load.bean.LogoutBean;
import com.loadapp.load.global.Constant;
import com.loadapp.load.global.GetProfileMgr;
import com.loadapp.load.ui.SplashActivity;
import com.loadapp.load.ui.profile.CommitProfileActivity;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "MeFragment";

    private LinearLayout llInfo, llBankCard, llMsg, llHelp, llTestProfile;
    private FrameLayout flLogout;
    private TextView tvName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName = view.findViewById(R.id.tv_me_avatar_name);
        llInfo = view.findViewById(R.id.ll_me_infomation);
        llBankCard = view.findViewById(R.id.ll_me_bank_card);
        llMsg = view.findViewById(R.id.ll_me_message);
        llHelp = view.findViewById(R.id.ll_me_help);
        flLogout = view.findViewById(R.id.fl_me_logout);

        llTestProfile = view.findViewById(R.id.ll_me_test_to_profile);
        if (Constant.IS_TEST) {
            llTestProfile.setVisibility(View.VISIBLE);
        }
        llInfo.setOnClickListener(this);
        llBankCard.setOnClickListener(this);
        llMsg.setOnClickListener(this);
        llHelp.setOnClickListener(this);
        flLogout.setOnClickListener(this);

        llTestProfile.setOnClickListener(this);

        GetProfileMgr.getInstance().addObserver(observer);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_me_infomation:

                break;
            case R.id.ll_me_bank_card:

                break;
            case R.id.ll_me_message:

                break;
            case R.id.ll_me_help:

                break;
            case R.id.fl_me_logout:
                logOut();
                break;
            case R.id.ll_me_test_to_profile:
                CommitProfileActivity.startActivity(getContext(), CommitProfileActivity.PHASE_1);
                break;
        }
    }

    private GetProfileMgr.Observer observer = new GetProfileMgr.Observer() {
        @Override
        public void onGetData(AccountProfileBean.AccountProfile profileBean) {
            if (tvName != null){
                tvName.setText(String.valueOf(profileBean.getName()));
            }
        }
    };

    private void logOut() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("account_id", Constant.mAccountId + "");
            jsonObject.put("access_token", Constant.mToken + "");
            jsonObject.put("request_time", System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.LOGOUT).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        LogoutBean logoutBean = checkResponseSuccess(response, LogoutBean.class);
                        if (logoutBean == null) {
                            Log.e(TAG, " logout error ." + response.body());
                            return;
                        }
                        Intent intent = new Intent(getContext(), SplashActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        ToastUtils.showShort("logout success");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        Log.e(TAG, "logout failure = " + response.body());
                        ToastUtils.showShort("logout failure");
                    }
                });
    }

    @Override
    public void onDestroyView() {
        OkGo.getInstance().cancelTag(TAG);
        GetProfileMgr.getInstance().removeObserver(observer);
        super.onDestroyView();
    }
}
