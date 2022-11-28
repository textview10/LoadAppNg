package com.loadapp.load.dialog.producttrial;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.bean.LoanApplyBean;
import com.loadapp.load.bean.ProductTrialBean;
import com.loadapp.load.dialog.requestpermission.RequestPermissionDialog;
import com.loadapp.load.global.Constant;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.loadapp.load.util.CheckResponseUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductTrialDialog extends Dialog {

    private static final String TAG = "ProductTrialDialog";
    private ProductTrialAdapter mAdapter;
    private final ArrayList<ProductTrialBean.Trial> mLists = new ArrayList<>();

    public ProductTrialDialog(@NonNull Context context, String productId,
                              String amount, String perodid) {
        super(context);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (ScreenUtils.getAppScreenWidth() * 3 / 4); //设置宽度
        getWindow().setAttributes(lp);
        setCancelable(false);

        setContentView(R.layout.dialog_product_trial);
        TextView tvCancel = findViewById(R.id.tv_product_trial_cancel);
        TextView tvAgree = findViewById(R.id.tv_product_trial_agree);
        RecyclerView rvContent = findViewById(R.id.rv_product_trial_content);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        mAdapter = new ProductTrialAdapter();
        mAdapter.setList(mLists);
        rvContent.setAdapter(mAdapter);

        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickAgree();
                }
                dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        getProductTrial(productId, amount, perodid);
    }

    /**
     * @param productId 产品ID
     * @param amount    产品金额
     * @param perodid   产品期限
     */
    private void getProductTrial(String productId, String amount, String perodid) {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            jsonObject.put("access_token", Constant.mToken);
            jsonObject.put("product_id", productId);
            jsonObject.put("amount", amount);
            jsonObject.put("perodi", perodid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.PRODUCT_TRIAL).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!isShowing()) {
                            return;
                        }
                        ProductTrialBean productTrial = CheckResponseUtils.checkResponseSuccess(response, ProductTrialBean.class);
                        if (productTrial == null) {
                            Log.e(TAG, " product trial error ." + response.body());
                            return;
                        }
                        if (productTrial.getTrials() != null) {
                            mLists.clear();
                            mLists.addAll(productTrial.getTrials());
                            if (mAdapter != null) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (!isShowing()) {
                            return;
                        }
                        Log.e(TAG, "product trial failure = " + response.body());
//                        ToastUtils.showShort("product trial failure");
                    }
                });
    }

    private OnDialogClickListener mListener;

    public void setOnDialogClickListener(OnDialogClickListener listener) {
        this.mListener = listener;
    }

    public static abstract class OnDialogClickListener {
        public abstract void onClickAgree();

        public void onClickCancel() {

        }
    }
}
