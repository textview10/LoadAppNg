package com.loadapp.load.ui.home.status;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.loadapp.load.R;
import com.loadapp.load.bean.OrderInfoBean;

/**
 * 借贷处理中...
 */
public class LoanProcessingFragment extends BaseStatusFragment{

    private ImageView ivStatus;
    private TextView tvStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_proceeing, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivStatus = view.findViewById(R.id.iv_loan_processing_status);
        tvStatus = view.findViewById(R.id.tv_loan_processing_desc);
    }

    public void setOrderInfo(int checkStatus, OrderInfoBean orderInfoBean){
//        android:src="@drawable/ic_loan_refuse"
//        android:text="@string/home_loan_status_refuse"
    }
}
