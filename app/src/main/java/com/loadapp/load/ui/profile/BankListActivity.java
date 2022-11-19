package com.loadapp.load.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.base.BaseActivity;
import com.loadapp.load.bean.AccountProfileBean;
import com.loadapp.load.bean.BankNameBean;
import com.loadapp.load.bean.event.BankListEvent;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.profile.banklist.BankListsAdapter;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class BankListActivity extends BaseActivity {
    private static final String TAG = "BankListActivity";

    private RecyclerView rvBankList;
    private ImageView ivBack;
    private ArrayList<Pair<String, String>> mBankList = new ArrayList<>();
    private BankListsAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        initializeView();
        getBankList();
    }

    private void initializeView() {
        rvBankList = findViewById(R.id.rv_bank_list);
        ivBack = findViewById(R.id.iv_bank_list_back);

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
