package com.loadapp.load.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.loadapp.load.R;
import com.loadapp.load.base.BaseFragment;
import com.loadapp.load.ui.home.profile.PersonProfileFragment;
import com.loadapp.load.ui.profile.CommitProfileActivity;

public class LoanFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnProfile = view.findViewById(R.id.btn_loan_profile_test);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CommitProfileActivity.class);
                startActivity(intent);
            }
        });
//        PersonProfileFragment personProfileFragment = new PersonProfileFragment();
//        toFragment(personProfileFragment);
    }

    public void toFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        transaction.replace(getFragmentContainerRes(), fragment);
        transaction.commitAllowingStateLoss();
    }

    protected @IdRes
    int getFragmentContainerRes() {
        return R.id.fl_loan_container;
    }
}
