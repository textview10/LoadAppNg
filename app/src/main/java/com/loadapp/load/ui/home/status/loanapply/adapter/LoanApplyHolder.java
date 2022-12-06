package com.loadapp.load.ui.home.status.loanapply.adapter;

import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;

public class LoanApplyHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvAmount;
    public FrameLayout flBg;

    public LoanApplyHolder(@NonNull View itemView) {
        super(itemView);
        tvAmount = itemView.findViewById(R.id.tv_item_load_apply_amount);
        flBg = itemView.findViewById(R.id.fl_item_load_apply_bg);
    }
}
