package com.loadapp.load.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.loadapp.load.R;

public abstract class BaseBackFragment extends BaseFragment{

    private ImageView ivBack;
    private TextView tvTitle;

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_back, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivBack = view.findViewById(R.id.iv_base_back);
        tvTitle = view.findViewById(R.id.tv_base_title);
        FrameLayout flContainer = view.findViewById(R.id.fl_base_back);
        View container = LayoutInflater.from(view.getContext()).inflate(getRes(), flContainer, false);
        flContainer.addView(container, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected abstract @LayoutRes int getRes();


    public void setTitle(@StringRes int strRes) {
        if (tvTitle != null) {
            tvTitle.setText(strRes);
        }
    }
}
