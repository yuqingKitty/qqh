package com.leuters.qqh.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.leuters.qqh.R;
import com.leuters.qqh.data.entity.CompleteBean;
import com.leuters.qqh.utils.GlideImageLoader;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

public class CompleteAdapter extends BaseAdapter<CompleteBean.ProductBean> {
    private Context mContext;

    public CompleteAdapter(Context context, @Nullable ArrayList<CompleteBean.ProductBean> data) {
        super(R.layout.adapter_product_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CompleteBean.ProductBean item) {
        GlideImageLoader.setImg(mContext, item.logoUrl, (ImageView) helper.getView(R.id.iv_product_icon),
                R.mipmap.icon_product_default, R.mipmap.icon_product_default);
        helper.setText(R.id.tv_product_name, item.name)
                .setText(R.id.tv_product_tag, item.prodLoanFeature)
                .setText(R.id.tv_loan_range, item.prodLoanRange)
                .setText(R.id.tv_loan_term, "借款期限" + item.prodLoanTerm)
                .setText(R.id.tv_market_phrases, item.prodLoanTime + " " + item.prodLoanInterest);

    }
}
