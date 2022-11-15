package com.loadapp.load.ui.login.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseFragment;
import com.loadapp.load.ui.login.SignUpActivity;
import com.loadapp.load.ui.login.widget.InputVerifyCodeView;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifySmsCodeFragment extends BaseFragment {
    private static final String TAG = "VerifySmsCodeFragment";

    private String mPhoneNum;
    private TextView tvResend;

    private static final int TYPE_TIME_REDUCE = 1111;
    private int MAX_TIME = 60;
    private int mCurTime = MAX_TIME;

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case TYPE_TIME_REDUCE:
                    mCurTime--;
                    if (tvResend != null) {
                        if (mCurTime == MAX_TIME) {
                            tvResend.setText(StringUtils.getString(R.string.Resent_now));
                            tvResend.setTextColor(Color.parseColor("#0EC6A2"));
                            mHandler.sendEmptyMessageDelayed(TYPE_TIME_REDUCE, 1000);
                        } else if (mCurTime == 0) {
                            tvResend.setText(StringUtils.getString(R.string.Resent_now));
                            tvResend.setTextColor(Color.parseColor("#0EC6A2"));
                        } else {
                            String text = StringUtils.getString(R.string.Resent_after)
                                    + " (" + mCurTime + ")";
                            tvResend.setText(text);
                            tvResend.setTextColor(Color.parseColor("#999999"));
                            mHandler.sendEmptyMessageDelayed(TYPE_TIME_REDUCE, 1000);
                        }

                    }
                    break;
            }
            return false;
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_num_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InputVerifyCodeView inputVerifyCodeView = view.findViewById(R.id.view_input_verify_code_verify_code);
        tvResend = view.findViewById(R.id.tv_verify_code_resend);

        TextView tvVerifyDesc = view.findViewById(R.id.tv_verify_num_desc);
        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurTime > 0 && mCurTime <= MAX_TIME) {
                    return;
                }
                inputVerifyCodeView.clearAll();
                mCurTime = MAX_TIME;
                mHandler.sendEmptyMessage(TYPE_TIME_REDUCE);
            }
        });

        inputVerifyCodeView.setObserver(new InputVerifyCodeView.Observer() {
            @Override
            public void onEnd() {
                String verifyCode = inputVerifyCodeView.getVerifyCode();
                if (TextUtils.isEmpty(verifyCode)) {
                    ToastUtils.showShort("verify code error. please input again.");
                    return;
                }
                verifySmsCode(mPhoneNum, verifyCode);
            }
        });

        String result = String.format(StringUtils.getString(R.string.signup_input_desc), mPhoneNum);
        tvVerifyDesc.setText(result);
    }

    private void verifySmsCode(String phoneNum, String verifyCode) {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("mobile", phoneNum);
            //测试验证码：6666
            jsonObject.put("auth_code", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.CHECK_SMS_CODE).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = checkResponseSuccess(response);
                        // TODO
//                        if (TextUtils.isEmpty(responseStr)) {
//                            Log.e(TAG,"verify sms code error");
//                            return;
//                        }
                        if (getActivity() instanceof SignUpActivity) {
                            ((SignUpActivity) getActivity()).toSetPwdPage(phoneNum);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "verify sms failure = " + response.body());
                    }
                });
    }

    public void setPhoneNum(String phoneNum) {
        mPhoneNum = phoneNum;
    }

    @Override
    public void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
