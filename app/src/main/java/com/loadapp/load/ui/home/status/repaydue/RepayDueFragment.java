package com.loadapp.load.ui.home.status.repaydue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loadapp.load.R;
import com.loadapp.load.bean.OrderInfoBean;
import com.loadapp.load.ui.home.status.BaseStatusFragment;

import java.util.ArrayList;
import java.util.List;

public class RepayDueFragment extends BaseStatusFragment {

    private AppCompatTextView tvAmount, tvDate;
    private FrameLayout flRepayCommit;
    private RecyclerView rvContent;
    private List<OrderInfoBean.Stage> mStages = new ArrayList<>();
    private RepayDueAdapter mAdapter;
    private boolean mIsDelay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repay_due, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAmount = view.findViewById(R.id.tv_repay_due_amount);
        tvDate = view.findViewById(R.id.tv_repay_due_date);
        flRepayCommit = view.findViewById(R.id.fl_repay_due_repay);
        rvContent = view.findViewById(R.id.rv_repay_due_content);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new RepayDueAdapter(mStages);
        rvContent.setLayoutManager(manager);
        rvContent.setAdapter(mAdapter);

        flRepayCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void setData(List<OrderInfoBean.Stage> stages, boolean isDelay){
        if (stages != null) {
            mStages.clear();
            mStages.addAll(stages);
            updateDataInternal();
        }
        mIsDelay = isDelay;
    }

    private void updateDataInternal(){
        if (mStages.size() > 0){
            OrderInfoBean.Stage stage = mStages.get(0);
            tvAmount.setText(String.valueOf(stage.getAmount()));
            tvDate.setText(String.valueOf(stage.getRepay_date()));
        }
        mAdapter.setIsDelay(mIsDelay);
        mAdapter.notifyDataSetChanged();
    }
}
