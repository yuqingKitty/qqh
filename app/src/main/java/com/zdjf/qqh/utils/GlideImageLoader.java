package com.zdjf.qqh.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.HomeBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.youth.banner.loader.ImageLoader;

import java.util.UUID;

import static com.zdjf.qqh.utils.ScreenUtil.dp2px;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /*
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        HomeBean.BannerBean bannerBean = (HomeBean.BannerBean) path;
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //Glide 加载图片简单用法
        setImg(context, bannerBean.getImageURL(), imageView, R.mipmap.bitmap_banner, R.mipmap.bitmap_banner);
    }

    /**
     * 设置圆形图片
     *
     * @param context     上下文
     * @param url         图片地址
     * @param imageView   控件
     * @param placeholder 占位图
     * @param error       错误显示图
     */
    public static void setCircleImg(Context context, String url, ImageView imageView, int placeholder, int error) {
        if (context != null && !((Activity) context).isDestroyed()) {
            Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(new CircleCrop()).placeholder(placeholder).error(error)).into(imageView);
        }
    }

    public static void setCircleImg(Context context, int picId, ImageView imageView) {
        if (context != null && !((Activity) context).isDestroyed()) {
            Glide.with(context).load(picId).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
        }
    }

    /**
     * 设置图片
     *
     * @param context     上下文
     * @param url         图片地址
     * @param imageView   控件
     * @param placeholder 占位图
     * @param error       错误显示图
     */
    public static void setImg(Context context, String url, ImageView imageView, int placeholder, int error) {
        if (context != null && !((Activity) context).isDestroyed()) {
            Glide.with(context).load(url).apply(new RequestOptions().placeholder(placeholder).error(error)).into(imageView);
        }
    }

    public static void setImg(Context context, String url, ImageView imageView) {
        if (context != null && !((Activity) context).isDestroyed()) {
            Glide.with(context).load(url).into(imageView);
        }
    }

    public static void setImg(Context context, int picId, ImageView imageView) {
        if (context != null && !((Activity) context).isDestroyed()) {
            Glide.with(context).load(picId).into(imageView);
        }
    }

    public static void setImg(Context context, Uri picId, ImageView imageView, boolean skipMemoryCache) {
        if (context != null && !((Activity) context).isDestroyed()) {
            if (skipMemoryCache) {
                Glide.with(context).load(picId).apply(new RequestOptions().signature(new ObjectKey(UUID.randomUUID().toString()))).into(imageView);
            } else {
                Glide.with(context).load(picId).into(imageView);
            }
        }
    }

    /**
     * 圆角
     *
     * @param context
     * @param picId
     * @param imageView
     */
    public static void setRoundedCorner(Context context, int picId, ImageView imageView) {
        if (context != null && !((Activity) context).isDestroyed()) {
            Glide.with(context).load(picId).apply(RequestOptions.bitmapTransform(new RoundedCorners(dp2px(context, 12)))).into(imageView);
        }
    }

    public static void setRoundedCorner(Context context, String url, ImageView imageView,int Corners) {
        if (context != null && !((Activity) context).isDestroyed()) {
            Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(new RoundedCorners(dp2px(context, Corners)))).into(imageView);
        }
    }

    public static void setRoundedCorner(Context context, String url, ImageView imageView, int placeholder, int error) {
        if (context != null && !((Activity) context).isDestroyed()) {
            Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(new RoundedCorners(dp2px(context, 12))).placeholder(placeholder).error(error)).into(imageView);
        }
    }

    /**
     * 布局加载网络背景图
     *
     * @param context
     * @param url
     * @param layout
     */
    public static void setLayoutBg(Context context, String url, final View layout) {
        if (context != null && !((Activity) context).isDestroyed()) {
            SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, Transition<? super Drawable> transition) {
                    layout.setBackground(resource);
                }
            };

            Glide.with(context).load(url).into(simpleTarget);
        }
    }
}
