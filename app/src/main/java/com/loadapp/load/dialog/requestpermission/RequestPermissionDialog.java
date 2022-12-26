package com.loadapp.load.dialog.requestpermission;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.loadapp.load.R;

public class RequestPermissionDialog extends Dialog {

    public RequestPermissionDialog(@NonNull Context context) {
        super(context);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;    //设置高度
        lp.horizontalMargin = 0;
        lp.verticalMargin = 0;
        getWindow().setAttributes(lp);

        setContentView(R.layout.dialog_request_permission);
        TextView tvCancel = findViewById(R.id.tv_request_permission_cancel);
        TextView tvAgree = findViewById(R.id.tv_request_permission_agree);

        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClickAgree();
                }
                dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClickCancel();
                }
                dismiss();
            }
        });
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public static abstract class OnItemClickListener {
        public abstract void onClickAgree();

        public void onClickCancel(){

        }
    }
}
