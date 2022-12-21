package com.loadapp.load.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseFragment;
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

public class CommitProfileFragment extends BaseFragment {

    private static final String TAG = "CommitProfileFragment";

    private AccountProfileBean.AccountProfile mProfileBean;
    private BaseCommitFragment mCurFragment;
//    private FrameLayout flContainer;

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            return false;
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commit_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProfile();
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
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void switchFragment(int index) {
        if (!isAdded()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    Log.e("Test", "----------- 200");
                    switchFragment(index);
                }
            },200);
            return;
        }
        switch (index) {
            case CommitProfileActivity.PHASE_1:
                mCurFragment = new PersonProfileFragment();
                break;
            case CommitProfileActivity.PHASE_2:
                mCurFragment = new PersonProfile2Fragment();
                break;
            case CommitProfileActivity.PHASE_3:
                mCurFragment = new PersonProfile3Fragment();
                break;
            case CommitProfileActivity.PHASE_4:
                mCurFragment = new BankInfoFragment();
                break;
            case CommitProfileActivity.PHASE_5:

                break;
            case CommitProfileActivity.PHASE_ALL:
                EventBus.getDefault().post(new PhaseAllEvent());
                getActivity().finish();
                break;
            case CommitProfileActivity.PHASE_COLLECT_DATA:
                // 收集信息.
                CollectDataManager.getInstance().collectAuthData(getContext(), new CollectDataManager.Observer() {
                    @Override
                    public void success(Response<String> response) {
                        Log.e(TAG, " upload client info success .");
                        ToastUtils.showShort("modify success");
//                        finish();
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

    private void toFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        transaction.replace(R.id.fl_fragment_person_profile_container, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void onActivityResultInternal(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCurFragment != null && mCurFragment instanceof PersonProfile3Fragment) {
            ((PersonProfile3Fragment) mCurFragment).OnActivityResultInternal(requestCode, data);
        }
    }
}
