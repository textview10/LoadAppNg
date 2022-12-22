package com.loadapp.load.ui.profile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.loadapp.load.R;

public class GenderCheckBox extends LinearLayout {

    private ImageView ivMale, ivFamily;
    private LinearLayout llMale, llFamily;

    private int mCurPos = 1;    //1男2女

    public GenderCheckBox(Context context) {
        super(context);
    }

    public GenderCheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivMale = findViewById(R.id.iv_person_profile_check_male);
        ivFamily = findViewById(R.id.iv_person_profile_check_family);
        llMale = findViewById(R.id.ll_person_profile_male_container);
        llFamily = findViewById(R.id.ll_person_profile_family_container);

        llMale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurPos = 1;
                updateState();
            }
        });

        llFamily.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurPos = 2;
                updateState();
            }
        });
    }

    private void updateState(){
        if (mCurPos == 0){
            ivMale.setImageResource(R.drawable.ic_checkbox_check);
            ivFamily.setImageResource(R.drawable.ic_checkbox_uncheck);
        } else {
            ivMale.setImageResource(R.drawable.ic_checkbox_uncheck);
            ivFamily.setImageResource(R.drawable.ic_checkbox_check);
        }
    }

    public int getCurPos(){
        return mCurPos;
    }

    public void setPos(int pos){
        if (pos == 1 || pos == 2) {
            mCurPos = pos;
            updateState();
        }
    }
}
