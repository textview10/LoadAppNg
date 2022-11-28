package com.loadapp.load.ui.home.status.loanapply.adapter;

import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;

public class LoanApplyHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvName, tvAmount, tvCount, tvCount2;
    public FrameLayout flCommit;

    public LoanApplyHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_item_load_apply_name);
        tvAmount = itemView.findViewById(R.id.tv_item_load_apply_amount);
        tvCount = itemView.findViewById(R.id.tv_item_load_apply_count);
        tvCount2 = itemView.findViewById(R.id.tv_item_load_apply_count2);
        flCommit = itemView.findViewById(R.id.fl_item_loan_apply_commit);
    }
}
