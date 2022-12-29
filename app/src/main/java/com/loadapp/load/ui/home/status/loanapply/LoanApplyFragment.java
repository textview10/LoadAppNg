package com.loadapp.load.ui.home.status.loanapply;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.loadapp.load.ui.home.status.loanapply.adapter.LoanApplyAdapter3;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoanApplyFragment extends BaseStatusFragment {

    private static final String TAG = "LoanApplyFragment";
    private RecyclerView rvLoanApply, rvLoanApplyDate, rvDetail;
    private LoanApplyAdapter1 adapter1;
    private LoanApplyAdapter2 adapter2;
    private LoanApplyAdapter3 adapter3;

    private FrameLayout flCommit;
    private ArrayList<Pair<String, ArrayList<LoanApplyBean.Product>>> mList;
    private LoanApplyBean.Product curProduct;

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
        rvDetail = view.findViewById(R.id.rv_loan_apply_detail);

        flCommit = view.findViewById(R.id.fl_item_loan_apply_commit);

        initializeView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        requestLoanProductList();
    }

    private void initializeView() {
        GridLayoutManager manager1 = new GridLayoutManager(getActivity(), 3);
        rvLoanApply.setLayoutManager(manager1);
        adapter1 = new LoanApplyAdapter1();
        adapter1.setSelectPos(0);
        rvLoanApply.setAdapter(adapter1);
        rvLoanApply.addItemDecoration(new HomeItemDecoration());
        adapter1.setItemClickListener((products, pos) -> {
            if (checkShortClickFast()) {
                return;
            }
            updateAdapter2(products);
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

        GridLayoutManager manager2 = new GridLayoutManager(getActivity(), 3);
        rvLoanApplyDate.setLayoutManager(manager2);
        adapter2 = new LoanApplyAdapter2();
        rvLoanApplyDate.setAdapter(adapter2);
        adapter2.setSelectPos(0);
        rvLoanApplyDate.addItemDecoration(new HomeItemDecoration());
        adapter2.setItemClickListener(new LoanApplyAdapter2.OnItemClickListener() {
            @Override
            public void onClick(LoanApplyBean.Product product, int pos) {
                if (checkShortClickFast()) {
                    return;
                }
                requestItemProductTrial(product);
            }
        });

        LinearLayoutManager manager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvDetail.setLayoutManager(manager3);
        adapter3 = new LoanApplyAdapter3(getContext());
        rvDetail.setAdapter(adapter3);

        flCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curProduct == null) {
                    ToastUtils.showShort("not choice product");
                    return;
                }
                checkCanLoanApply();
            }
        });
    }

    private void updateAdapter3(ArrayList<ProductTrialBean.Trial> trial) {
        ArrayList<ProductTrialBean.Trial> tempList = new ArrayList<>();
        tempList.addAll(trial);
        if (adapter3 != null) {
            adapter3.setList(tempList);
            adapter3.notifyDataSetChanged();
        }
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
                        mList = convertData(loanApply);

                        if (adapter1 != null) {
                            adapter1.setList(mList);
                            adapter1.notifyDataSetChanged();
                        }
                        if (mList != null && mList.size() > 0) {
                            Pair<String, ArrayList<LoanApplyBean.Product>> pair = mList.get(0);
                            updateAdapter2(pair.second);
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

    private ArrayList<Pair<String, ArrayList<LoanApplyBean.Product>>> convertData(LoanApplyBean loanApply) {
        HashMap<String, ArrayList<LoanApplyBean.Product>> mMap = new HashMap<>();
        for (int i = 0; i < loanApply.getProducts().size(); i++) {
            LoanApplyBean.Product product = loanApply.getProducts().get(i);
            ArrayList<LoanApplyBean.Product> itemList;
            if (mMap.containsKey(product.getAmount())) {
                itemList = mMap.get(product.getAmount());
            } else {
                itemList = new ArrayList<>();
                mMap.put(product.getAmount(), itemList);
            }
            itemList.add(product);
        }

        ArrayList<Pair<String, ArrayList<LoanApplyBean.Product>>> list = new ArrayList<>();
        Iterator<Map.Entry<String, ArrayList<LoanApplyBean.Product>>> iterator = mMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<LoanApplyBean.Product>> next = iterator.next();
            Pair<String, ArrayList<LoanApplyBean.Product>> pair = new Pair<>(next.getKey(), next.getValue());
            list.add(pair);
        }
        return list;
    }

    private void updateAdapter2(ArrayList<LoanApplyBean.Product> list) {
        if (adapter2 != null) {
            adapter2.setList(list);
            adapter2.notifyDataSetChanged();
            if (list != null && list.size() > 0) {
                LoanApplyBean.Product product = list.get(0);
                requestItemProductTrial(product);
            }
        }
    }

    private void requestItemProductTrial(LoanApplyBean.Product product) {
        curProduct = product;
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
                        } catch (Exception e) {

                        }

                        if (productTrial == null) {
                            Log.e(TAG, " product trial error ." + response.body());
                            return;
                        }
                        if (productTrial.getTrials() != null) {
                            ArrayList<ProductTrialBean.Trial> list = new ArrayList();
                            list.addAll(productTrial.getTrials());
                            if (list.size() > 0) {
                                updateAdapter3(list);
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
                            new Thread() {
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
                        if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
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

    @Override
    public void onDestroy() {
        OkGo.getInstance().cancelTag(TAG);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
