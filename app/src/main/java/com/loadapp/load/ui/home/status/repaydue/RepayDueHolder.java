package com.loadapp.load.ui.home.status.repaydue;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;
import com.loadapp.load.ui.home.widget.IndexView;

public class RepayDueHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvIndex, tvAmount, tvDate, tvStatus;
    public IndexView indexView;

    public RepayDueHolder(@NonNull View itemView) {
        super(itemView);
        tvIndex = itemView.findViewById(R.id.tv_item_repay_due_index);
        indexView = itemView.findViewById(R.id.index_view_item_repay_due);
        tvAmount = itemView.findViewById(R.id.tv_item_repay_due_amount);
        tvDate = itemView.findViewById(R.id.tv_item_repay_due_date);
        tvStatus = itemView.findViewById(R.id.tv_item_repay_due_status);


    }
}
