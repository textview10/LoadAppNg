package com.loadapp.load.dialog.producttrial;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;

public class ProductTrialHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView tvTitle, tvDesc1, tvDesc2;

    public ProductTrialHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tv_item_product_trial_title);
        tvDesc1 = itemView.findViewById(R.id.tv_item_product_trial_desc1);
        tvDesc2 = itemView.findViewById(R.id.tv_item_product_trial_desc2);
    }
}
