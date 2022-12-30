package com.loadapp.load.ui.profile.fragment.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.loadapp.load.R;
import com.loadapp.load.ui.profile.fragment.BankInfoFragment;

public class SelectTypePopUp extends PopupWindow {

    public SelectTypePopUp(Context context, OnPopUpClickListener listener) {
        super(context);
        View view = createView(context);
        setContentView(view);
        setWidth(ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(30));
        setOutsideTouchable(true);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_round_white_10));

        FrameLayout tvWallet = view.findViewById(R.id.popup_type_wallet);
        FrameLayout tvBank = view.findViewById(R.id.popup_type_bank);
        tvWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(BankInfoFragment.TYPE_WALLET);
                }
                dismiss();
            }
        });
        tvBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(BankInfoFragment.TYPE_BANK);
                }
                dismiss();
            }
        });
    }

    public void show(View view) {
//        )
        showAsDropDown(view ,(int) view.getX(), (int) (view.getY() - ConvertUtils.dp2px(90)));
    }

    private View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.popup_selct_type, null);
        return view;
    }


    public interface OnPopUpClickListener {
        void onClick(int type);
    }
}
