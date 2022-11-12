package com.loadapp.load.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Pair;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.loadapp.load.R;

import java.util.ArrayList;

public class SelectDataDialog extends Dialog {

    private RecyclerView rvSelectData;

    private SelectDataAdapter mAdapter;

    private ArrayList<Pair<String, String>> mLists = new ArrayList<>();

    private Observer mObserver;

    public SelectDataDialog(@NonNull Context context) {
        super(context,R.style.DialogTheme);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int)(ScreenUtils.getAppScreenWidth() * 3 / 4); //设置宽度
        getWindow().setAttributes(lp);
        setContentView(R.layout.dialog_select_data);
        rvSelectData = findViewById(R.id.rv_select_data);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvSelectData.setLayoutManager(layoutManager);
        mAdapter = new SelectDataAdapter(mLists);
        rvSelectData.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SelectDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pair<String, String> content, int pos) {
                dismiss();
                if (mObserver != null){
                    mObserver.onItemClick(content, pos);
                }
            }
        });
    }

    public void setList(ArrayList<Pair<String, String>> lists, Observer observer) {
        mObserver = observer;
        mLists.clear();
        mLists.addAll(lists);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public interface Observer {
        void onItemClick(Pair<String, String> content, int pos);
    }
}
