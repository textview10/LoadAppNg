package com.loadapp.load.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.loadapp.load.R;


/**
 * @author xu.wang
 * @date 2019/6/27 17:08
 * @desc 自定义样式的Dialog
 */
public class CustomDialog extends BaseDialog {
    private Context mContext;
    private TextView tvComfirm, tvTitle, tvCancel;
    private FrameLayout flContent;
    private View.OnClickListener positiveListener, negativeListener;
    private LinearLayout llContainer;
    private ImageView ivIcon;
    private ImageView ivClose;

    public CustomDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
        this.mContext = context;
        initializeView();
    }

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        initializeView();
    }

    private void initializeView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_base);
        llContainer = findViewById(R.id.ll_custom_dialog_container);
        ivIcon = findViewById(R.id.iv_dialog_icon);
        tvTitle = findViewById(R.id.tv_dialog_title);
        tvCancel = findViewById(R.id.tv_dialog_cancel);
        tvComfirm = findViewById(R.id.tv_dialog_comfirm);
        flContent = findViewById(R.id.fl_dialog_content);
        ivClose = findViewById(R.id.iv_dialog_close);

        ivClose.setOnClickListener(v -> dismiss());

        tvComfirm.setOnClickListener(v -> {
            if (positiveListener != null) {
                positiveListener.onClick(v);
            }
            dismiss();
        });

        tvCancel.setOnClickListener(v -> {
            if (negativeListener != null) {
                negativeListener.onClick(v);
            }
            dismiss();
        });

        llContainer.setBackgroundResource( R.drawable.shape_corner_light_custom_dialog_bg);

        tvTitle.setTextColor(getContext().getResources().getColor(R.color.custom_dialog_title_text_color_light));
        tvCancel.setTextColor(getContext().getResources().getColor( R.color.custom_dialog_cancel_text_color_light));
        tvComfirm.setTextColor(getContext().getResources().getColor( R.color.custom_dialog_confirm_text_color_light));
    }

    public CustomDialog setView(@LayoutRes int res) {
        return setView(LayoutInflater.from(getContext()).inflate(res, flContent, false));
    }

    public CustomDialog setMessage(String msg) {
        if (flContent.getChildCount() != 0) {
            flContent.removeAllViews();
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_base_text, flContent, false);
        TextView tvDesc = view.findViewById(R.id.tv_base_desc);
        tvDesc.setText(msg);
        tvDesc.setTextColor(getContext().getResources().getColor( R.color.custom_dialog_text_color_light));
        flContent.addView(view);
        return this;
    }


    public CustomDialog setView(View view) {
        if (flContent.getChildCount() != 0) {
            flContent.removeAllViews();
        }
        flContent.addView(view);
        return this;
    }

    public CustomDialog setTitle(String title) {
        if (tvTitle.getVisibility() != View.VISIBLE){
            tvTitle.setVisibility(View.VISIBLE);
        }
        tvTitle.setText(title);
        return this;
    }

    public CustomDialog setIcon(@DrawableRes int res) {
        if (ivIcon.getVisibility() != View.VISIBLE){
            ivIcon.setVisibility(View.VISIBLE);
        }
        ivIcon.setImageResource(res);
        return this;
    }

    public CustomDialog setPositiveButton(String text, View.OnClickListener listener) {
        tvComfirm.setVisibility(View.VISIBLE);
        tvComfirm.setText(text);
        this.positiveListener = listener;
        return this;
    }

    public CustomDialog setNegativeButton(String text, View.OnClickListener listener) {
        tvCancel.setVisibility(View.VISIBLE);
        tvCancel.setText(text);
        this.negativeListener = listener;
        return this;
    }

    @Override
    public void show() {
        if (mContext != null && mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.isFinishing() || activity.isDestroyed()) {
                    return;
                }
            }
        }
        super.show();
    }
}
