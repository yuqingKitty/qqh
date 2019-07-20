package com.zdjf.qqh.ui.customview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.zdjf.qqh.data.entity.RxBusMessage;
import com.zdjf.qqh.ui.adapter.RecommendProductAdapter;
import com.zdjf.qqh.utils.GlideImageLoader;
import com.zdjf.qqh.utils.rxbus.RxBus;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import static com.zdjf.qqh.data.commons.Constants.RXBUS_TO_COMPLETE_KEY;

/**
 * 首页list头部控件
 */
public class HomeRvHeaderView extends LinearLayout {
    private Context mContext;
    private Banner mBanner;
    private RecyclerView mRecyclerView;
    private TabLayout mTabLayou;
    private TextView mApplyTv;
    private ViewFlipper mFlipperView;
    private List<HomeBean.ProductBean> mCategoryist = new ArrayList<>();
    private RecommendProductAdapter mRecommendAdapter;
    private TabChangeListener mListener;
    private ClickProductListener mProcuctListener;
    private View mView;
    private List<HomeBean.BannerBean> mBannerList;
    private List<HomeBean.NoticeBean> mNoticesList;
    private List<HomeBean.ProductBean> mCategoryList;
    private List<HomeBean.TypeBean> mTabListList;

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

    public void setListener(TabChangeListener listener, ClickProductListener productListener, OnBannerListener bannerListener) {
        mListener = listener;
        mProcuctListener = productListener;
        if (mBanner != null) {
            mBanner.setOnBannerListener(bannerListener);
        }
    }

    private void initView() {
        mBanner = findViewById(R.id.home_banner);
        mRecyclerView = findViewById(R.id.recommend_list);
        mFlipperView = findViewById(R.id.home_view_flipper);
        mTabLayou = findViewById(R.id.pager_tab);
        mApplyTv = findViewById(R.id.apply_notification_tv);
        mRecommendAdapter = new RecommendProductAdapter(mContext, mCategoryist);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecyclerView.addItemDecoration(new HomeGridRvItemDecoration(mContext, 1, 30, R.color.lineColor));
        mRecyclerView.setAdapter(mRecommendAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (BaseApplication.isLogin((Activity) mContext, true, true)) {
                    if (mCategoryList != null && mCategoryList.size() > position) {
                        if (mProcuctListener != null) {
                            mProcuctListener.onProductClick(((HomeBean.ProductBean) adapter.getData().get(position)).getProductId(), ((HomeBean.ProductBean) adapter.getData().get(position)).getLink(),
                                    Constants.moduleName.Recommend.getName(), position);
                        }
                    }
                }
            }
        });

        findViewById(R.id.home_recommend_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstanceBus().post(new RxBusMessage<>(RXBUS_TO_COMPLETE_KEY));
            }
        });
    }

    /**
     * 设置数据
     *
     * @param banner   banner数据
     * @param notices  公告数据
     * @param category 推荐数据
     * @param tabList  类型数据
     */
    public void setData(@NonNull List<HomeBean.BannerBean> banner, @NonNull List<HomeBean.NoticeBean> notices, @NonNull List<HomeBean.ProductBean> category, @NonNull List<HomeBean.TypeBean> tabList, String num) {
        mBannerList = banner;
        mNoticesList = notices;
        mCategoryList = category;
        mTabListList = tabList;
        if (num.length() > 4) {
            String title = num.substring(0, 4);
            String nu = num.substring(4);
            String apply = title + "<br><font color='#FE5C0D'>" + nu + "</font>";
            mApplyTv.setText(Html.fromHtml(apply));
        } else {
            mApplyTv.setText(num);
        }
        mBanner.setIndicatorGravity(BannerConfig.CENTER)
                .setImages(banner)
                .setBannerAnimation(Transformer.Default)
                .setImageLoader(new GlideImageLoader())
                .start();

        //设置公告
        if (notices.size() > 0) {
            for (HomeBean.NoticeBean noticeBean : notices) {
                View flipperView = LayoutInflater.from(mContext).inflate(R.layout.layout_home_view_flipper, null);
                ImageView icon = flipperView.findViewById(R.id.notice_icon);
                TextView name = flipperView.findViewById(R.id.notice_name);
                TextView phone = flipperView.findViewById(R.id.notice_phone);
                TextView price = flipperView.findViewById(R.id.notice_price);
                TextView date = flipperView.findViewById(R.id.notice_date);

                flipperView.setTag(noticeBean);
                flipperView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BaseApplication.isLogin((Activity) mContext, true, true)) {
                            HomeBean.NoticeBean bean = (HomeBean.NoticeBean) v.getTag();
                            if (mProcuctListener != null) {
                                mProcuctListener.onProductClick(bean.getProductId(), bean.getLink(), Constants.moduleName.Notice.getName(), -1);
                            }
                        }
                    }
                });
                GlideImageLoader.setRoundedCorner(mContext, noticeBean.getLogoUrl(), icon, 2);
                name.setText(noticeBean.getName());
                phone.setText(noticeBean.getPhone());
                price.setText(noticeBean.getMoney());
                date.setText(noticeBean.getTime());

                if (mFlipperView == null) {
                    if (mView != null) {
                        mFlipperView = mView.findViewById(R.id.home_view_flipper);
                    }
                }
                if (mFlipperView != null) {
                    mFlipperView.addView(flipperView);
                }
            }
            if (notices.size() <= 1) {
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

        if (category.size() > 0) {
            mCategoryist.clear();
            mCategoryist.addAll(category);
            mRecommendAdapter.notifyDataSetChanged();
        }

        if (tabList.size() > 0) {
            mTabLayou.setVisibility(VISIBLE);
            mTabLayou.removeAllTabs();
            for (int i = 0; i < tabList.size(); i++) {
                mTabLayou.addTab(mTabLayou.newTab().setText(tabList.get(i).getName()));
            }
            mTabLayou.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (mListener != null) {
                        mListener.tabChange(tab.getPosition());
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        } else {
            mTabLayou.setVisibility(GONE);
        }
    }

    public interface TabChangeListener {
        void tabChange(int position);
    }

    public interface ClickProductListener {
        void onProductClick(String productId, String url, String moduleName, int moduleOrder);
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

