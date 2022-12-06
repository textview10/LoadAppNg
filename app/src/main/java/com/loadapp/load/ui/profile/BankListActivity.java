package com.loadapp.load.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.bean.BankNameBean;
import com.loadapp.load.bean.event.BankListEvent;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.profile.banklist.BankListsAdapter;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.loadapp.load.view.WaveSideBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class BankListActivity extends BaseActivity {
    private static final String TAG = "BankListActivity";

    private RecyclerView rvBankList;
    private ImageView ivBack;
    private ArrayList<Pair<String, String>> mBankList = new ArrayList<>();
    private BankListsAdapter mAdapter;
    private WaveSideBar sideBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarVisibility(this, false);
        setContentView(R.layout.activity_bank_list);
        initializeView();
        getBankList();
    }

    private void initializeView() {
        rvBankList = findViewById(R.id.rv_bank_list);
        ivBack = findViewById(R.id.iv_bank_list_back);
        sideBar = findViewById(R.id.sidebar_bank_list);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBankList.setLayoutManager(manager);
        mAdapter = new BankListsAdapter(mBankList);
        mAdapter.setOnItemClickListener(new BankListsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pair<String, String> content, int pos) {
                EventBus.getDefault().post(new BankListEvent(content));
                finish();
            }
        });
        rvBankList.setAdapter(mAdapter);
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                Log.e(TAG, " test index ...");
            }
        });
    }

    private void getBankList() {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.GET_BANK_LIST).tag(TAG)
                .params("data", jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        BankNameBean bankNameBean = checkResponseSuccess(response, BankNameBean.class);
                        if (bankNameBean == null) {
                            Log.e(TAG, " get bank list ." + response.body());
                            return;
                        }
                        ArrayList<Pair<String, String>> list = parseCityList(bankNameBean.getBank_name());
                        Collections.sort(list, new Comparator<Pair<String, String>>() {
                            @Override
                            public int compare(Pair<String, String> pair1, Pair<String, String> pair2) {
                                if (TextUtils.isEmpty(pair1.second)){
                                    return -1;
                                }
                                if (TextUtils.isEmpty(pair2.second)){
                                    return 1;
                                }
                                char c1 = pair1.second.charAt(0);
                                char c2 = pair2.second.charAt(0);
                                return c1 - c2;
                            }
                        });
                        mBankList.clear();
                        mBankList.addAll(list);

                        if (mAdapter != null){
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "get bank list = " + response.body());
                    }
                });
    }

    private ArrayList<Pair<String, String>> parseCityList(Object bankName) {
        ArrayList<Pair<String, String>> lists = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(GsonUtils.toJson(bankName));
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String cityKey = iterator.next();
                String cityValue = jsonObject.optString(cityKey);
                lists.add(new Pair<>(cityKey, cityValue));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }

}
