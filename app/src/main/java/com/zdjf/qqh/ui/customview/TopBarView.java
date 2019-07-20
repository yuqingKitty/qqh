package com.zdjf.qqh.ui.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdjf.qqh.R;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.utils.ScreenUtil;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.lang.reflect.Method;

/**
 * @Description: 头布局
 * @Author: Young
 * @Time: 2018/5/7 16:39
 */
public class TopBarView extends AutoRelativeLayout {

    private TextView mTvTitle;//标题栏
    private TextView mTvAction;//右边按钮
    private Context mContext;
    private ImageView mRightImg;
    private AutoRelativeLayout mLayoutBack;//返回键
    private AutoRelativeLayout tbLayout;

    private String mTitle = "";
    private int mTitleColor;
    private int isShowClose;
    private int default_reached_color = Color.rgb(51, 51, 51);
    private int default_close = 1;

    public TopBarView(Context context) {
        this(context, null);
    }

    public TopBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBarView);
        mTitle = ta.getString(R.styleable.TopBarView_bar_title);
        mTitleColor = ta.getColor(R.styleable.TopBarView_title_text_color, default_reached_color);
        isShowClose = ta.getInt(R.styleable.TopBarView_show_close, default_close);
        ta.recycle();
        initData();
        int height;
        if (hasNotchInScreen(mContext)) {
            //华为刘海屏
            height = getNotchSize(mContext)[1];
            ViewGroup.LayoutParams layoutParams = tbLayout.getLayoutParams();
            layoutParams.height += height;
        } else if (hasNotchInScreenAtOppo(mContext)) {
            //oppo刘海屏
            height = 80;
            ViewGroup.LayoutParams layoutParams = tbLayout.getLayoutParams();
            layoutParams.height += height;
        } else if (hasNotchInScreenAtVivo(mContext)) {
            //vivo刘海屏
            height = ScreenUtil.dp2px(mContext, 32);
            ViewGroup.LayoutParams layoutParams = tbLayout.getLayoutParams();
            layoutParams.height += height;
        } else {
            height = ScreenUtil.getStatusHeight(mContext);
        }
        tbLayout.setPadding(0, height, 0, 0);
    }

    //判断是否是华为刘海屏
    public static boolean hasNotchInScreen(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);

        } catch (ClassNotFoundException e) {
            LogUtil.e("hasNotchInScreenHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            LogUtil.e("hasNotchInScreenHuawei NoSuchMethodException");
        } catch (Exception e) {
            LogUtil.e("hasNotchInScreenHuawei Exception");
        } finally {
            return ret;
        }
    }

    /**
     * oppo是否刘海屏
     *
     * @param context
     * @return
     */
    public static boolean hasNotchInScreenAtOppo(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    /**
     * vivo是否刘海屏
     */
    public static final int NOTCH_IN_SCREEN_VIVO = 0x00000020;//是否有凹槽
    public static final int ROUNDED_IN_SCREEN_VIVO = 0x00000008;//是否有圆角

    public static boolean hasNotchInScreenAtVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class FtFeature = cl.loadClass("com.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) get.invoke(FtFeature, NOTCH_IN_SCREEN_VIVO);
        } catch (ClassNotFoundException e) {
            LogUtil.e("hasNotchInScreenVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            LogUtil.e("hasNotchInScreenVivo NoSuchMethodException");
        } catch (Exception e) {
            LogUtil.e("hasNotchInScreenVivo Exception");
        } finally {
            return ret;
        }
    }

    //获取华为刘海的高宽
    public static int[] getNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            LogUtil.e("getNotchSize ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            LogUtil.e("getNotchSize NoSuchMethodException");
        } catch (Exception e) {
            LogUtil.e("getNotchSize Exception");
        } finally {
            return ret;
        }
    }

    public void changeTitleFont() {
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/langqing.otf");
        mTvTitle.setTypeface(typeface);
    }

    public void setTitleBold() {
        mTvTitle.getPaint().setFakeBoldText(true);
    }

    private void initData() {
        View mView = inflate(mContext, R.layout.item_navigation_bars, this);
        AutoUtils.auto(mView);
        mTvTitle = mView.findViewById(R.id.tv_navigation_bars_title);
        mTvAction = mView.findViewById(R.id.tv_navigation_bars_action);
        mRightImg = mView.findViewById(R.id.right_img);
        mLayoutBack = mView.findViewById(R.id.layout_back);
        tbLayout = mView.findViewById(R.id.tbv_navigation);
        mTvTitle.setText(mTitle);
        mTvTitle.setTextColor(mTitleColor);
        mLayoutBack.setVisibility(isShowClose == default_close ? GONE : VISIBLE);
        mLayoutBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
            }
        });
    }

    /**
     * 设置返回按钮和右边编辑按钮是否显示
     *
     * @param leftVisible
     * @param rightVisible
     */
    public void setLeftAndRightVisibility(boolean leftVisible, boolean rightVisible) {
        if (leftVisible) {
            mLayoutBack.setVisibility(View.VISIBLE);
        } else {
            mLayoutBack.setVisibility(View.GONE);
        }
        if (rightVisible) {
            mRightImg.setVisibility(View.VISIBLE);
        } else {
            mRightImg.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右图片
     *
     * @param rightImg
     */
    public void setRightImg(int rightImg) {
        mTvAction.setVisibility(View.GONE);
        mRightImg.setVisibility(View.VISIBLE);
        mRightImg.setImageResource(rightImg);
    }

    public void setRightImgVisibility(int visibility) {
        mRightImg.setVisibility(visibility);
    }

    /**
     * 设置右图片点击事件
     *
     * @param l
     */
    public void setRightImgClick(OnClickListener l) {
        mRightImg.setOnClickListener(l);
    }

    /**
     * 设置右图标/文字点击事件
     *
     * @param l
     */
    public void setRightClick(OnClickListener l) {
        mTvAction.setOnClickListener(l);
    }

    /**
     * 设置左边编辑按钮的点击事件
     *
     * @param l
     */
    public void setLeftClick(OnClickListener l) {
        mLayoutBack.setOnClickListener(l);
    }

    /**
     * 设置右边编辑按钮的文字内容
     *
     * @param str
     */
    public void setRightContent(String str) {
        mTvAction.setText(str);
    }

    public void setRightVisibility(int visibility) {
        mTvAction.setVisibility(visibility);
    }

    /**
     * 设置右边编辑按钮的文字颜色
     *
     * @param i
     */
    public void setRightColor(int i) {
        mTvAction.setTextColor(i);
    }

    /**
     * 设置中间标题的内容
     *
     * @param str
     */
    public void setTitleContent(String str) {
        mTvTitle.setText(str);
    }

    public String getTitleContent() {
        return mTvTitle.getText().toString();
    }

}

