package com.loadapp.load.ui.home.status.repaydue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;
import com.loadapp.load.bean.OrderInfoBean;

import java.util.List;

public class RepayDueAdapter extends RecyclerView.Adapter<RepayDueHolder> {

    private List<OrderInfoBean.Stage> mStages;

    public RepayDueAdapter(List<OrderInfoBean.Stage> stages) {
        mStages = stages;
    }

    @NonNull
    @Override
    public RepayDueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repay_due, parent, false);
        return new RepayDueHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepayDueHolder holder, int position) {
        OrderInfoBean.Stage stage = mStages.get(position);
        if (stage != null){
            holder.tvAmount.setText(String.valueOf(stage.getAmount()));
            holder.tvIndex.setText(getNum(stage.getStage_no()));
            holder.tvDate.setText(String.valueOf(stage.getRepay_date()));
        }

    }

    private String getNum(int num){
        switch (num){
            case 1:
                return "1st";
            case 2:
                return "2nd";
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return mStages == null ? 0 :mStages.size();
    }
}
