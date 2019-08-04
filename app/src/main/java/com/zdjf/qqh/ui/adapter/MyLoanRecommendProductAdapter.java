package com.zdjf.qqh.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.MyLoanRecordBean;
import com.zdjf.qqh.utils.GlideImageLoader;

import java.util.List;

public class MyLoanRecommendProductAdapter extends BaseAdapter<MyLoanRecordBean.MyRecommendProductBean> {
    private Context mContext;

    public MyLoanRecommendProductAdapter(Context context, @Nullable List<MyLoanRecordBean.MyRecommendProductBean> data) {
        super(R.layout.adapter_product_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MyLoanRecordBean.MyRecommendProductBean item) {
        GlideImageLoader.setImg(mContext, item.logoUrl, (ImageView) helper.getView(R.id.iv_product_icon),
                R.mipmap.icon_product_default, R.mipmap.icon_product_default);
        helper.setText(R.id.tv_product_name, item.name)
                .setText(R.id.tv_product_tag, item.prodLoanFeature)
                .setText(R.id.tv_loan_range, item.prodLoanRange)
                .setText(R.id.tv_loan_term, "借款期限" + item.prodLoanTerm)
                .setText(R.id.tv_market_phrases, item.prodLoanTime + " " + item.prodLoanInterest);
    }
}

