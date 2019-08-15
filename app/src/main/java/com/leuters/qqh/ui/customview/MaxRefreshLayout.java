package com.leuters.qqh.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

import com.leuters.qqh.R;

public class MaxRefreshLayout extends SwipeRefreshLayout {
    private int mMaxHeight;

    public MaxRefreshLayout(Context context) {
        super(context);
    }

    public MaxRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }


    private void initialize(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRefreshView);
        mMaxHeight = arr.getLayoutDimension(R.styleable.MaxHeightRefreshView_maxHeight, mMaxHeight);
        arr.recycle();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight > 0) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(mMaxHeight, View.MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
