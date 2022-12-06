package com.loadapp.load.ui.home;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.bean.BaseConfigBean;
import com.loadapp.load.bean.LoginResponseBean;
import com.loadapp.load.collect.CollectDataManager;
import com.loadapp.load.collect.FireBaseMgr;
import com.loadapp.load.dialog.requestpermission.RequestPermissionDialog;
import com.loadapp.load.global.ConfigMgr;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.home.widget.BottomTabLayout;
import com.loadapp.load.ui.login.SignInActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";

    private BottomTabLayout mBottomLayout;
    private LoanFragment loanFragment;
    private MeFragment meFragment;
    private ViewPager vpMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeView();
        initializeData();
        requestPermission();
        ConfigMgr.getAllConfig();
    }

    private void initializeView() {
        mBottomLayout = findViewById(R.id.main_bottom_tablayout);
        vpMain = findViewById(R.id.vp_main_container);
        mBottomLayout.setOnTabChangeListener(new BottomTabLayout.OnTabChangeListener() {
            @Override
            public void onTabChange(int index) {
                updateSelectFragment(index);
            }
        });
        vpMain.setAdapter(mMainAdapter);
    }

    private void initializeData() {
        loanFragment = new LoanFragment();
        meFragment = new MeFragment();

        vpMain.setCurrentItem(0,false);

        SPUtils.getInstance().put(Constant.KEY_ACCOUNT_ID, Constant.mAccountId);
        SPUtils.getInstance().put(Constant.KEY_TOKEN, Constant.mToken);

        FireBaseMgr.getInstance().reportFcmToken(this);
    }

    private void requestPermission() {
        boolean hasPermission = PermissionUtils.isGranted(PermissionConstants.LOCATION,PermissionConstants.CAMERA,
                PermissionConstants.SMS, PermissionConstants.CALENDAR, PermissionConstants.CONTACTS);
        if (false && hasPermission) {
            executeNext();
        } else {
            requestPermissionInternal();
        }
    }

    private void requestPermissionInternal(){
        RequestPermissionDialog dialog = new RequestPermissionDialog(this);
        dialog.setOnItemClickListener(new RequestPermissionDialog.OnItemClickListener() {
            @Override
            public void onClickAgree() {
                PermissionUtils.permission(Manifest.permission.READ_CALENDAR, Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA).callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        executeNext();
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort("please allow permission.");
                    }
                }).request();
            }
        });
        dialog.show();
    }

    private void executeNext() {

    }

    private void updateSelectFragment(int index) {
        vpMain.setCurrentItem(index, false);
    }

    private FragmentPagerAdapter mMainAdapter = new FragmentPagerAdapter(getSupportFragmentManager()
    , BEHAVIOR_SET_USER_VISIBLE_HINT) {
        @Override
        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return loanFragment;
            } else {
                return meFragment;
            }
        }
    } ;
}
