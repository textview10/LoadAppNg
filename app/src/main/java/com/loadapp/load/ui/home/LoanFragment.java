package com.loadapp.load.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.BuildConfig;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseFragment;
import com.loadapp.load.bean.OrderInfoBean;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.home.status.loanapply.LoanApplyFragment;
import com.loadapp.load.ui.home.status.LoanProcessingFragment;
import com.loadapp.load.ui.profile.CommitProfileActivity;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class LoanFragment extends BaseFragment {

    private static final String TAG = "LoanFragment";
    private ProgressBar pbLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pbLoading = view.findViewById(R.id.pb_main_loading);
        pbLoading.setVisibility(View.VISIBLE);
        if (BuildConfig.DEBUG) {
            Button btnProfile = view.findViewById(R.id.btn_loan_profile_test);
            btnProfile.setVisibility(View.VISIBLE);
            btnProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), CommitProfileActivity.class);
                    startActivity(intent);
                }
            });
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(1000);
                getOrderInfo();
            }
        }.start();
//        PersonProfileFragment personProfileFragment = new PersonProfileFragment();
//        toFragment(personProfileFragment);
    }

    public void toFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        transaction.replace(getFragmentContainerRes(), fragment);
        transaction.commitAllowingStateLoss();
    }

    private void getOrderInfo() {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            jsonObject.put("access_token", Constant.mToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.e(TAG, "111 id = " + Constant.mAccountId);
//        Log.e(TAG, "111 token = " + Constant.mToken);
        OkGo.<String>post(Api.GET_ORDER_INFO).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (pbLoading != null){
                            pbLoading.setVisibility(View.GONE);
                        }
                        if (getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        OrderInfoBean orderInfo = checkResponseSuccess(response, OrderInfoBean.class);
                        if (orderInfo == null) {
                            Log.e(TAG, " order info error ." + response.body());
                            return;
                        }
                       updatePageByStatus(orderInfo);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (pbLoading != null){
                            pbLoading.setVisibility(View.GONE);
                        }
                        if (getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        Log.e(TAG, "order info failure = " + response.body());
                        ToastUtils.showShort("order info failure");
                    }
                });
    }

    private void updatePageByStatus(OrderInfoBean orderInfoBean){
        //可以借款
//        if (orderInfoBean.isCan_apply() && orderInfoBean.getOrder_id() == 0){
        if (true){
            LoanApplyFragment loanApplyFragment = new LoanApplyFragment();
            toFragment(loanApplyFragment);
            return;
        }
        int checkStatus = orderInfoBean.getCheck_status();
        switch (checkStatus){
            case 1: //已提交待审核
            case 2: //审核拒绝
            case 3: //等待人工审核
            case 4: //等待放款
            case 5: //放款中
            case 6: //放款失败
                LoanProcessingFragment processingFragment = new LoanProcessingFragment();
                toFragment(processingFragment);
                break;

            case 7: //等待还款

                break;
            case 8: //已结清

                break;
            case 9: //逾期

                break;
            case 10: //还款中

                break;
            case 11: //等待展期

                break;
            case 12: //展期结清

                break;
            case 13: //展期申请中

                break;
            case 14: //展期失效

                break;
        }
    }

    protected @IdRes
    int getFragmentContainerRes() {
        return R.id.fl_loan_container;
    }
}
