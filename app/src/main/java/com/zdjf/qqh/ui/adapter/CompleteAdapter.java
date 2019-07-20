package com.zdjf.qqh.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.CompleteProductBean;
import com.zdjf.qqh.utils.GlideImageLoader;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

public class CompleteAdapter extends BaseAdapter<CompleteProductBean.ProductBean> {
    private Context mContext;
    private OnApplyClickListener onApplyClickListener;

    public interface OnApplyClickListener{
        void onApplyClicked(CompleteProductBean.ProductBean productBean);
    }

    public CompleteAdapter(Context context, @Nullable ArrayList<CompleteProductBean.ProductBean> data, OnApplyClickListener onApplyClickListener) {
        super(R.layout.adapter_product_item, data);
        mContext = context;
        this.onApplyClickListener = onApplyClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CompleteProductBean.ProductBean item) {
        helper.setText(R.id.tv_product_name, item.name)
                .setText(R.id.tv_product_tag, item.tagDesc)
                .setText(R.id.tv_loan_range, item.loanRange)
                .setText(R.id.tv_loan_term, item.loanTerm)
                .setText(R.id.tv_market_phrases, item.marketPhrases);

        GlideImageLoader.setRoundedCorner(mContext, item.logoURL, (ImageView) helper.getView(R.id.iv_product_icon), 10);

        helper.getView(R.id.tv_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onApplyClickListener != null){
                    onApplyClickListener.onApplyClicked(item);
                }
            }
        });
    }
}
