package com.loadapp.load.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.bean.AccountProfileBean;
import com.loadapp.load.bean.event.PhaseAllEvent;
import com.loadapp.load.collect.CollectDataManager;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.profile.fragment.BankInfoFragment;
import com.loadapp.load.ui.profile.fragment.BaseCommitFragment;
import com.loadapp.load.ui.profile.fragment.PersonProfile2Fragment;
import com.loadapp.load.ui.profile.fragment.PersonProfile3Fragment;
import com.loadapp.load.ui.profile.fragment.PersonProfileFragment;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class CommitProfileActivity extends BaseActivity {

    private ImageView ivBack;
    private TextView tvTitle;

    private static final String TAG = "CommitProfileActivity";

    public static final String INTENT_PHASE = "intent_phase";

    public static final int PHASE_1 = 101;
    public static final int PHASE_2 = 102;
    public static final int PHASE_3 = 103;
    public static final int PHASE_4 = 104;
    public static final int PHASE_5 = 105;
    public static final int PHASE_COLLECT_DATA = 106;
    public static final int PHASE_ALL = 110;

    private AccountProfileBean.AccountProfile mProfileBean;
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
        initPage();
    }

    public static void startActivity(Context context , int phaseIndex){
        Intent intent = new Intent(context, CommitProfileActivity.class);
        intent.putExtra(INTENT_PHASE, phaseIndex);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initPage();
    }

    private void initPage(){
        getProfile();
        int phaseIndex = getIntent().getIntExtra(INTENT_PHASE, PHASE_1);
        switchFragment(phaseIndex);
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
            case PHASE_1:
                mCurFragment = new PersonProfileFragment();
                break;
            case PHASE_2:
                mCurFragment = new PersonProfile2Fragment();
                break;
            case PHASE_3:
                mCurFragment = new PersonProfile3Fragment();
                break;
            case PHASE_4:
                mCurFragment = new BankInfoFragment();
                break;
            case PHASE_5:

                break;
            case PHASE_ALL:
                EventBus.getDefault().post(new PhaseAllEvent());
                finish();
                break;
            case PHASE_COLLECT_DATA:
                // 收集信息.
                CollectDataManager.getInstance().collectAuthData(this, new CollectDataManager.Observer() {
                    @Override
                    public void success(Response<String> response) {
                        Log.e(TAG, " upload client info success .");
                        ToastUtils.showShort("modify success");
                        finish();
                    }

                    @Override
                    public void failure(Response<String> response) {
                        Log.e(TAG, " upload client info failure .");
                    }
                });
                return;
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
                        if (profileBean == null || profileBean.getAccount_profile() == null) {
                            Log.e(TAG, " get profile error ." + response.body());
                            return;
                        }
                        mProfileBean = profileBean.getAccount_profile();
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
                        Log.e(TAG, "get profile failure = " + response.body());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        onBackInternal();
    }

    private void onBackInternal() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCurFragment != null && mCurFragment instanceof PersonProfile3Fragment) {
            ((PersonProfile3Fragment) mCurFragment).OnActivityResultInternal(requestCode, data);
        }
    }
}
