package com.zdjf.qqh.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.HomeBean;
import com.zdjf.qqh.utils.GlideImageLoader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 首页推荐列表适配器
 */
public class HomeRecommendProductAdapter extends BaseQuickAdapter<HomeBean.ProductBean, BaseViewHolder> {
    private Context mContext;

    public HomeRecommendProductAdapter(Context context, @Nullable List<HomeBean.ProductBean> data) {
        super(R.layout.item_home_today_recommend, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeBean.ProductBean item) {
        helper.setText(R.id.tv_recommend_name, item.getName())
                .setText(R.id.tv_recommend_intro, item.getLoanRange());

        GlideImageLoader.setRoundedCorner(mContext, item.getLogoUrl(), (ImageView) helper.getView(R.id.iv_recommend_icon),10);
    }
}