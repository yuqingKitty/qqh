package com.zdjf.qqh.ui.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.HomeTypeProductBean;
import com.zdjf.qqh.utils.GlideImageLoader;

import java.util.List;

public class HomeTypeAdHeader extends LinearLayout {
    private Banner mBanner;


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
    }

    public void setListener(OnBannerListener bannerListener) {
        if (mBanner != null) {
            mBanner.setOnBannerListener(bannerListener);
        }
    }

    public void setHeadData(@NonNull List<HomeTypeProductBean.BannerBean> bannerBeanList) {
        // 广告位
        mBanner.setIndicatorGravity(BannerConfig.CENTER)
                .setImages(bannerBeanList)
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

}
