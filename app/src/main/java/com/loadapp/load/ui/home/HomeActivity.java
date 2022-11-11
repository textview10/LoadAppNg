package com.loadapp.load.ui.home;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.SPUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.bean.BaseConfigBean;
import com.loadapp.load.bean.LoginResponseBean;
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
