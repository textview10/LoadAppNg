package com.loadapp.load.ui.login.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.ColorUtils;
import com.loadapp.load.R;

public class InputVerifyCodeView extends LinearLayout {

    private AppCompatEditText etNum1, etNum2, etNum3, etNum4;
    private View view1, view2, view3, view4;
    private int selectedColor, unselectedColor;

    public InputVerifyCodeView(Context context) {
        super(context);
    }

    public InputVerifyCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InputVerifyCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        etNum1 = findViewById(R.id.et_verify_num_1);
        etNum2 = findViewById(R.id.et_verify_num_2);
        etNum3 = findViewById(R.id.et_verify_num_3);
        etNum4 = findViewById(R.id.et_verify_num_4);

        view1 = findViewById(R.id.view_verify_num_1);
        view2 = findViewById(R.id.view_verify_num_2);
        view3 = findViewById(R.id.view_verify_num_3);
        view4 = findViewById(R.id.view_verify_num_4);

        etNum1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                updateViewIndicate(0);
            }
        });
        etNum2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                updateViewIndicate(1);
            }
        });
        etNum3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                updateViewIndicate(2);
            }
        });
        etNum4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                updateViewIndicate(3);
            }
        });
        etNum1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onTextChange(etNum1, etNum2);
            }
        });
        etNum2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onTextChange(etNum2, etNum3);
            }
        });
        etNum3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onTextChange(etNum3, etNum4);
            }
        });
        etNum4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onTextChange(etNum4, null);
            }
        });
        selectedColor = ColorUtils.getColor(R.color.verify_sms_select);
        unselectedColor = ColorUtils.getColor(R.color.verify_sms_unselect);
        etNum1.requestFocus();
    }

    private void onTextChange(AppCompatEditText et1, AppCompatEditText et2) {
        String text = et1.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            int length = text.length();
            if (length > 0) {
                if (length > 1) {
                    et1.setText(text.substring(0, 1));
                } else {

                }
                if (et2 != null) {
                    et2.requestFocus();
                }
            }
        }
        checkInputComplete();
    }

    public String getVerifyCode(){
        String s1 = getVerifyItemCode(etNum1);
        String s2 = getVerifyItemCode(etNum2);
        String s3 = getVerifyItemCode(etNum3);
        String s4 = getVerifyItemCode(etNum4);
        if (TextUtils.isEmpty(s1) || TextUtils.isEmpty(s2)
            || TextUtils.isEmpty(s3) || TextUtils.isEmpty(s4)){
            return "";
        }
        return s1 + s2 + s3 + s4;
    }

    private String getVerifyItemCode(AppCompatEditText et){
        if (et != null && et.getText() != null ){
            if (et.getText().toString().length() == 0) {
                return null;
            } else if (et.getText().toString().length() == 1) {
                return et.getText().toString();
            } else {
                return et.getText().toString().substring(0, 1);
            }
        }
        return null;
    }

    private boolean checkInputComplete() {
        if (checkInputItemComplete(etNum1) && checkInputItemComplete(etNum2)
                && checkInputItemComplete(etNum3) && checkInputItemComplete(etNum4)) {
            if (mObserver != null){
                mObserver.onEnd();
            }
            return true;
        }
        return false;
    }

    private boolean checkInputItemComplete(AppCompatEditText et) {
        if (et == null || et.getText() == null) {
            return false;
        }
        if (TextUtils.isEmpty(et.getText().toString())) {
            return false;
        }
        if (et.getText().toString().length() != 1) {
            return false;
        }
        return true;
    }

    private void updateViewIndicate(int index) {
        view1.setBackgroundColor(unselectedColor);
        view2.setBackgroundColor(unselectedColor);
        view3.setBackgroundColor(unselectedColor);
        view4.setBackgroundColor(unselectedColor);
        switch (index) {
            case 0:
                view1.setBackgroundColor(selectedColor);
                break;
            case 1:
                view2.setBackgroundColor(selectedColor);
                break;
            case 2:
                view3.setBackgroundColor(selectedColor);
                break;
            case 3:
                view4.setBackgroundColor(selectedColor);
                break;
        }
    }

    public void clearAll() {
        etNum1.setText("");
        etNum2.setText("");
        etNum3.setText("");
        etNum4.setText("");
        etNum1.requestFocus();
    }

    private Observer mObserver;

    public void setObserver(Observer observer){
        mObserver = observer;
    }

    public interface Observer {
        void onEnd();
    }
}
