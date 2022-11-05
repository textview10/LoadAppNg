package com.loadapp.load.base;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.loadapp.load.R;
import com.loadapp.load.global.AppManager;

public abstract class BaseActivity extends AppCompatActivity {

//    public void toFragment(BaseFragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
//        transaction.replace(getFragmentContainerRes(), fragment);
//        transaction.commitAllowingStateLoss();
//    }
//
//    protected @IdRes
//    int getFragmentContainerRes() {
//        return R.id.fl_main_container;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppManager.getAppManager().addActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }
}
