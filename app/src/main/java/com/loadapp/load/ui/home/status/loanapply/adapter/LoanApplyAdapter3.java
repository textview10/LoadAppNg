package com.loadapp.load.ui.home.status.loanapply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;
import com.loadapp.load.bean.ProductTrialBean;

import java.util.List;

public class LoanApplyAdapter3 extends RecyclerView.Adapter<LoanApplyHolder2> {
    private List<ProductTrialBean.Trial> mTrials;

    private String TEMPLAIN;

    public LoanApplyAdapter3(Context context) {
        TEMPLAIN = context.getString(R.string.load_apply_index);
    }

    public void setList(List<ProductTrialBean.Trial> trials) {
        mTrials = trials;
    }

    @NonNull
    @Override
    public LoanApplyHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan_apply3, parent, false);
        return new LoanApplyHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanApplyHolder2 holder, int position) {
        ProductTrialBean.Trial trial = mTrials.get(position);
        if (holder.tvTitle != null) {    //利息
            holder.tvTitle.setText(String.format(TEMPLAIN, position + 1));
        }
        if (holder.tvLoanApply1 != null) {   //应还款总金额
            holder.tvLoanApply1.setText(String.valueOf(trial.getTotal()));
        }
        if (holder.tvLoanApply2 != null) {  //本金
            holder.tvLoanApply2.setText(String.valueOf(trial.getAmount()));
        }
        if (holder.tvLoanApply3 != null) {       //服务费
            holder.tvLoanApply3.setText(String.valueOf(trial.getService_fee()));
        }
        if (holder.tvLoanApply4 != null) {    //利息
            holder.tvLoanApply4.setText(String.valueOf(trial.getInterest()));
        }
        if (holder.tvLoanApply5 != null) {    //利息
            holder.tvLoanApply5.setText(String.valueOf(trial.getRepay_date()));
        }

//        if (holder.tvLoanApply5 != null) {        //砍头服务费，非砍头产品为0
//            holder.tvLoanApply5.setText(String.valueOf(trial.getInterest_prepaid()));
//        }
//        if (holder.tvLoanApply6 != null) {    //放款金额
//            holder.tvLoanApply6.setText(String.valueOf(trial.getInterest_prepaid()));
//        }
//        if (tvDisburse != null) {    //放款金额
//            tvDisburse.setText(String.valueOf(trial.getDisburse_amount()));
//        }
        //砍头利息，非砍头产品为0
    }

    @Override
    public int getItemCount() {
        return mTrials == null ? 0 : mTrials.size();
    }
}
