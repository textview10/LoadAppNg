package com.loadapp.load.ui.home.status.loanapply.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class HomeItemDecoration extends RecyclerView.ItemDecoration {

    public HomeItemDecoration() {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // TODO
        outRect.left = 20;
        outRect.right = 20;
        outRect.top = 10;
        outRect.bottom = 10;
////        //如果不是第一个，则设置top的值。
//        if (parent.getChildAdapterPosition(view) != 0){
//            //这里直接硬编码为1px
//            outRect.top = 1;
//        }
    }

}
