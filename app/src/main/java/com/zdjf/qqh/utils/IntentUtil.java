package com.zdjf.qqh.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.zdjf.qqh.ui.activity.AboutUsActivity;
import com.zdjf.qqh.ui.activity.ChangePwdActivity;
import com.zdjf.qqh.ui.activity.CustomerServiceActivity;
import com.zdjf.qqh.ui.activity.FeedbackActivity;
import com.zdjf.qqh.ui.activity.ForgetPwdActivity;
import com.zdjf.qqh.ui.activity.LoginActivity;
import com.zdjf.qqh.ui.activity.MainActivity;
import com.zdjf.qqh.ui.activity.ProtocolActivity;
import com.zdjf.qqh.ui.activity.RegisterActivity;
import com.zdjf.qqh.ui.activity.SetNicknameActivity;
import com.zdjf.qqh.ui.activity.SettingActivity;
import com.zdjf.qqh.ui.activity.WebAPPActivity;
import com.zdjf.qqh.ui.activity.WebActivity;

import static com.zdjf.qqh.data.commons.Constants.CHOOSE_PICTURE;
import static com.zdjf.qqh.data.commons.Constants.CMSTYPE_INTENT_KEY;
import static com.zdjf.qqh.data.commons.Constants.PHOTO_REQUEST_RESULT;
import static com.zdjf.qqh.data.commons.Constants.TITLE_INTENT_KEY;

/**
 * Intent跳转
 */
public class IntentUtil {
    public static final String INTENT_KEY = "LOAD_URL";
    public static final String TITLE = "TITLE";
    public static final String STATISTICS_ID = "statisticsId";

    /**
     * 跳转主界面
     *
     * @param context
     */
    public static void toMainActivity(Activity context) {
        context.startActivity(new Intent(context, MainActivity.class));

    }

    /**
     * 跳转登录界面
     *
     * @param context
     */
    public static void toLoginActivity(Activity context) {
        context.startActivity(new Intent(context, LoginActivity.class));

    }

    /**
     * 跳转设置界面
     *
     * @param context
     */
    public static void toSettingActivity(Activity context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    /**
     * 跳转修改密码界面
     *
     * @param context
     */
    public static void toChangePwdActivity(Activity context) {
        context.startActivity(new Intent(context, ChangePwdActivity.class));
    }

    /**
     * 跳转忘记密码界面
     *
     * @param context
     */
    public static void toForgetPwdActivity(Activity context) {
        context.startActivity(new Intent(context, ForgetPwdActivity.class));
    }

    /**
     * 跳转注册界面
     *
     * @param context
     */
    public static void toRegisterActivity(Activity context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    /**
     * 跳转协议
     *
     * @param context
     */
    public static void toProtocolActivity(Activity context, String cmsType, String title) {
        Intent intent = new Intent(context, ProtocolActivity.class);
        intent.putExtra(CMSTYPE_INTENT_KEY, cmsType);
        intent.putExtra(TITLE_INTENT_KEY, title);
        context.startActivity(intent);
    }

    /**
     * 意见反馈
     *
     * @param context
     */
    public static void toFeedbackActivity(Activity context) {
        context.startActivity(new Intent(context, FeedbackActivity.class));
    }

    /**
     * 客服
     *
     * @param context
     */
    public static void toServiceActivity(Activity context) {
        context.startActivity(new Intent(context, CustomerServiceActivity.class));
    }

    /**
     * 客服
     *
     * @param context
     */
    public static void toAboutUsActivity(Activity context) {
        context.startActivity(new Intent(context, AboutUsActivity.class));
    }

    /**
     * 网页跳转
     *
     * @param context
     * @param url
     * @param id
     */
    public static void toWebView(Activity context, String url, String id) {
        if (!TextUtils.isEmpty(url.trim())) {
            WebActivity.startWebView(context, url, id);
        }
    }

    public static void toAppWebView(Activity context, String url, String id) {
        if (!TextUtils.isEmpty(url.trim())) {
            Intent intent = new Intent(context, WebAPPActivity.class);
            intent.putExtra(INTENT_KEY, url);
            intent.putExtra(STATISTICS_ID, id);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转设置用户名
     *
     * @param context
     */
    public static void toSetNickname(Activity context) {
        context.startActivity(new Intent(context, SetNicknameActivity.class));
    }

    /**
     * 跳转到微信
     */
    public static void getWechatApi(Activity context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastCompat.makeText(context, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 调用系统的裁剪功能
     */
    public static void startPhotoZoom(Activity context, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");// crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("aspectX", 1); // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);// outputX outputY 是裁剪图片宽高
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, PHOTO_REQUEST_RESULT);
    }

    public static void openFile(Activity activity) {
        //选择照片
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        activity.startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
    }


}
