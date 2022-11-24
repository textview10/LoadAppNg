package com.loadapp.load.ui.home.status.loanapply.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;
import com.loadapp.load.bean.LoanApplyBean;

import java.util.ArrayList;
import java.util.List;

public class LoanApplyAdapter extends RecyclerView.Adapter<LoanApplyHolder> {

    private List<LoanApplyBean.Product> mLists;

    public LoanApplyAdapter() {

    }

    public void setList(List<LoanApplyBean.Product> lists){
        mLists = lists;
    }

    @NonNull
    @Override
    public LoanApplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan_apply, parent, false);
        return new LoanApplyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanApplyHolder holder, int position) {
        LoanApplyBean.Product product = mLists.get(position);
        if (product != null) {
            if (!TextUtils.isEmpty(product.getProduct_name())){
                holder.tvName.setText(product.getProduct_name());
            }
            if (!TextUtils.isEmpty(product.getAmount())){
                holder.tvAmount.setText(product.getAmount());
            }
            if (!TextUtils.isEmpty(product.getPeriod())){
                holder.tvCount.setText(product.getPeriod());
            }
            if (!TextUtils.isEmpty(product.getStage())){
                holder.tvCount2.setText(product.getStage());
            }
        }

    }


    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }
}
