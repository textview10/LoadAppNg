package com.loadapp.load.ui.profile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.loadapp.load.R;
import com.loadapp.load.base.BaseFragment;
import com.loadapp.load.ui.profile.CommitProfileActivity;

public class PersonProfileFragment extends BaseFragment {

    private FrameLayout flCommit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof CommitProfileActivity) {
            ((CommitProfileActivity) getActivity()).setTitle(R.string.loan_person_profile_title);
        }

        flCommit = view.findViewById(R.id.fl_person_profile_commit);
        flCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof CommitProfileActivity) {
                    ((CommitProfileActivity) getActivity()).switchFragment(1);
                }
            }
        });
    }
}
