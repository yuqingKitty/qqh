package com.zdjf.qqh.ui.adapter;

import android.support.annotation.Nullable;

import com.zdjf.qqh.ui.customview.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 适配器基类
 */
abstract class BaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    BaseAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        this.setLoadMoreView(new CustomLoadMoreView());
    }
}
