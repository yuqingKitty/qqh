package com.leuters.qqh.ui.customview;


import com.leuters.qqh.R;

/**
 * 加载更多底部样式
 */

public class CustomLoadMoreView extends com.chad.library.adapter.base.loadmore.LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.loading_layout;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.fail_layout;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.end_layout;
    }
}
