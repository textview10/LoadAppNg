package com.loadapp.load.ui.home.status.loanapply.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ColorUtils;
import com.loadapp.load.R;
import com.loadapp.load.bean.LoanApplyBean;

import java.util.ArrayList;

public class LoanApplyAdapter1 extends RecyclerView.Adapter<LoanApplyHolder> {

    private ArrayList<Pair<String, ArrayList<LoanApplyBean.Product>>> mList ;

    private OnItemClickListener mListener;

    private int mSelectPos = 0;

    public LoanApplyAdapter1() {

    }

    public void setList(ArrayList<Pair<String, ArrayList<LoanApplyBean.Product>>> list){
        mList = list;
    }

    @NonNull
    @Override
    public LoanApplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan_apply, parent, false);
        return new LoanApplyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanApplyHolder holder, @SuppressLint("RecyclerView") int position) {
        Pair<String, ArrayList<LoanApplyBean.Product>> pair = mList.get(position);
        if (pair != null && !TextUtils.isEmpty(pair.first)) {
            holder.tvAmount.setText(pair.first);
        }
        holder.flBg.setBackgroundResource(mSelectPos == position ?
                R.drawable.shape_round_grey_10 : R.drawable.shape_round_white_10);
        @ColorRes int res = mSelectPos == position ? R.color.white : R.color.black;
        holder.tvAmount.setTextColor(ColorUtils.getColor(res));
        holder.itemView.setOnClickListener(view -> {
            int oldPos = mSelectPos;
            mSelectPos = position;
            notifyItemChanged(oldPos);
            notifyItemChanged(mSelectPos);
            if (mListener != null) {
                mListener.onClick(pair.second, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(ArrayList<LoanApplyBean.Product> products, int pos);
    }

    public void setSelectPos(int pos){
        mSelectPos = pos;
    }
}
