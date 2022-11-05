package com.loadapp.load.ui.home.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ColorUtils;
import com.loadapp.load.R;

public class BottomTabLayout extends LinearLayout {

    private LinearLayout llLoanContainer, llMeContainer;
    private ImageView ivLoan, ivMe;
    private TextView tvLoan, tvMe;

    private OnTabChangeListener mListener;

    private int mCurIndex = 0;

    public BottomTabLayout(Context context) {
        super(context);
    }

    public BottomTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        llLoanContainer = findViewById(R.id.ll_main_loan_container);
        llMeContainer = findViewById(R.id.ll_main_me_container);
        ivLoan = findViewById(R.id.iv_main_loan);
        ivMe = findViewById(R.id.iv_main_me);
        tvLoan = findViewById(R.id.tv_main_loan);
        tvMe = findViewById(R.id.tv_main_me);

        llLoanContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurIndex = 0;
                if (mListener != null) {
                    mListener.onTabChange(mCurIndex);
                }
                updateTabInternal(mCurIndex);
            }
        });
        llMeContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurIndex = 1;
                if (mListener != null) {
                    mListener.onTabChange(mCurIndex);
                }
                updateTabInternal(mCurIndex);
            }
        });
    }

    private void updateTabInternal(int index){
        if (index == 0){
            ivLoan.setImageResource(R.drawable.ic_home_loan_select);
            ivMe.setImageResource(R.drawable.ic_home_me_unselect);
            tvLoan.setTextColor(ColorUtils.getColor(R.color.select_green));
            tvMe.setTextColor(ColorUtils.getColor(R.color.unselect_gray));
        } else if (index == 1) {
            ivLoan.setImageResource(R.drawable.ic_home_loan_unselect);
            ivMe.setImageResource(R.drawable.ic_home_me_select);
            tvMe.setTextColor(ColorUtils.getColor(R.color.select_green));
            tvLoan.setTextColor(ColorUtils.getColor(R.color.unselect_gray));
        }
    }

    public void setOnTabChangeListener(OnTabChangeListener listener) {
        mListener = listener;
    }

    public interface OnTabChangeListener {
        void onTabChange(int index);
    }
}
