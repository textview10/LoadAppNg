package com.loadapp.load.ui.login.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.base.BaseFragment;
import com.loadapp.load.presenter.PhoneNumPresenter;
import com.loadapp.load.ui.login.SignUpActivity;
import com.loadapp.load.ui.webview.WebViewFragment;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class InputPhoneNumFragment extends BaseFragment {

    private static final String TAG = "InputPhoneNumFragment";
    private AppCompatEditText etInput;
    private PhoneNumPresenter presenter;
    private Spinner spinner;
    private WebViewFragment webViewFragment;
    private ImageView checkAgree;
    private boolean isAgree = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inputView = inflater.inflate(R.layout.fragment_input_phone_num, container, false);
        return inputView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.spinner_signup_input);
        etInput = view.findViewById(R.id.et_signup_input);
        RelativeLayout rlCommit = view.findViewById(R.id.rl_signup_input_commit);

        checkAgree = view.findViewById(R.id.iv_signup_agree);
        TextView tvTerm = view.findViewById(R.id.tv_signup_term);

        presenter = new PhoneNumPresenter(getContext());
        presenter.initSpinner(spinner);

        rlCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAgree){
                    ToastUtils.showShort(" must agree terms");
                    return;
                }
                boolean isNotAvailable = checkPhoneNumLocal(etInput);
                if (isNotAvailable) {
                    ToastUtils.showShort("phone num is not input.");
                    return;
                }
                String mobile = etInput.getText().toString();
                checkPhoneNumAvailable(mobile);
            }
        });

        checkAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAgree = !isAgree;
                updateAgreeState();
            }
        });
        tvTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity activity = (SignUpActivity) getActivity();
                activity.toWebView();
            }
        });
    }

    private boolean checkPhoneNumLocal(AppCompatEditText et) {
        if (et == null || et.getText() == null){
            return true;
        }
        if (TextUtils.isEmpty(et.getText().toString())){
            return true;
        }
        return false;
    }

    private void checkPhoneNumAvailable(String mobile){
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            String prefix = presenter.getSelectString(spinner.getSelectedItemPosition());
//            jsonObject.put("mobile", prefix + mobile);
            // TODO 暂时去掉前缀
            jsonObject.put("mobile", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.CHECK_MOBILE).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = checkResponseSuccess(response);
                        if (TextUtils.isEmpty(responseStr)) {
//                            ToastUtils.showShort("mobile num is not correct.");
                            return;
                        }
                        sendSmsCode(mobile);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "check mobile response error = " + response.body());
                    }
                });
    }

    private void sendSmsCode(String phoneNum){
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("mobile", phoneNum);
            //1是注册
            jsonObject.put("send_type", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.GET_SMS_CODE).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = checkResponseSuccess(response);
                        // TODO
//                        if (TextUtils.isEmpty(responseStr)) {
//                            Log.e(TAG,"send sms code error");
//                            return;
//                        }
                        if (getActivity() instanceof SignUpActivity){
                            ((SignUpActivity) getActivity()).toVerifySmsCodePage(phoneNum);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "send sms request error = " + response.body());
                    }
                });
    }

    private void updateAgreeState(){
        if (checkAgree != null){
            checkAgree.setImageResource(isAgree ? R.drawable.ic_selected : R.drawable.ic_unselected);
        }
    }
}
