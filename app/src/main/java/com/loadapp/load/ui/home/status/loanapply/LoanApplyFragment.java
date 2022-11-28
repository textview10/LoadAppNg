package com.loadapp.load.ui.home.status.loanapply;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.bean.CommitLoanBean;
import com.loadapp.load.bean.LoanApplyBean;
import com.loadapp.load.bean.OrderInfoBean;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.home.status.BaseStatusFragment;
import com.loadapp.load.ui.home.status.loanapply.adapter.LoanApplyAdapter;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class LoanApplyFragment extends BaseStatusFragment {

    private static final String TAG = "LoanApplyFragment";
    private RecyclerView rvLoanApply;
    private LoanApplyAdapter adapter;

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
        initializeView();
        canLoanApply();
    }

    private void initializeView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvLoanApply.setLayoutManager(manager);
        adapter = new LoanApplyAdapter();
        rvLoanApply.setAdapter(adapter);
        adapter.setItemClickListener((product, pos) -> {
            if (checkClickFast()){
                return;
            }
            commitLoanApply(product.getProduct_id());
        });
    }

    private void canLoanApply() {
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
                            Log.e(TAG, " loan apply error ." + response.body());
                            return;
                        }
                        if (adapter != null) {
                            adapter.setList(loanApply.getProducts());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        Log.e(TAG, "loan apply  failure = " + response.body());
                        ToastUtils.showShort("loan apply  failure");
                    }
                });
    }

    private void commitLoanApply(String productId){
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
//        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            jsonObject.put("access_token", Constant.mToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "access_token = " +  Constant.mToken);
        Log.e(TAG, "account_id = " +  Constant.mAccountId);
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
                        if (commitLoan.getOrder_id() != 0){
                            ToastUtils.showShort("check order success");
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

    @Override
    public void onDestroy() {
        OkGo.getInstance().cancelTag(TAG);
        super.onDestroy();
    }
}
