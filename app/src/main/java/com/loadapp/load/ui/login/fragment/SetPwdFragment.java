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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseFragment;
import com.loadapp.load.bean.LoginResponseBean;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.login.SignUpActivity;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class SetPwdFragment extends BaseFragment {
    private static final String TAG = "SetPwdFragment";

    private String mPhoneNum;
    private AppCompatEditText et1, et2;
    private ImageView ivShow1, ivShow2;

    private boolean isPwdMode1 = true, isPwdMode2 = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_pwd, container, false);
        return view;
    }

    public void setPhoneNum(String phoneNum) {
        mPhoneNum = phoneNum;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FrameLayout flCommit = view.findViewById(R.id.fl_set_pwd_commit);

        flCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strPassCode1 = et1.getText().toString();
                String strPassCode2 = et2.getText().toString();
                if (TextUtils.isEmpty(strPassCode1) || TextUtils.isEmpty(strPassCode2)){
                    ToastUtils.showShort("password is null");
                    return;
                }
                if (!TextUtils.equals(strPassCode1, strPassCode2)){
                    ToastUtils.showShort(" two passwords not equal");
                    et2.requestFocus();
                    et2.setSelection(strPassCode2.length());
                    return;
                }
                register(mPhoneNum, strPassCode2);
            }
        });

        et1 = view.findViewById(R.id.et_set_pwd_1);
        et2 = view.findViewById(R.id.et_set_pwd_2);
        ivShow1 = view.findViewById(R.id.iv_set_pwd_show1);
        ivShow2 = view.findViewById(R.id.iv_set_pwd_show2);

        et1.setTransformationMethod(isPwdMode1 ? PasswordTransformationMethod.getInstance()
                : HideReturnsTransformationMethod.getInstance());
        et2.setTransformationMethod(isPwdMode2 ? PasswordTransformationMethod.getInstance()
                : HideReturnsTransformationMethod.getInstance());

        ivShow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPwdMode1 = !isPwdMode1;
                if (et1 != null) {
                    et1.setTransformationMethod(isPwdMode1 ? PasswordTransformationMethod.getInstance()
                            : HideReturnsTransformationMethod.getInstance());
                }
                if (ivShow1 != null) {
                    ivShow1.setImageResource(isPwdMode1 ? R.drawable.ic_setpwd_noshow
                            : R.drawable.ic_setpwd_show);
                }
            }
        });
        ivShow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPwdMode2 = !isPwdMode2;
                if (et2 != null) {
                    et2.setTransformationMethod(isPwdMode2 ? PasswordTransformationMethod.getInstance()
                            : HideReturnsTransformationMethod.getInstance());
                }
                if (ivShow2 != null) {
                    ivShow2.setImageResource(isPwdMode2 ? R.drawable.ic_setpwd_noshow
                            : R.drawable.ic_setpwd_show);
                }
            }
        });
    }

    private void register(String phoneNum, String password){
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("mobile", phoneNum);
            //测试验证码：6666
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.REGISTER).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LoginResponseBean loginBean = checkResponseSuccess(response, LoginResponseBean.class);
                        if (loginBean == null) {
                            Log.e(TAG,"register error");
                            return;
                        }
                        Constant.mAccountId = loginBean.getAccount_id();
                        Constant.mToken = loginBean.getAccess_token();
                        if (getActivity() instanceof SignUpActivity) {
                            ((SignUpActivity) getActivity()).toHomePage();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "register failure = " + response.body().toString());
                    }
                });
    }
}
