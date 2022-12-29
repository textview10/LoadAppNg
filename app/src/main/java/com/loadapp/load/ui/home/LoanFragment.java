package com.loadapp.load.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
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
import com.loadapp.load.bean.event.PhaseAllEvent;
import com.loadapp.load.bean.event.UpdateOrderInfo;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.home.status.loanapply.LoanApplyFragment;
import com.loadapp.load.ui.home.status.LoanProcessingFragment;
import com.loadapp.load.ui.home.status.repaydue.RepayDueFragment;
import com.loadapp.load.ui.profile.CommitProfileActivity;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class LoanFragment extends BaseFragment {

    private static final String TAG = "LoanFragment";
    private ProgressBar pbLoading;

    private static final int TYPE_DELAY = 111;

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case TYPE_DELAY:
                    if (Constant.mLaunchOrderInfo != null &&
                            Constant.mLaunchOrderInfo.getOrder_detail() != null){
                        if (pbLoading != null){
                            pbLoading.setVisibility(View.GONE);
                        }
                        updatePageByStatus(Constant.mLaunchOrderInfo.getOrder_detail());
                    } else {
                        getOrderInfo();
                    }
                    break;
            }
            return false;
        }
    });

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
        mHandler.sendEmptyMessageDelayed(TYPE_DELAY, 500);
//        PersonProfileFragment personProfileFragment = new PersonProfileFragment();
//        toFragment(personProfileFragment);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
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
        Log.e(TAG, "111 id = " + Constant.mAccountId);
        Log.e(TAG, "111 token = " + Constant.mToken);
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
                       updatePageByStatus(orderInfo.getOrder_detail());
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

    private void updatePageByStatus(OrderInfoBean.OrderDetail orderInfoBean){
        //可以借款
        if (orderInfoBean.isCan_apply() || TextUtils.equals(orderInfoBean.getOrder_id(), "0")){
//        if (true){
            LoanApplyFragment loanApplyFragment = new LoanApplyFragment();
            toFragment(loanApplyFragment);
            return;
        }
        int checkStatus = orderInfoBean.getCheck_status();
//        Log.e("Test","check status == " + checkStatus);
        switch (checkStatus){
            case 1: //已提交待审核
            case 2: //审核拒绝
            case 3: //等待人工审核
            case 4: //等待放款
            case 5: //放款中
            case 6: //放款失败
                LoanProcessingFragment processingFragment = new LoanProcessingFragment();
                processingFragment.setOrderInfo(checkStatus, orderInfoBean);
                toFragment(processingFragment);
                break;
            case 7: //等待还款
            case 9: //逾期
                RepayDueFragment repayDueFragment = new RepayDueFragment();
                repayDueFragment.setData(orderInfoBean.getStages(), checkStatus == 9);
                toFragment(repayDueFragment);
                break;
            case 8: //已结清,可以再次贷款.
                LoanApplyFragment loanApplyFragment = new LoanApplyFragment();
                toFragment(loanApplyFragment);
                break;
            case 10: //还款中
            case 11: //等待展期
            case 12: //展期结清
            case 13: //展期申请中
            case 14: //展期失效
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onEvent(UpdateOrderInfo event) {
        getOrderInfo();
    }

    protected @IdRes
    int getFragmentContainerRes() {
        return R.id.fl_loan_container;
    }

    @Override
    public void onDestroy() {
        OkGo.getInstance().cancelTag(TAG);
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
