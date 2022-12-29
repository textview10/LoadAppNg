package com.loadapp.load.ui.home.status.loanapply.adapter;

import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;

public class LoanApplyHolder2 extends RecyclerView.ViewHolder {

    public AppCompatTextView tvTitle, tvLoanApply1, tvLoanApply2, tvLoanApply3, tvLoanApply4, tvLoanApply5, tvLoanApply6;

    public LoanApplyHolder2(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tv_loan_apply_title);
        tvLoanApply1 = itemView.findViewById(R.id.tv_loan_apply_1);
        tvLoanApply2 = itemView.findViewById(R.id.tv_loan_apply_2);
        tvLoanApply3 = itemView.findViewById(R.id.tv_loan_apply_3);
        tvLoanApply4 = itemView.findViewById(R.id.tv_loan_apply_4);
        tvLoanApply5 = itemView.findViewById(R.id.tv_loan_apply_5);

    }
}
