package com.loadapp.load.ui.profile.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.bean.AccountProfileBean;
import com.loadapp.load.bean.PhaseBean;
import com.loadapp.load.dialog.SelectDataDialog;
import com.loadapp.load.global.ConfigMgr;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.profile.CommitProfileActivity;
import com.loadapp.load.ui.profile.widget.EditTextContainer;
import com.loadapp.load.ui.profile.widget.SelectContainer;
import com.loadapp.load.ui.webview.WebViewFragment;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class PersonProfileFragment extends BaseCommitFragment {
    private static final String TAG = "PersonProfileFragment";

    private FrameLayout flCommit;
    private EditTextContainer emailEditText, streetEditText;
    private SelectContainer selectMonthSalary, selectWorkYear, selectCity;

    private Pair<String, String> mMonthSalary;
    private Pair<String, String> mWorkYear;
    private ImageView checkAgree;
    private TextView tvTerms;
    private WebViewFragment webViewFragment;
    private boolean isAgree = true;

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

        emailEditText = view.findViewById(R.id.edit_view_person_profile_email);
        selectMonthSalary = view.findViewById(R.id.select_view_person_profile_monthly_salary);
        selectWorkYear = view.findViewById(R.id.select_view_person_profile_work_year);
        selectCity = view.findViewById(R.id.select_view_person_profile_province);
        streetEditText = view.findViewById(R.id.edittext_view_person_profile_address);
        flCommit = view.findViewById(R.id.fl_person_profile_commit);

        checkAgree = view.findViewById(R.id.iv_person_profile_agree);
        tvTerms = view.findViewById(R.id.tv_person_profile_term);

        initializeListener();
    }

    private void initializeListener(){
        selectMonthSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog(ConfigMgr.mMonthSalaryList, new SelectDataDialog.Observer() {
                    @Override
                    public void onItemClick(Pair<String, String> content, int pos) {
                        mMonthSalary = content;
                        selectMonthSalary.setData(content.first);
                    }
                });
            }
        });
        selectWorkYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog(ConfigMgr.mWorkingYearList, new SelectDataDialog.Observer() {
                    @Override
                    public void onItemClick(Pair<String, String> content, int pos) {
                        mWorkYear = content;
                        selectWorkYear.setData(content.first);
                    }
                });
            }
        });
        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAreaPicker();
            }
        });

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity baseActivity = (BaseActivity) getActivity();
                if (webViewFragment == null){
                    webViewFragment = new WebViewFragment();
                }
                webViewFragment.setUrl(Api.WEB_VIEW_POLICY);
                baseActivity.addFragment(webViewFragment, "term");
            }
        });

        checkAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAgree = !isAgree;
                updateAgreeState();
            }
        });

        flCommit.setOnClickListener(view1 -> {
            if (!isAgree){
                ToastUtils.showShort(" must agree terms");
                return;
            }
            boolean check = checkProfileParams();
            if (check) {
                uploadBase();
            }
        });
    }

    private void showListDialog(HashSet<Pair<String, String>> set, SelectDataDialog.Observer observer){
        SelectDataDialog dialog = new SelectDataDialog(getContext());
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        list.addAll(set);
        dialog.setList(list, observer);
        dialog.show();
    }

    @Override
    public void setProfileBean(AccountProfileBean.AccountProfile profileBean) {
        super.setProfileBean(profileBean);
        if (emailEditText != null && !TextUtils.isEmpty(profileBean.getEmail())){
            emailEditText.setEditTextAndSelection(profileBean.getEmail());
        }
        if (selectMonthSalary != null) {
            Pair<String, String> data = getData(ConfigMgr.mMonthSalaryList, profileBean.getMonth_salary());
            if (data != null) {
                mMonthSalary = data;
                selectMonthSalary.setData(data.first);
            }
        }
        if (selectWorkYear != null) {
            Pair<String, String> data = getData(ConfigMgr.mWorkingYearList, profileBean.getWorking_year());
            if (data != null) {
                mWorkYear = data;
                selectWorkYear.setData(data.first);
            }
        }
        if (selectCity != null && mHomeState != null) {
            mHomeCity = profileBean.getHome_city();
            mHomeState = profileBean.getHome_state();
            selectCity.setData(mHomeCity + "-" + mHomeState);
        }
        if (streetEditText != null && !TextUtils.isEmpty(profileBean.getHome_address())){
            streetEditText.setEditTextAndSelection(profileBean.getHome_address());
        }
    }

    private Pair<String, String> getData(HashSet<Pair<String, String>> set, int key){
        Iterator<Pair<String, String>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Pair<String, String> pair = iterator.next();
            if (TextUtils.equals(pair.second, String.valueOf(key))) {
                return pair;
            }
        }
        return null;
    }

    private boolean checkProfileParams() {
        if (emailEditText == null || TextUtils.isEmpty(emailEditText.getText())) {
            ToastUtils.showShort("email is null");
            return false;
        }
        if (streetEditText == null || TextUtils.isEmpty(streetEditText.getText())) {
            ToastUtils.showShort("street is null");
            return false;
        }
        if (mMonthSalary == null ) {
            ToastUtils.showShort("monthly salary is null");
            return false;
        }
        if (mWorkYear == null) {
            ToastUtils.showShort("work year is null");
            return false;
        }
        if (mHomeCity == null || mHomeState == null) {
            ToastUtils.showShort("province or state is null");
            return false;
        }
        return true;
    }

    private final ArrayList<String> provinceList = new ArrayList<>();
    private final ArrayList<ArrayList<String>> stateList = new ArrayList<>();
    private String mHomeCity, mHomeState;

    private void initAreaPickerData() {
        provinceList.clear();
        stateList.clear();
        HashMap<String, HashSet<String>> areaData = ConfigMgr.mStateCityList;
        Iterator<Map.Entry<String, HashSet<String>>> iterator = areaData.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, HashSet<String>> entry = iterator.next();
            provinceList.add(entry.getKey());
            ArrayList<String> stateItemList = new ArrayList<>();
            stateItemList.addAll(entry.getValue());
            stateList.add(stateItemList);
        }
    }

    public void showAreaPicker() {
        KeyboardUtils.hideSoftInput(getActivity());
        initAreaPickerData();
        if (provinceList.size() == 0 ||  stateList.size() == 0 ){
            ConfigMgr.getAllConfig();
            ToastUtils.showShort("area data error , please wait a monment and try again");
            return;
        }
        OptionsPickerBuilder pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (options1 != -1){
                    mHomeCity = provinceList.get(options1);
                }
                if (options1 != -1 && options2 != -1) {
                    mHomeState = stateList.get(options1).get(options2);
                }
                if (selectCity != null) {
                    selectCity.setData(mHomeCity + "-" + mHomeState);
                }
                Log.i(TAG, " select province = " + mHomeCity + " select state = " + mHomeState);
            }
        });
        OptionsPickerView view = pvOptions.setSubmitText("ok")//确定按钮文字
                .setCancelText("cancel")//取消按钮文字
                .setTitleText("city picker")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .build();
        view.setPicker(provinceList, stateList);
        view.setSelectOptions(0, 0, 0);
        view.show();
    }

    private void uploadBase(){
        // TODO
//        if (true) {
//            if (getActivity() instanceof CommitProfileActivity) {
//                ((CommitProfileActivity) getActivity()).switchFragment(1);
//            }
//            return;
//        }
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("access_token", Constant.mToken);
            jsonObject.put("account_id", Constant.mAccountId + "");
            jsonObject.put("email", emailEditText.getText());
            jsonObject.put("month_salary", mMonthSalary.second);
            jsonObject.put("working_year", mWorkYear.second);
            jsonObject.put("home_state", mHomeState);
            jsonObject.put("home_city", mHomeCity);
            jsonObject.put("home_address", streetEditText.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.UPLOAD_BASE).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        PhaseBean phaseBean = checkResponseSuccess(response, PhaseBean.class);
                        if (phaseBean == null) {
                            Log.e(TAG, " upload base error ." + response.body());
                            return;
                        }
                        checkAndToPageByPhaseCode(phaseBean.getCurrent_phase());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "upload base failure = " + response.body());
                    }
                });
    }

    private void updateAgreeState(){
        if (checkAgree != null){
            checkAgree.setImageResource(isAgree ? R.drawable.ic_selected : R.drawable.ic_unselected);
        }
    }
}
