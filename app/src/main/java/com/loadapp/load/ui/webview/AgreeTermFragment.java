package com.loadapp.load.ui.webview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseFragment;

public class AgreeTermFragment extends BaseFragment {

    private FrameLayout frameLayout;
    private TextView tvCancel;
    private TextView tvAgree;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agree_term, container, false);
        initializeView(view);
        initializeWebView();
        return view;
    }

    private void initializeView(View view) {
        frameLayout = view.findViewById(R.id.fl_agree_term);
        tvCancel = view.findViewById(R.id.tv_agree_term_cancel);
        tvAgree = view.findViewById(R.id.tv_agree_term_agree);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        tvAgree.setOnClickListener(view1 -> {
//            if (getActivity() instanceof SignUpActivity) {
//                showOrHideTopContainer(true);
//                SignUpActivity signUpActivity = (SignUpActivity) getActivity();
//                signUpActivity.toVerificationFragment();
//            }

        });
        showOrHideTopContainer(false);
    }

    private void showOrHideTopContainer(boolean showFlag){
//        if (getActivity() instanceof SignUpActivity) {
//            SignUpActivity signUpActivity = (SignUpActivity) getActivity();
//            signUpActivity.showOrHideTopContainer(showFlag);
//        }
    }

    private void initializeWebView() {
        WebViewFragment webViewFragment = new WebViewFragment();
        webViewFragment.setUrl(Api.WEB_VIEW_TERM);
        toFragment(webViewFragment);

    }

    public void toFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        transaction.replace(R.id.fl_agree_term, fragment);
        transaction.commitAllowingStateLoss();
    }
}
