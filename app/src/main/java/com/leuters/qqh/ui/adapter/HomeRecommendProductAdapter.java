package com.leuters.qqh.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.leuters.qqh.R;
import com.leuters.qqh.data.entity.HomeBean;
import com.leuters.qqh.utils.GlideImageLoader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 首页推荐列表适配器
 */
public class HomeRecommendProductAdapter extends BaseQuickAdapter<HomeBean.RecommendProductBean, BaseViewHolder> {
    private Context mContext;

    public HomeRecommendProductAdapter(Context context, @Nullable List<HomeBean.RecommendProductBean> data) {
        super(R.layout.item_home_recommend_product, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeBean.RecommendProductBean item) {
        helper.setText(R.id.tv_recommend_name, item.name)
                .setText(R.id.tv_recommend_intro, item.des);
        GlideImageLoader.setImg(mContext, item.logoUrl, (ImageView) helper.getView(R.id.iv_recommend_icon),
                R.mipmap.icon_today_recommend, R.mipmap.icon_today_recommend);
    }
}
