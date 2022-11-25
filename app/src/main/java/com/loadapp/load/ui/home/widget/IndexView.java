package com.loadapp.load.ui.home.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;

public class IndexView extends View {
    private static final int INDEX_TOP = 10;
    private static final int RADIUS = 7;
    private static final int INNER_RADIUS = 2;

    private int mTop;
    private int radius;
    private int innerRadius;
    private Paint paint;
    private Paint innerPaint;

    private Rect rectTop = new Rect();
    private Rect rectBottom = new Rect();

    private Paint mLinePaint = new Paint();

    public IndexView(Context context) {
        super(context);
        initData();
    }

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public IndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        mTop = SizeUtils.dp2px(INDEX_TOP);
        radius = SizeUtils.dp2px(RADIUS);
        innerRadius = SizeUtils.dp2px(INNER_RADIUS);

        paint = new Paint();
        paint.setColor(ColorUtils.string2Int("#1774F7"));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        innerPaint = new Paint();
        innerPaint.setColor(Color.WHITE);
        innerPaint.setStyle(Paint.Style.FILL);
        innerPaint.setStyle(Paint.Style.FILL);
        innerPaint.setAntiAlias(true);

        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(ColorUtils.string2Int("#111F2C"));
        mLinePaint.setAntiAlias(true);

//        setBackgroundColor(Color.RED);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        rectTop.set(getWidth() / 2 - 1, 0, getWidth() / 2 + 1,
                mTop);
        rectBottom.set(getWidth() / 2 - 1, mTop + radius * 2,
                getWidth() / 2 + 1, getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() == 0){
            return;
        }
        canvas.drawCircle(getWidth() / 2, mTop + radius, radius, paint);
        canvas.drawCircle(getWidth() / 2, mTop + radius, innerRadius , innerPaint);
        canvas.drawRect(rectTop, mLinePaint);
        canvas.drawRect(rectBottom, mLinePaint);
    }
}
