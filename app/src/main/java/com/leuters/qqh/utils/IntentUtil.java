package com.leuters.qqh.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.leuters.qqh.application.BaseApplication;
import com.leuters.qqh.ui.activity.CustomerServiceActivity;
import com.leuters.qqh.ui.activity.HomeTypeProductActivity;
import com.leuters.qqh.ui.activity.LoginNewActivity;
import com.leuters.qqh.ui.activity.MainActivity;
import com.leuters.qqh.ui.activity.MessageCenterActivity;
import com.leuters.qqh.ui.activity.MyLoanRecordActivity;
import com.leuters.qqh.ui.activity.SetNicknameActivity;
import com.leuters.qqh.ui.activity.SettingActivity;
import com.leuters.qqh.ui.activity.WebAPPActivity;
import com.leuters.qqh.ui.activity.WebActivity;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.VerifyListener;

import static com.leuters.qqh.data.commons.Constants.CHOOSE_PICTURE;
import static com.leuters.qqh.data.commons.Constants.EXTRA_TYPE_ID;
import static com.leuters.qqh.data.commons.Constants.PHOTO_REQUEST_RESULT;
import static com.leuters.qqh.data.commons.Constants.SP_KEY_CAN_JIGUANNG_LOGIN;
import static com.leuters.qqh.data.commons.Constants.TITLE_INTENT_KEY;

/**
 * Intent跳转
 */
public class IntentUtil {
    public static final String INTENT_KEY = "LOAD_URL";
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
     * 跳转设置界面
     *
     * @param context
     */
    public static void toSettingActivity(Activity context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    /**
     * 申请记录
     *
     * @param context
     */
    public static void toMyLoanRecordActivity(Activity context) {
        context.startActivity(new Intent(context, MyLoanRecordActivity.class));
    }

    /**
     * 消息中心
     *
     * @param context
     */
    public static void toMessageCenterActivity(Activity context) {
        context.startActivity(new Intent(context, MessageCenterActivity.class));
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

    /**
     * 跳转首页类型
     *
     * @param context
     * @param title
     * @param typeId
     */
    public static void toHomeTypeProductActivity(Activity context, String title, String typeId) {
        Intent intent = new Intent(context, HomeTypeProductActivity.class);
        intent.putExtra(TITLE_INTENT_KEY, title);
        intent.putExtra(EXTRA_TYPE_ID, typeId);
        context.startActivity(intent);
    }

    /**
     * 跳转登录界面
     *
     * @param context
     */
    public static void toLoginActivity(Activity context) {
        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(context);
        boolean isCanJiguangLogin = (boolean) SPUtil.get(context, SP_KEY_CAN_JIGUANNG_LOGIN, false);
        if (verifyEnable && isCanJiguangLogin){
            // 满足极光一键登录的条件
            loginAuth(context);
        }else {
            context.startActivity(new Intent(context, LoginNewActivity.class));
        }
    }


    private static void loginAuth(final Activity context) {
        JVerificationInterface.setCustomUIWithConfig(BaseApplication.getUIConfig(context));
        JVerificationInterface.loginAuth(context, true, new VerifyListener() {
            @Override
            public void onResult(final int code, final String content, final String operator) {
                // code: 返回码，6000代表loginToken获取成功，6001代表loginToken获取失败，其他返回码详见描述
                // content：返回码的解释信息，若获取成功，内容信息代表loginToken。
                // operator：成功时为对应运营商，CM代表中国移动，CU代表中国联通，CT代表中国电信。失败时可能为null
                if (code == 6000){
                    // 验证接口
                    BaseApplication.setToken(context, content);
                    JiGuangLoginUtil.verifyUserInfo(context);
                }
            }
        });
    }

}
