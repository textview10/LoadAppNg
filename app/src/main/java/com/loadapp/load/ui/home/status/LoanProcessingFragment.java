package com.loadapp.load.ui.home.status;

import android.os.Bundle;
import android.text.TextUtils;
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

    private int mCheckStatus = -1;
    private OrderInfoBean mOrderInfo;

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
        updateInfoInternal();
    }

    public void setOrderInfo(int checkStatus, OrderInfoBean orderInfoBean){
        mCheckStatus = checkStatus;
        mOrderInfo = orderInfoBean;
        updateInfoInternal();
    }

    private void updateInfoInternal(){
        if (mCheckStatus != -1){
            if (ivStatus != null && tvStatus != null){
                String str = "";
                switch (mCheckStatus){
                    case 1: //已提交待审核
                    case 4: //等待放款
                    case 5: //放款中
                        ivStatus.setImageResource(R.drawable.ic_loan_processing);
                        str = getContext().getString(R.string.home_loan_status_processing);
                        break;
                    case 2: //审核拒绝
                        ivStatus.setImageResource(R.drawable.ic_loan_refuse);
                        str = getContext().getString(R.string.home_loan_status_refuse);
                        if (mOrderInfo != null && !TextUtils.isEmpty(mOrderInfo.getReject_message())){
                            str = mOrderInfo.getReject_message();
                        }
                        break;
                    case 3: //等待人工审核
                    case 6: //放款失败
                        ivStatus.setImageResource(R.drawable.ic_loan_processing);
                        str = getContext().getString(R.string.home_loan_status_need_comfire);
                        break;
                }
                tvStatus.setText(str);
            }
        }
    }
}
