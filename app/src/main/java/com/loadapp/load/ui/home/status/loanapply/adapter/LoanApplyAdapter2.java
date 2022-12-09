package com.loadapp.load.ui.home.status.loanapply.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;
import com.loadapp.load.bean.LoanApplyBean;
import com.loadapp.load.bean.ProductTrialBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LoanApplyAdapter2 extends RecyclerView.Adapter<LoanApplyHolder> {

    private List<ProductTrialBean.Trial> mLists;

    private OnItemClickListener mListener;

    private int mSelectPos = 0;

    public LoanApplyAdapter2() {

    }

    public void setList(List<ProductTrialBean.Trial> lists) {
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
        ProductTrialBean.Trial trial = mLists.get(position);

        if (trial != null && !TextUtils.isEmpty(trial.getRepay_date())) {
//            Date date = new Date(trial.getRepay_date());
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            String dateString = formatter.format(date);
            holder.tvAmount.setText(trial.getRepay_date());
        }
        holder.flBg.setBackgroundResource(mSelectPos == position ?
                R.drawable.shape_round_grey_10 : R.drawable.shape_round_white_10);
        holder.itemView.setOnClickListener(view -> {
            int oldPos = mSelectPos;
            mSelectPos = position;
            notifyItemChanged(oldPos);
            notifyItemChanged(mSelectPos);
            if (mListener != null) {
                mListener.onClick(trial, position);
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
        void onClick(ProductTrialBean.Trial trial, int pos);
    }

    public void setSelectPos(int pos) {
        mSelectPos = pos;
    }
}
