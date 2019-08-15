package com.leuters.qqh.data.commons;

import static com.leuters.qqh.data.commons.Constants.IS_RELEASE;

/**
 * 地址
 */
public class Urls {
    /**
     * 生产地址
     */
    private static String ReleaseIp = "http://qqh.leuters.com.cn/bf/";
//    private static String ReleaseIp = "http://qqh-test.leuters.com.cn/bf/";
    private static String testIp = "http://192.168.0.159:8381/";
    public static String baseUrl = IS_RELEASE ? ReleaseIp : testIp;

    public static String AliyunToken = "app/pub/getAliyunStsToken"; //获取阿里云token地址
    /**
     * 阿里云域名
     */
    public static String endPoints = "http://oss-cn-shenzhen.aliyuncs.com";
    public static String imagePath = "https://ltkj-qqh.oss-cn-shenzhen.aliyuncs.com/";
    /**
     * 阿里云BucketName
     */
    public static final String ALIBucketName = "ltkj-qqh";
}
