package com.loadapp.load.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.gson.Gson;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.bean.AccountProfileBean;
import com.loadapp.load.bean.BaseResponseBean;
import com.loadapp.load.global.ConfigMgr;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.profile.fragment.BaseCommitFragment;
import com.loadapp.load.ui.profile.fragment.GarantorInfoFragment;
import com.loadapp.load.ui.profile.fragment.PersonProfile2Fragment;
import com.loadapp.load.ui.profile.fragment.PersonProfileFragment;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class CommitProfileActivity extends BaseActivity {

    private ImageView ivBack;
    private TextView tvTitle;

    private static final String TAG = "CommitProfileActivity";

    private AccountProfileBean mProfileBean;
    private BaseCommitFragment mCurFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_profile);

        ivBack = findViewById(R.id.iv_commit_profile_back);
        tvTitle = findViewById(R.id.tv_commit_profile_title);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackInternal();
            }
        });
        getProfile();
        switchFragment(0);
    }

    @Override
    protected int getFragmentContainerRes() {
        return R.id.fl_commit_profile_container;
    }

    public void setTitle(@StringRes int strRes) {
        if (tvTitle != null) {
            tvTitle.setText(strRes);
        }

    }

    public void switchFragment(int index) {
        switch (index) {
            case 0:
                mCurFragment = new PersonProfileFragment();
                break;
            case 1:
                mCurFragment = new GarantorInfoFragment();
                break;
            case 2:
                mCurFragment = new PersonProfile2Fragment();
                break;
        }
        if (mCurFragment != null) {
            if (mProfileBean != null) {
                mCurFragment.setProfileBean(mProfileBean);
            }
            toFragment(mCurFragment);
        }
    }

    private void getProfile() {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("access_token", Constant.mToken);
            jsonObject.put("account_id", Constant.mAccountId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.GET_PROFILE).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        AccountProfileBean profileBean = checkResponseSuccess(response, AccountProfileBean.class);
                        if (profileBean == null) {
                            Log.e(TAG, " get profile error ." + response.body().toString());
                            return;
                        }
                        mProfileBean = profileBean;
                        if (mCurFragment != null) {
                            if (mProfileBean != null) {
                                mCurFragment.setProfileBean(mProfileBean);
                            }
                            toFragment(mCurFragment);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "get profile failure = " + response.body().toString());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        onBackInternal();
    }

    private void onBackInternal(){
        finish();
    }
}
