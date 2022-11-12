package com.loadapp.load.ui.profile.fragment;

import com.loadapp.load.base.BaseFragment;
import com.loadapp.load.bean.AccountProfileBean;

public class BaseCommitFragment extends BaseFragment {

    protected AccountProfileBean mProfileBean;

    public void setProfileBean(AccountProfileBean profileBean) {
        mProfileBean = profileBean;
    }
}
