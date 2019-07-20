package com.zdjf.qqh.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.CompleteProductBean;
import com.zdjf.qqh.utils.GlideImageLoader;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

public class CompleteAdapter extends BaseAdapter<CompleteProductBean.ProductBean> {
    private Context mContext;

    public CompleteAdapter(Context context, @Nullable ArrayList<CompleteProductBean.ProductBean> data) {
        super(R.layout.item_complete, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CompleteProductBean.ProductBean item) {
        helper.setText(R.id.product_name, item.getName())
                .setText(R.id.product_publicity, item.getMarketPhrases());

        GlideImageLoader.setRoundedCorner(mContext, item.getLogoURL(), (ImageView) helper.getView(R.id.product_icon), 10);
    }
}
