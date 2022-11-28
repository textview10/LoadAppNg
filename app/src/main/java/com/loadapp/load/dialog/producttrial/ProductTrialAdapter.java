package com.loadapp.load.dialog.producttrial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;
import com.loadapp.load.bean.ProductTrialBean;

import java.util.ArrayList;

public class ProductTrialAdapter extends RecyclerView.Adapter<ProductTrialHolder> {

    private ArrayList<ProductTrialBean.Trial> mList;


    public void setList(ArrayList<ProductTrialBean.Trial> list) {
        mList = list;
    }

    @NonNull
    @Override
    public ProductTrialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_trial, parent, false);
        return new ProductTrialHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTrialHolder holder, int position) {
        ProductTrialBean.Trial trial = mList.get(position);
        if (trial != null) {
            holder.tvTitle.setText(getTitle(trial.getStage_no()));
            holder.tvDesc1.setText("Due date : " + trial.getRepay_date());
            holder.tvDesc2.setText("Amount due : " + trial.getAmount());
        }
    }

    private String getTitle(int pos) {
        switch (pos) {
            case 2:
                return "2st installment";
            case 1:
                return "1st installment";

        }
        return "";
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

}
