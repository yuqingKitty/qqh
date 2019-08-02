package com.zdjf.qqh.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.HomeBean;
import com.zdjf.qqh.utils.GlideImageLoader;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class HomeLoanProductListAdapter extends BaseAdapter<HomeBean.ProductBean> {
    private Context mContext;

    public HomeLoanProductListAdapter(Context context, @Nullable List<HomeBean.ProductBean> data) {
        super(R.layout.adapter_product_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeBean.ProductBean item) {
        GlideImageLoader.setRoundedCorner(mContext, item.logoUrl, (ImageView) helper.getView(R.id.iv_product_icon), 10);

        helper.setText(R.id.tv_product_name, item.name)
                .setText(R.id.tv_product_tag, item.loanRange)
                .setText(R.id.tv_loan_range, item.loanRange)
                .setText(R.id.tv_loan_term, "借款期限" + item.loanRange)
                .setText(R.id.tv_market_phrases, item.loanRange);

    }
}
