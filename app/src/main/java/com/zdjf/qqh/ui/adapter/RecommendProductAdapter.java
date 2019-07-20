package com.zdjf.qqh.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.HomeBean;
import com.zdjf.qqh.utils.GlideImageLoader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 推荐列表适配器
 */
public class RecommendProductAdapter extends BaseQuickAdapter<HomeBean.ProductBean, BaseViewHolder> {
    private Context mContext;

    public RecommendProductAdapter(Context context, @Nullable List<HomeBean.ProductBean> data) {
        super(R.layout.item_product_grid_layout, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeBean.ProductBean item) {
        helper.setText(R.id.product_name, item.getName())
                .setText(R.id.product_intro, item.getLoanRange());

        ((TextView) helper.getView(R.id.product_intro)).getPaint().setFakeBoldText(true);

        GlideImageLoader.setRoundedCorner(mContext, item.getLogoUrl(), (ImageView) helper.getView(R.id.product_icon),12);
    }
}
