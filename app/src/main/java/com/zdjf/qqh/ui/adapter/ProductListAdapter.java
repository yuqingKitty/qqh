package com.zdjf.qqh.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.HomeBean;
import com.zdjf.qqh.utils.GlideImageLoader;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ProductListAdapter extends BaseAdapter<HomeBean.ProductBean> {
    private Context mContext;

    public ProductListAdapter(Context context, @Nullable List<HomeBean.ProductBean> data) {
        super(R.layout.item_product_list_layout, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeBean.ProductBean item) {
        helper.setText(R.id.product_name, item.getName())
                .setText(R.id.product_intro, item.getMarketPhrases())
                .setText(R.id.product_deadline, item.getLoanTerm())
                .setText(R.id.product_limit, item.getLoanRange())
                .setText(R.id.product_num, item.getNumber());
        ((TextView) helper.getView(R.id.product_deadline)).getPaint().setFakeBoldText(true);
        ((TextView) helper.getView(R.id.product_limit)).getPaint().setFakeBoldText(true);
        helper.setVisible(R.id.label1, item.getLabels() != null && item.getLabels().size() > 0);
        helper.setVisible(R.id.label2, item.getLabels() != null && item.getLabels().size() > 1);
        if (item.getLabels() != null) {
            switch (item.getLabels().size()) {
                case 0:
                    helper.setVisible(R.id.label1, false).setVisible(R.id.label2, false);
                    break;
                case 1:
                    helper.setVisible(R.id.label1, true).setVisible(R.id.label2, false)
                            .setText(R.id.label1, item.getLabels().get(0).getName());
                    break;
                case 2:
                    helper.setVisible(R.id.label1, true).setVisible(R.id.label2, true)
                            .setText(R.id.label1, item.getLabels().get(0).getName())
                            .setText(R.id.label2, item.getLabels().get(1).getName());
                    break;
                default:
                    helper.setVisible(R.id.label1, true).setVisible(R.id.label2, true)
                            .setText(R.id.label1, item.getLabels().get(0).getName())
                            .setText(R.id.label2, item.getLabels().get(1).getName());
                    break;
            }
        }

        GlideImageLoader.setRoundedCorner(mContext, item.getLogoUrl(), (ImageView) helper.getView(R.id.product_icon), 12);
    }
}
