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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.BarUtils;
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

import java.util.List;

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
    private CommitProfileFragment commitProfileFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarVisibility(this, false);

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

    public static void startActivity(Context context, int phaseIndex) {
        Intent intent = new Intent(context, CommitProfileActivity.class);
        intent.putExtra(INTENT_PHASE, phaseIndex);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initPage();
    }

    private void initPage() {
        int phaseIndex = getIntent().getIntExtra(INTENT_PHASE, PHASE_1);
        commitProfileFragment = new CommitProfileFragment();
        addFragment(commitProfileFragment, "commitProfile");
        commitProfileFragment.switchFragment(phaseIndex);
    }

    public void switchFragment(int curPhase) {
        if (commitProfileFragment != null) {
            commitProfileFragment.switchFragment(curPhase);
        }
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

    @Override
    public void onBackPressed() {
        onBackInternal();
    }

    private void onBackInternal() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 1) {
            if (tvTitle != null) {
                tvTitle.setText(R.string.loan_person_profile_title);
            }
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            if (commitProfileFragment != null) {
                boolean onBackFlag = commitProfileFragment.onBackInternal();
                if (onBackFlag) {
                    return;
                }
            }
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (commitProfileFragment != null && commitProfileFragment instanceof CommitProfileFragment) {
            (commitProfileFragment).onActivityResultInternal(requestCode,
                    resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
        super.onDestroy();
    }
}
