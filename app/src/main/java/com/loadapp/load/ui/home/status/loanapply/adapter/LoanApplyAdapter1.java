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

public class LoanApplyAdapter1 extends RecyclerView.Adapter<LoanApplyHolder> {

    private List<LoanApplyBean.Product> mLists;

    private OnItemClickListener mListener;

    private int mSelectPos = 0;

    public LoanApplyAdapter1() {

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
        if (product != null && !TextUtils.isEmpty(product.getAmount())) {
            holder.tvAmount.setText(product.getAmount());
        }
        holder.flBg.setBackgroundResource(mSelectPos == position ?
                R.drawable.shape_round_grey_10 : R.drawable.shape_round_white_10);
        holder.itemView.setOnClickListener(view -> {
            int oldPos = mSelectPos;
            mSelectPos = position;
            notifyItemChanged(oldPos);
            notifyItemChanged(mSelectPos);
            if (mListener != null) {
                mListener.onClick(product, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public void setItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(LoanApplyBean.Product product, int pos);
    }

    public void setSelectPos(int pos){
        mSelectPos = pos;
    }
}
