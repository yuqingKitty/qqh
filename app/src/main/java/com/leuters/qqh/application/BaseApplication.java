package com.leuters.qqh.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leuters.qqh.R;
import com.leuters.qqh.data.entity.UserBean;
import com.leuters.qqh.ui.activity.LoginNewActivity;
import com.leuters.qqh.ui.customview.swipeback.ActivityLifecycleHelper;
import com.leuters.qqh.utils.GsonUtil;
import com.leuters.qqh.utils.IntentUtil;
import com.leuters.qqh.utils.LogUtil;
import com.leuters.qqh.utils.SPUtil;
import com.leuters.qqh.utils.ScreenUtil;
import com.leuters.qqh.utils.ToastCompat;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIClickCallback;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.RequestCallback;
import cn.jpush.android.api.JPushInterface;

import static com.leuters.qqh.data.commons.Constants.HIDE_LOG;
import static com.leuters.qqh.data.commons.Constants.IS_RELEASE;
import static com.leuters.qqh.data.commons.Constants.LOGIN_SAVE_KEY;
import static com.leuters.qqh.data.commons.Constants.TOKEN_KEY;
import static com.leuters.qqh.data.commons.Constants.USER_ID_KEY;
import static com.leuters.qqh.utils.LogUtil.NOTHING;
import static com.leuters.qqh.utils.LogUtil.VERBOSE;
import static com.leuters.qqh.utils.ScreenUtil.isMeizuFlymeOS;

public class BaseApplication extends Application {

    public static boolean isMeizu = false;
    public static String CHANNEL = "";
    private static UserBean mUserInfo;
    private static String token = "";
    private static String userId;
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        isMeizu = isMeizuFlymeOS();
        LogUtil.init(HIDE_LOG ? NOTHING : VERBOSE, "qqh");
        //友盟统计初始化
        if (IS_RELEASE) {
            UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
            MobclickAgent.openActivityDurationTrack(false);
            MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        }
        UMConfigure.setEncryptEnabled(IS_RELEASE);
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());

        JPushInterface.init(this); // 初始化极光推送
        // 初始化 一键登录sdk
        JVerificationInterface.init(this, new RequestCallback<String>() {
            @Override
            public void onResult(int i, String s) {
               LogUtil.d("BaseApplication","code = " + i + " msg = " + s);
            }
        });
    }

    public static BaseApplication getContext() {
        return instance;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static UserBean getUserBean(Context context) {
        if (null == mUserInfo) {
            String userJson = (String) SPUtil.get(context, LOGIN_SAVE_KEY, "");
            mUserInfo = GsonUtil.GsonToBean(userJson, UserBean.class);
        }
        return mUserInfo;
    }

    /**
     * 保存用户信息
     *
     * @param context
     * @param userInfo
     */
    public static void setUserInfo(Context context, UserBean userInfo) {
        if (userInfo == null) {
            return;
        }
        BaseApplication.mUserInfo = userInfo;
        SPUtil.put(context, LOGIN_SAVE_KEY, GsonUtil.GsonString(userInfo));
    }

    public static void setToken(Context context, String token) {
        BaseApplication.token = token;
        SPUtil.put(context, TOKEN_KEY, token);
    }

    public static void setUserId(Context context, String userId) {
        BaseApplication.userId = userId;
        SPUtil.put(context, USER_ID_KEY, userId);
    }

    public static void setUserHead(Context context, String userHead) {
        UserBean bean = getUserBean(context);
        bean.setImageIcon(userHead);
        setUserInfo(context, bean);
    }

    /**
     * 获取token
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        if (TextUtils.isEmpty(token)) {
            token = (String) SPUtil.get(context, TOKEN_KEY, "");
        }
        return token;
    }

    /**
     * 获取用户id
     *
     * @param context
     * @return
     */
    public static String getUserId(Context context) {
        if (TextUtils.isEmpty(userId)) {
            userId = (String) SPUtil.get(context, USER_ID_KEY, "");
        }
        return userId;
    }

    /**
     * 退出登陆，清除数据
     *
     * @param context
     */
    public static void ClearUser(Context context) {
        mUserInfo = null;
        SPUtil.remove(context, LOGIN_SAVE_KEY);
        SPUtil.remove(context, TOKEN_KEY);
        SPUtil.remove(context, USER_ID_KEY);
        token = "";
        userId = "";
    }

    /**
     * 是否登录
     *
     * @param context   上下文
     * @param toLogin   是否跳转登录
     * @param showToast 是否显示提示
     * @return false:未登录，true:已登录
     */
    public static boolean isLogin(Activity context, boolean toLogin, boolean showToast) {
        if ((TextUtils.isEmpty(getToken(context)) || TextUtils.isEmpty(getUserId(context))) && showToast) {
            ToastCompat.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
        }
        if ((TextUtils.isEmpty(getToken(context)) || TextUtils.isEmpty(getUserId(context))) && toLogin) {
            IntentUtil.toLoginActivity(context);
        }
        return !(TextUtils.isEmpty(getToken(context)) || TextUtils.isEmpty(getUserId(context)));
    }

    /**
     * 获取极光一键登录自定义UI
     * @param context
     * @return
     */
    public static JVerifyUIConfig getUIConfig(Context context) {
        JVerifyUIConfig.Builder configBuilder = new JVerifyUIConfig.Builder();

        TextView loginTextView = new TextView(context);
        loginTextView.setText("其他号码登录");
        loginTextView.setTextColor(context.getResources().getColor(R.color.color_FFC71D));
        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(0, ScreenUtil.dp2px(context, 400.0f), 0, 0);
        mLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        loginTextView.setLayoutParams(mLayoutParams);


        configBuilder
                .setNavColor(0xffffffff)
                .setNavReturnImgPath("umcsdk_return_bg")
                .setLogoWidth(83)
                .setLogoHeight(83)
                .setLogoHidden(false)
                .setNumberColor(0xff181001)
                .setLogBtnText("本机号码一键登录")
                .setLogBtnTextColor(0xffffffff)
                .setLogBtnImgPath("umcsdk_login_btn_bg")
                .setLogBtnHeight(42)
                .setLogBtnTextSize(16)
                .setAppPrivacyColor(0xffa8a8a8, 0xffffc71d)
                .setUncheckedImgPath("umcsdk_uncheck_image")
                .setCheckedImgPath("umcsdk_check_image")
                .setSloganTextColor(0xffcacaca)
                .setLogoOffsetY(50)
                .setLogoImgPath("logo_cm")
                .setNumFieldOffsetY(190)
                .setSloganOffsetY(250)
                .setLogBtnOffsetY(275)
                .setNumberSize(20)
                .setPrivacyState(true)
                .setNavTransparent(true)
                .addCustomView(loginTextView, true, new JVerifyUIClickCallback() {
                    @Override
                    public void onClicked(Context context, View view) {
                        context.startActivity(new Intent(context, LoginNewActivity.class));
                    }
                })
                .setPrivacyOffsetY(35)
                .setPrivacyTextCenterGravity(true);
        return configBuilder.build();
    }


}
