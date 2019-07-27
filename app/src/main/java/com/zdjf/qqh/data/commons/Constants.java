package com.zdjf.qqh.data.commons;

import android.os.Environment;

/**
 * 常量
 */
public class Constants {
    //是否发布
    public static final boolean IS_RELEASE = true;
    //是否隐藏log
    public static final boolean HIDE_LOG = false;

    /**
     * 定义SD卡存储的根路径
     */
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/qqh_market";
    public final static String FILE_PATH = ROOT_PATH + "/file";
    /**
     * 保存用户信息
     */
    public static final String LOGIN_SAVE_KEY = "LOGIN_SAVE_KEY";
    /**
     * 保存token
     */
    public static final String TOKEN_KEY = "TOKEN_KEY";
    /**
     * 保存userId
     */
    public static final String USER_ID_KEY = "USER_ID_KEY";
    /**
     * 注册协议类型
     */
    public static final String REGIST_CMSTYPE = "REGIST";
    /**
     * 公司简介
     */
    public static final String SYSTEM_CMSTYPE = "SYSTEM";

    public static final String CMSTYPE_INTENT_KEY = "cmsType";
    public static final String TITLE_INTENT_KEY = "title";

    public static final String PHOTO_JPG_FORMAT = ".jpg";
    public static final String EXTRA_TYPE = "extra_type";
    /**
     * 选择照片回调
     */
    public static final int CHOOSE_PICTURE = 2;
    /**
     * 相机回调
     */
    public static final int INTENT_REQUESTCODE_CAMERA_SYSTEM = 3;
    /**
     * 裁剪图片
     */
    public static final int PHOTO_REQUEST_RESULT = 5;

    public static final int RXBUS_LOGIN_SUCCESS_KEY = 1001;
    public static final int RXBUS_LOGOUT_SUCCESS_KEY = 1002;
    public static final int RXBUS_UPLOAAD_SUCCESS_KEY = 1003;
    public static final int RXBUS_NICKNAME_SUCCESS_KEY = 1004;
    public static final int RXBUS_TO_COMPLETE_KEY = 1005;
    /**
     * 阿里云上传文件路径地址
     */
    public static final String USER_IMG = "dc/head_img/"; //用户头像地址
    public static final String FEEDBACK_IMG = "dc/feed_back/"; //用户反馈地址

    public enum moduleName {
        TypeProduct("0"), Banner("1"), Notice("2"), Type("3"), Recommend("4"), ScrollBottom("5"), MyLoanRecord("6"), Service("7"), Setting("8"), MessageCenter("9"), Complete("10");

        private String name;

        moduleName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
