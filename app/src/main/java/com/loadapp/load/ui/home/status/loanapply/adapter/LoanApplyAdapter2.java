package com.loadapp.load.ui.home.status.loanapply.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;
import com.loadapp.load.bean.LoanApplyBean;
import com.loadapp.load.bean.ProductTrialBean;

import java.util.ArrayList;
import java.util.List;

public class LoanApplyAdapter2 extends RecyclerView.Adapter<LoanApplyHolder> {

    private List<LoanApplyBean.Product> mLists;

    private OnItemClickListener mListener;

    private int mSelectPos = 0;

    public LoanApplyAdapter2() {

    }

    public void setList(ArrayList<LoanApplyBean.Product> lists) {
        mLists = lists;
    }

    @NonNull
    @Override
    public LoanApplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan_apply, parent, false);
        return new LoanApplyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanApplyHolder holder, @SuppressLint("RecyclerView") int position) {
        LoanApplyBean.Product product = mLists.get(position);
        if (product != null && !TextUtils.isEmpty(product.getPeriod())) {
//            Date date = new Date(trial.getRepay_date());
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            String dateString = formatter.format(date);
            holder.tvAmount.setText(product.getPeriod());
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

    public void setItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(LoanApplyBean.Product product, int pos);
    }

    public void setSelectPos(int pos) {
        mSelectPos = pos;
    }
}
