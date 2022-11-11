package com.loadapp.load.ui.profile;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.loadapp.load.R;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.ui.home.profile.PersonProfileFragment;

public class CommitProfileActivity extends BaseActivity {

    private ImageView ivBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_profile);

        ivBack = findViewById(R.id.iv_commit_profile_back);
        tvTitle = findViewById(R.id.tv_commit_profile_title);
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

    private void switchFragment(int index) {
        switch (index) {
            case 0:
                PersonProfileFragment personProfileFragment = new PersonProfileFragment();
                toFragment(personProfileFragment);
                break;
        }
    }
}
