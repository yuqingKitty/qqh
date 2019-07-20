package com.zdjf.qqh.ui.customview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 贷款大全 分割线
 */
public class CompleteDecoration extends RecyclerView.ItemDecoration {
    private int mColumn;
    private int mLeftSpace;
    private int mBottomSpace;

    public CompleteDecoration(int column, int leftSpace, int bottomSpace) {
        this.mColumn = column;
        this.mLeftSpace = leftSpace;
        this.mBottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = mLeftSpace;
        outRect.bottom = mBottomSpace;
        //由于每行都只有column个，所以第一个都是column的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) % mColumn == 0) {
            outRect.left = 0;
        }
    }

}
