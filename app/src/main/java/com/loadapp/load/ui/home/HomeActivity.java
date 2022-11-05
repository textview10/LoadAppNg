package com.loadapp.load.ui.home;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.loadapp.load.R;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.ui.home.widget.BottomTabLayout;

public class HomeActivity extends BaseActivity {

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
        Log.e("wangxu", "update = " + index );
        vpMain.setCurrentItem(index, false);
//        if (index == 0) {
//            toFragment(loanFragment);
//        } else if (index == 1) {
//            toFragment(meFragment);
//        }
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
            Log.e("Wangxu", "position = " + position);
            if (position == 0){
                return loanFragment;
            } else {
                return meFragment;
            }
        }
    } ;
}
