package com.loadapp.load.ui.profile.fragment;

import com.loadapp.load.base.BaseFragment;
import com.loadapp.load.bean.AccountProfileBean;
import com.loadapp.load.ui.profile.CommitProfileActivity;

public class BaseCommitFragment extends BaseFragment {

    protected AccountProfileBean.AccountProfile mProfileBean;

    public void setProfileBean(AccountProfileBean.AccountProfile profileBean) {
        mProfileBean = profileBean;
    }

    protected boolean checkAndToPageByPhaseCode(int curPhase){
        if (curPhase >= CommitProfileActivity.PHASE_ALL){
            toPageByPhaseCode(CommitProfileActivity.PHASE_ALL);
            return true;
        }
        toPageByPhaseCode(curPhase);
        return false;
    }

    private void toPageByPhaseCode(int curPhase){
        if (getActivity() instanceof CommitProfileActivity) {
            ((CommitProfileActivity) getActivity()).switchFragment(curPhase);
        }
    }
}
