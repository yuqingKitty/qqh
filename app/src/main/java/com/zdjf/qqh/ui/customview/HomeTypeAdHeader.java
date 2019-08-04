package com.zdjf.qqh.ui.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.HomeBean;
import com.zdjf.qqh.data.entity.HomeTypeProductBean;
import com.zdjf.qqh.utils.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HomeTypeAdHeader extends LinearLayout {
    private Banner mBanner;
    private TextView tv_type_loan_all;
    private ClickTypeAllListener clickTypeAllListener;

    public HomeTypeAdHeader(Context context) {
        this(context, null);
    }

    public HomeTypeAdHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeTypeAdHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.activity_home_type_product_header, this);
        mBanner = mView.findViewById(R.id.banner_home_type);
        tv_type_loan_all = mView.findViewById(R.id.tv_type_loan_all);

        tv_type_loan_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickTypeAllListener != null){
                    clickTypeAllListener.onTypeAllClicked();
                }
            }
        });
    }

    public void setListener(ClickTypeAllListener clickTypeAllListener, OnBannerListener bannerListener) {
        this.clickTypeAllListener = clickTypeAllListener;
        if (mBanner != null) {
            mBanner.setOnBannerListener(bannerListener);
        }
    }

    public void setHeadData(@NonNull List<HomeTypeProductBean.BannerBean> bannerBeanList) {
        List<HomeBean.BannerBean> homeBannerList = new ArrayList<>();
        for (HomeTypeProductBean.BannerBean bannerBean : bannerBeanList){
            HomeBean.BannerBean homeBannerBean = new HomeBean().new BannerBean();
            homeBannerBean.id = bannerBean.id;
            homeBannerBean.productId = bannerBean.productId;
            homeBannerBean.imageURL = bannerBean.imageURL;
            homeBannerBean.srcURL = bannerBean.srcURL;
            homeBannerList.add(homeBannerBean);
        }
        // 广告位
        mBanner.setIndicatorGravity(BannerConfig.CENTER)
                .setImages(homeBannerList)
                .setBannerAnimation(Transformer.Default)
                .setImageLoader(new GlideImageLoader())
                .start();
    }

    public void startScroll() {
        if (mBanner != null) {
            mBanner.start();
        }
    }

    public void stopScroll() {
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
    }

    public interface ClickTypeAllListener {
        void onTypeAllClicked();
    }

}
