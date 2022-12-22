package com.loadapp.load.ui.login.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.SPUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseFragment;
import com.loadapp.load.bean.LoginResponseBean;
import com.loadapp.load.global.Constant;
import com.loadapp.load.presenter.PhoneNumPresenter;
import com.loadapp.load.ui.login.SignInActivity;
import com.loadapp.load.ui.login.SignUpActivity;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class LoginFragment extends BaseFragment {
    private static final String TAG = "LoginFragment";

    private FrameLayout flCommit;
    private AppCompatEditText etAccount ,etSetPwd;
    private ImageView ivPwdShow;

    public static final String KEY_PHONE_NUM = "key_sign_in_phone_num";
    public static final String KEY_PASS_CODE = "key_sign_in_pass_code";

    private Spinner spinner;
    private PhoneNumPresenter mPresenter;
    private boolean passwordMode = true;
    private ImageView ivBack;
    private ProgressBar pbLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flCommit = view.findViewById(R.id.fl_signin_commit);
        etAccount = view.findViewById(R.id.et_signin_account);
        etSetPwd = view.findViewById(R.id.et_signin_set_pwd);
        ivPwdShow = view.findViewById(R.id.iv_signin_set_pwd_show);
        spinner = view.findViewById(R.id.spinner_signin_input);
        ivBack = view.findViewById(R.id.iv_verify_back);

        pbLoading = view.findViewById(R.id.pb_login_loading);

        String phoneNum = SPUtils.getInstance().getString(KEY_PHONE_NUM);
        String passCode = SPUtils.getInstance().getString(KEY_PASS_CODE);
        if (!TextUtils.isEmpty(phoneNum)) {
            etAccount.setText(phoneNum);
            etAccount.setSelection(phoneNum.length() - 1);
        }
        if (!TextUtils.isEmpty(passCode)) {
            etSetPwd.setText(passCode);
            etSetPwd.setSelection(passCode.length() - 1);
        }
        mPresenter = new PhoneNumPresenter(getContext());
        mPresenter.initSpinner(spinner);

        etSetPwd.setTransformationMethod(passwordMode ? PasswordTransformationMethod.getInstance()
                : HideReturnsTransformationMethod.getInstance());

        flCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = etAccount.getText().toString();
                if (TextUtils.isEmpty(account)){
                    return;
                }
                String pwd = etSetPwd.getText().toString();
                if (TextUtils.isEmpty(pwd)){
                    return;
                }

                login(account,pwd);
            }
        });
        ivPwdShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordMode = !passwordMode;
                if (etSetPwd != null) {
                    etSetPwd.setTransformationMethod(passwordMode ? PasswordTransformationMethod.getInstance()
                            : HideReturnsTransformationMethod.getInstance());
                }
                if (ivPwdShow != null) {
                    ivPwdShow.setImageResource(passwordMode ? R.drawable.ic_setpwd_noshow
                            : R.drawable.ic_setpwd_show);
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof SignInActivity) {
                    ((SignInActivity) getActivity()).backPress();
                }
            }
        });
    }

    private void login(String phoneNum, String password) {
        if (pbLoading != null){
            pbLoading.setVisibility(View.VISIBLE);
        }
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            String prefix = mPresenter.getSelectString(spinner.getSelectedItemPosition());
            // TODO 暂时去掉前缀
//            jsonObject.put("mobile", prefix + phoneNum);
            jsonObject.put("mobile", phoneNum);
            jsonObject.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("okhttp", jsonObject.toString());
        OkGo.<String>post(Api.LOGIN).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (pbLoading != null){
                            pbLoading.setVisibility(View.GONE);
                        }

                        LoginResponseBean loginBean = checkResponseSuccess(response, LoginResponseBean.class);
                        if (loginBean == null) {
                            Log.e(TAG,"login error");
                            return;
                        }
                        Constant.mAccountId = loginBean.getAccount_id();
                        Constant.mToken = loginBean.getAccess_token();
                        SPUtils.getInstance().put(KEY_PHONE_NUM, phoneNum);
                        SPUtils.getInstance().put(KEY_PASS_CODE, password);
                        Log.e(TAG, "login success = " + response.body());
//                        Log.e(TAG, "login success 1 = " + Constant.mToken);
//                        Log.e(TAG, "login success 2 = " + Constant.mAccountId);
//                        modifyPassword("aa123456", "ab123456", "ab123456");
                        if (getActivity() instanceof SignInActivity) {
                            ((SignInActivity) getActivity()).toHomePage();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (pbLoading != null){
                            pbLoading.setVisibility(View.GONE);
                        }
                        Log.e(TAG, "login failure = " + response.body());
                    }
                });
    }

    @Override
    public void onDestroy() {
        OkGo.getInstance().cancelTag(TAG);
        super.onDestroy();
    }
}
