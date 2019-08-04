package com.zdjf.qqh.ui.customview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.zdjf.qqh.R;
import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.commons.Constants;
import com.zdjf.qqh.data.entity.HomeBean;
import com.zdjf.qqh.ui.adapter.HomeRecommendProductAdapter;
import com.zdjf.qqh.utils.GlideImageLoader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页list头部控件
 */
public class HomeHeaderView extends LinearLayout implements View.OnClickListener {
    private View mView;
    private Banner mBanner;
    private ViewFlipper mFlipperView;
    private LinearLayout ll_home_type;
    private RecyclerView homeRecommendRecyclerView;

    private Context mContext;
    private List<HomeBean.RecommendProductBean> recommendProductList = new ArrayList<>();
    private HomeRecommendProductAdapter homeRecommendProductAdapter;
    private ClickHomeHeadListener clickHomeHeadListener;

    public HomeHeaderView(Context context) {
        this(context, null);
    }

    public HomeHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.view_home_header, this);
        initView();
    }

    public void setListener(ClickHomeHeadListener clickHomeHeadListener, OnBannerListener bannerListener) {
        this.clickHomeHeadListener = clickHomeHeadListener;
        if (mBanner != null) {
            mBanner.setOnBannerListener(bannerListener);
        }
    }

    private void initView() {
        mBanner = findViewById(R.id.home_banner);
        mFlipperView = findViewById(R.id.home_view_flipper);
        ll_home_type = findViewById(R.id.ll_home_type);
        homeRecommendRecyclerView = findViewById(R.id.home_recommend_list);
        findViewById(R.id.tv_loan_all).setOnClickListener(this);

        homeRecommendProductAdapter = new HomeRecommendProductAdapter(mContext, recommendProductList);
        homeRecommendRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(mContext.getResources().getDrawable(R.drawable.divider));
        homeRecommendRecyclerView.addItemDecoration(divider);
        homeRecommendRecyclerView.setAdapter(homeRecommendProductAdapter);
        homeRecommendRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (BaseApplication.isLogin((Activity) mContext, true, true)) {
                    if (recommendProductList != null && recommendProductList.size() > position) {
                        if (clickHomeHeadListener != null) {
                            clickHomeHeadListener.onRecommendProductClick(((HomeBean.RecommendProductBean) adapter.getData().get(position)).productId, ((HomeBean.RecommendProductBean) adapter.getData().get(position)).link,
                                    Constants.moduleName.Recommend.getName(), position);
                        }
                    }
                }
            }
        });
    }

    /**
     * 设置数据
     *
     * @param banner                banner数据
     * @param noticeList            公告数据
     * @param typeBeanList          类型数据
     * @param recommendProductBeans 推荐数据
     */
    public void setData(@NonNull List<HomeBean.BannerBean> banner, @NonNull List<HomeBean.NoticeBean> noticeList,
                        @NonNull List<HomeBean.TypeBean> typeBeanList, @NonNull List<HomeBean.RecommendProductBean> recommendProductBeans) {
        // 广告位
        mBanner.setIndicatorGravity(BannerConfig.CENTER)
                .setImages(banner)
                .setBannerAnimation(Transformer.Default)
                .setImageLoader(new GlideImageLoader())
                .start();

        // 设置公告
        if (noticeList.size() > 0) {
            for (HomeBean.NoticeBean noticeBean : noticeList) {
                View flipperView = LayoutInflater.from(mContext).inflate(R.layout.layout_home_view_flipper, null);
                TextView notice_desc = flipperView.findViewById(R.id.notice_desc);
                notice_desc.setText(noticeBean.msg);

                if (mFlipperView == null) {
                    if (mView != null) {
                        mFlipperView = mView.findViewById(R.id.home_view_flipper);
                    }
                }
                if (mFlipperView != null) {
                    mFlipperView.addView(flipperView);
                }
            }
            if (noticeList.size() <= 1) {
                if (mFlipperView != null) {
                    mFlipperView.stopFlipping();
                } else {
                    if (mView != null) {
                        mFlipperView = mView.findViewById(R.id.home_view_flipper);
                        if (mFlipperView != null) {
                            mFlipperView.stopFlipping();
                        }
                    }
                }
            }
        }

        // 首页图标类型
        ll_home_type.removeAllViews();
        for (final HomeBean.TypeBean typeBean : typeBeanList) {
            View typeView = LayoutInflater.from(mContext).inflate(R.layout.view_home_header_type, null);
            ImageView iv_home_type = typeView.findViewById(R.id.iv_home_type);
            TextView tv_home_type = typeView.findViewById(R.id.tv_home_type);
            GlideImageLoader.setImg(mContext, typeBean.iconPath, iv_home_type, R.mipmap.home_type_first, R.mipmap.home_type_first);
            tv_home_type.setText(typeBean.name);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            typeView.setLayoutParams(layoutParams);
            ll_home_type.addView(typeView);
            typeView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickHomeHeadListener != null) {
                        clickHomeHeadListener.onTypeClicked(typeBean.name, Integer.valueOf(typeBean.id));
                    }
                }
            });
        }

        if (recommendProductBeans.size() > 0) {
            recommendProductList.clear();
            recommendProductList.addAll(recommendProductBeans);
            homeRecommendProductAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_loan_all:
                // 全部
                if (clickHomeHeadListener != null){
                    clickHomeHeadListener.onLoanAllClicked();
                }
                break;
            default:
                break;
        }
    }

    public interface ClickHomeHeadListener {
        void onTypeClicked(String title, int type);

        void onRecommendProductClick(String productId, String url, String moduleName, int moduleOrder);

        void onLoanAllClicked();
    }

}

