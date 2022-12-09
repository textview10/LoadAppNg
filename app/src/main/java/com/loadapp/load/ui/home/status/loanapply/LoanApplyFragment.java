package com.loadapp.load.ui.home.status.loanapply;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.bean.ApplyResultBean;
import com.loadapp.load.bean.CommitLoanBean;
import com.loadapp.load.bean.LoanApplyBean;
import com.loadapp.load.bean.ProductTrialBean;
import com.loadapp.load.bean.event.PhaseAllEvent;
import com.loadapp.load.bean.event.UpdateOrderInfo;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.home.status.BaseStatusFragment;
import com.loadapp.load.ui.home.status.loanapply.adapter.HomeItemDecoration;
import com.loadapp.load.ui.home.status.loanapply.adapter.LoanApplyAdapter1;
import com.loadapp.load.ui.home.status.loanapply.adapter.LoanApplyAdapter2;
import com.loadapp.load.ui.profile.CommitProfileActivity;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.loadapp.load.util.CheckResponseUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoanApplyFragment extends BaseStatusFragment {

    private static final String TAG = "LoanApplyFragment";
    private RecyclerView rvLoanApply, rvLoanApplyDate;
    private LoanApplyAdapter1 adapter1;
    private LoanApplyAdapter2 adapter2;

    private AppCompatTextView tvLoanApply1, tvLoanApply2, tvLoanApply3, tvLoanApply4, tvLoanApply5, tvLoanApply6;
    private LoanApplyBean.Product curProduct;
    private FrameLayout flCommit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_apply, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvLoanApply = view.findViewById(R.id.rv_loan_apply);
        rvLoanApplyDate = view.findViewById(R.id.rv_loan_apply_date);

        tvLoanApply1 = view.findViewById(R.id.tv_loan_apply_1);
        tvLoanApply2 = view.findViewById(R.id.tv_loan_apply_2);
        tvLoanApply3 = view.findViewById(R.id.tv_loan_apply_3);
        tvLoanApply4 = view.findViewById(R.id.tv_loan_apply_4);
        tvLoanApply5 = view.findViewById(R.id.tv_loan_apply_5);
        tvLoanApply6 = view.findViewById(R.id.tv_loan_apply_6);

        flCommit = view.findViewById(R.id.fl_item_loan_apply_commit);

        initializeView();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        requestLoanProductList();
    }

    private void initializeView() {
        GridLayoutManager manager1 = new GridLayoutManager(getActivity(), 2);
        rvLoanApply.setLayoutManager(manager1);
        adapter1 = new LoanApplyAdapter1();
        adapter1.setSelectPos(0);
        rvLoanApply.setAdapter(adapter1);
        rvLoanApply.addItemDecoration(new HomeItemDecoration());
        adapter1.setItemClickListener((product, pos) -> {
            if (checkClickFast()) {
                return;
            }
            curProduct = product;
            requestItemProductTrial(curProduct);
//            ProductTrialDialog dialog = new ProductTrialDialog(getActivity(), productId,
//                    amount, period);
//            dialog.setOnDialogClickListener(new ProductTrialDialog.OnDialogClickListener() {
//                @Override
//                public void onClickAgree() {
//                    curProductId = product.getProduct_id();
//                    checkCanLoanApply();
//                }
//            });
//            dialog.show();
        });

        GridLayoutManager manager2 = new GridLayoutManager(getActivity(), 2);
        rvLoanApplyDate.setLayoutManager(manager2);
        adapter2 = new LoanApplyAdapter2();
        rvLoanApplyDate.setAdapter(adapter2);
        adapter2.setSelectPos(0);
        rvLoanApplyDate.addItemDecoration(new HomeItemDecoration());
        adapter2.setItemClickListener(new LoanApplyAdapter2.OnItemClickListener() {
            @Override
            public void onClick(ProductTrialBean.Trial trial, int pos) {
                updateTextView(trial);
            }
        });

        flCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curProduct == null){
                    ToastUtils.showShort("not choice product");
                    return;
                }
                checkCanLoanApply();
            }
        });
    }

    //请求有几种借贷
    private void requestLoanProductList() {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            jsonObject.put("access_token", Constant.mToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.GET_PRODUCT_LIST).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        LoanApplyBean loanApply = checkResponseSuccess(response, LoanApplyBean.class);
                        if (loanApply == null) {
                            Log.e(TAG, " get product list error ." + response.body());
                            return;
                        }

                        ArrayList<LoanApplyBean.Product> list = new ArrayList();
                        list.addAll(loanApply.getProducts());
                        if (adapter1 != null) {
                            adapter1.setList(list);
                            adapter1.notifyDataSetChanged();
                        }
                        if (list != null && list.size() > 0) {
                            LoanApplyBean.Product product = list.get(0);
                            curProduct = product;
                            requestItemProductTrial(product);
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        Log.e(TAG, " get product list failure = " + response.body());
                        ToastUtils.showShort(" get product list failure");
                    }
                });
    }

    private void requestItemProductTrial(LoanApplyBean.Product product) {
        String productId = product.getProduct_id();
        String amount = product.getAmount();
        String period = product.getPeriod();
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            jsonObject.put("access_token", Constant.mToken);
            jsonObject.put("product_id", productId);
            jsonObject.put("amount", amount);
            jsonObject.put("perodi", period);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.PRODUCT_TRIAL).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ProductTrialBean productTrial = null;
                        try {
                            productTrial = CheckResponseUtils.checkResponseSuccess(response, ProductTrialBean.class);
                        } catch (Exception e){

                        }

                        if (productTrial == null) {
                            Log.e(TAG, " product trial error ." + response.body());
                            return;
                        }
                        if (productTrial.getTrials() != null) {
                            ArrayList<ProductTrialBean.Trial> list = new ArrayList();
                            list.addAll(productTrial.getTrials());
                            if (adapter2 != null) {
                                adapter2.setList(list);
                                adapter2.notifyDataSetChanged();
                            }
                            if (list.size() > 0){
                                ProductTrialBean.Trial trial = list.get(0);
                                updateTextView(trial);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "product trial failure = " + response.body());
//                        ToastUtils.showShort("product trial failure");
                    }
                });
    }

    private void checkCanLoanApply() {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            jsonObject.put("access_token", Constant.mToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.CHECK_CAN_ORDER).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        CommitLoanBean commitLoan = checkResponseSuccess(response, CommitLoanBean.class);
                        if (commitLoan == null) {
                            Log.e(TAG, " commit loan error ." + response.body());
                            return;
                        }
                        if (commitLoan.getOrder_id() != 0) {
                            //正式开始申请贷款
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    SystemClock.sleep(3000);
                                    applyLoad(commitLoan.getOrder_id());
                                }
                            }.start();
                        } else {
                            if (commitLoan.getCurrent_phase() == CommitProfileActivity.PHASE_ALL) {
                                ToastUtils.showShort("orderId is null");
                            } else {
                                CommitProfileActivity.startActivity(getActivity(), commitLoan.getCurrent_phase());
                            }
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        Log.e(TAG, "commit loan  failure = " + response.body());
                        ToastUtils.showShort("commit loan failure");
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onEvent(PhaseAllEvent event) {
        checkCanLoanApply();
    }

    private void applyLoad(long orderId) {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            jsonObject.put("access_token", Constant.mToken);
            //订单ID
            jsonObject.put("order_id", orderId + "");
            //申请的产品ID
            jsonObject.put("product_id", curProduct.getProduct_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.ORDER_APPLY).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        ApplyResultBean applyResult = checkResponseSuccess(response, ApplyResultBean.class);
                        if (applyResult == null) {
                            Log.e(TAG, " apply result error ." + response.body());
                            return;
                        }
                        if (applyResult.getOrder_create() == 1) {
                            ToastUtils.showShort("order create success");
                            EventBus.getDefault().post(new UpdateOrderInfo());
                        } else {
                            ToastUtils.showShort("order create failure");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        Log.e(TAG, "apply result  failure = " + response.body());
                        ToastUtils.showShort("apply result failure");
                    }
                });
    }

    private void updateTextView(ProductTrialBean.Trial trial) {

        if (tvLoanApply1 != null) {   //应还款总金额
            tvLoanApply1.setText(String.valueOf(trial.getTotal()));
        }
        if (tvLoanApply2 != null) {  //本金
            tvLoanApply2.setText(String.valueOf(trial.getAmount()));
        }
        if (tvLoanApply3 != null) {    //利息
            tvLoanApply3.setText(String.valueOf(trial.getInterest()));
        }
        if (tvLoanApply4 != null) {       //服务费
            tvLoanApply4.setText(String.valueOf(trial.getService_fee()));
        }
        if (tvLoanApply5 != null) {        //砍头服务费，非砍头产品为0
            tvLoanApply5.setText(String.valueOf(trial.getInterest_prepaid()));
        }
        if (tvLoanApply6 != null) {    //放款金额
            tvLoanApply6.setText(String.valueOf(trial.getInterest_prepaid()));
        }
//        if (tvDisburse != null) {    //放款金额
//            tvDisburse.setText(String.valueOf(trial.getDisburse_amount()));
//        }
        //砍头利息，非砍头产品为0
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
