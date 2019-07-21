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

import butterknife.OnClick;

/**
 * 首页list头部控件
 */
public class HomeRvHeaderView extends LinearLayout implements View.OnClickListener {
    private View mView;
    private Banner mBanner;
    private ViewFlipper mFlipperView;
    private List<TextView> typeTextViewList;
    private RecyclerView homeRecommendRecyclerView;

    private Context mContext;
    private List<HomeBean.ProductBean> recommendProductList = new ArrayList<>();
    private HomeRecommendProductAdapter homeRecommendProductAdapter;
    private ClickHomeHeadListener clickHomeHeadListener;

    public HomeRvHeaderView(Context context) {
        this(context, null);
    }

    public HomeRvHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeRvHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        TextView tv_home_type_first = findViewById(R.id.tv_home_type_first);
        TextView tv_home_type_sec = findViewById(R.id.tv_home_type_sec);
        TextView tv_home_type_third = findViewById(R.id.tv_home_type_third);
        homeRecommendRecyclerView = findViewById(R.id.home_recommend_list);
        typeTextViewList = new ArrayList<>();
        typeTextViewList.add(tv_home_type_first);
        typeTextViewList.add(tv_home_type_sec);
        typeTextViewList.add(tv_home_type_third);
        for (TextView textView : typeTextViewList){
            textView.setOnClickListener(this);
        }
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
                            clickHomeHeadListener.onRecommendProductClick(((HomeBean.ProductBean) adapter.getData().get(position)).getProductId(), ((HomeBean.ProductBean) adapter.getData().get(position)).getLink(),
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
     * @param banner   banner数据
     * @param noticeList  公告数据
     * @param typeBeanList  类型数据
     * @param recommendList 推荐数据
     */
    public void setData(@NonNull List<HomeBean.BannerBean> banner, @NonNull List<HomeBean.NoticeBean> noticeList,
                        @NonNull List<HomeBean.TypeBean> typeBeanList, @NonNull List<HomeBean.ProductBean> recommendList) {
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

                flipperView.setTag(noticeBean);
                flipperView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BaseApplication.isLogin((Activity) mContext, true, true)) {
                            HomeBean.NoticeBean bean = (HomeBean.NoticeBean) v.getTag();
                            if (clickHomeHeadListener != null) {
                                clickHomeHeadListener.onRecommendProductClick(bean.getProductId(), bean.getLink(), Constants.moduleName.Notice.getName(), -1);
                            }
                        }
                    }
                });
                notice_desc.setText(noticeBean.getName());

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

        // 首页三个图标类型
        for (int i = 0; i < typeBeanList.size(); i++){
            typeTextViewList.get(i).setText(typeBeanList.get(i).getName());
        }

        if (recommendList.size() > 0) {
            recommendProductList.clear();
            recommendProductList.addAll(recommendList);
            homeRecommendProductAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (clickHomeHeadListener == null)
            return;
        switch (v.getId()) {
            case R.id.tv_home_type_first:
                // 无视黑白
                clickHomeHeadListener.onTypeClicked(0);
                break;
            case R.id.tv_home_type_sec:
                // 极速下款
                clickHomeHeadListener.onTypeClicked(1);
                break;
            case R.id.tv_home_type_third:
                // 反馈
                clickHomeHeadListener.onTypeClicked(2);
                break;
            case R.id.tv_loan_all:
                // 全部
                clickHomeHeadListener.onLoanAllClicked();
                break;
            default:
                break;
        }
    }

    public interface ClickHomeHeadListener {
        void onTypeClicked(int position);
        void onRecommendProductClick(String productId, String url, String moduleName, int moduleOrder);
        void onLoanAllClicked();
    }

    public void startScroll() {
        if (mFlipperView != null) {
            mFlipperView.startFlipping();
        }
        if (mBanner != null) {
            mBanner.start();
        }
    }

    public void stopScroll() {
        if (mFlipperView != null && mFlipperView.isFlipping()) {
            mFlipperView.stopFlipping();
        }
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
    }
}

