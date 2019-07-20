package com.zdjf.qqh.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.zdjf.qqh.data.entity.UserBean;
import com.zdjf.qqh.ui.customview.swipeback.ActivityLifecycleHelper;
import com.zdjf.qqh.utils.GsonUtil;
import com.zdjf.qqh.utils.IntentUtil;
import com.zdjf.qqh.utils.LogUtil;
import com.zdjf.qqh.utils.SPUtil;
import com.zdjf.qqh.utils.ToastCompat;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import static com.zdjf.qqh.data.commons.Constants.HIDE_LOG;
import static com.zdjf.qqh.data.commons.Constants.IS_RELEASE;
import static com.zdjf.qqh.data.commons.Constants.LOGIN_SAVE_KEY;
import static com.zdjf.qqh.data.commons.Constants.TOKEN_KEY;
import static com.zdjf.qqh.data.commons.Constants.USER_ID_KEY;
import static com.zdjf.qqh.utils.LogUtil.NOTHING;
import static com.zdjf.qqh.utils.LogUtil.VERBOSE;
import static com.zdjf.qqh.utils.ScreenUtil.isMeizuFlymeOS;

public class BaseApplication extends Application {
    public static boolean isMeizu = false;
    private static UserBean mUserInfo;
    private static String token = "";
    private static String userId;
    public static String CHANNEL = "";

    @Override
    public void onCreate() {
        super.onCreate();
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
        bean.setImageFast(userHead);
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

}
