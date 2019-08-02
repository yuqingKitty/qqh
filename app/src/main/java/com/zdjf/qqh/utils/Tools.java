package com.zdjf.qqh.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.zdjf.qqh.application.BaseApplication;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Enumeration;

public class Tools {

    /**
     * 获取设备唯一性，生成规则如下
     */
    public static String getDeviceId() {
        Context context = BaseApplication.getContext();
        String deviceId = "";
        String imei = "";
        if (Build.VERSION.SDK_INT < 23) {
            imei = getImei(context, deviceId);
        } else {
            //6.0以后的不做处理,因为即使没有imei,deviceid的值也是唯一的,只要uuid不变的话.
            //如果6.0以后让imei参与生成md5值,会发生用户没授权与授权的两种情况,导致同一台手机不同的deviceid
        }

        String android_id = getAndroidId(context);
        String mac_Adress = "";
        if (hasPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
            mac_Adress = getMacAddress(context);
        }
        //String randUUid = UUID.randomUUID().toString();
        String randUUid = "";
        deviceId = getMD5String(new StringBuilder().append(imei).append(android_id).append(mac_Adress).append(randUUid).toString());
        return deviceId;
    }

    /**
     * 获取Imei
     */
    @SuppressLint("MissingPermission")
    public static String getImei(Context context, String imei) {
        try {
            if (hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                imei = telephonyManager.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }

    /**
     * 检测权限
     */
    public static boolean hasPermission(Context context, String thePermission) {
        try {
            if (null == context || TextUtils.isEmpty(thePermission))
                throw new IllegalArgumentException("empty params");
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(thePermission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取Android ID
     */
    public static String getAndroidId(Context context) {
        String androidId = "";
        try {
            /**
             * java.lang.SecurityException:Unable to find app for caller android.app.ApplicationThreadProxy 异常
             * 异常的设备型号是：HUAWEI-T1-821w、Xiaomi-MI PAD
             * */
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (SecurityException e) {
        } catch (Exception e) {
        }
        return androidId;
    }

    /**
     * 获取 Md 值
     * 此方法 全局通用
     */
    public static String getMD5String(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            char[] charArray = inStr.toCharArray();
            byte[] byteArray = new byte[charArray.length];

            for (int i = 0; i < charArray.length; i++) {
                byteArray[i] = (byte) charArray[i];
            }
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取手机mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String macAddress = "";
        if (Build.VERSION.SDK_INT >= 23) {
            macAddress = getMacAddressWithJavaInterface();
        } else {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if ((wifiManager != null) && (wifiManager.getConnectionInfo()) != null) {
                macAddress = wifiManager.getConnectionInfo().getMacAddress();
            }
        }
        return macAddress;
    }


    /**
     * 安卓6.0以上获取mac地址的方法
     *
     * @return
     */
    private static String getMacAddressWithJavaInterface() {
        String macAddress = "";
        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iF = interfaces.nextElement();
                byte[] addr;
                addr = iF.getHardwareAddress();
                if (addr == null || addr.length == 0) {
                    continue;
                }
                StringBuilder buf = new StringBuilder();
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                if (iF.getName().equals("wlan0")) {
                    macAddress = buf.toString();
                    break;
                }
            }
        } catch (SocketException e) {
            LogUtil.e("device_info", "getMacAddressWithJavaInterface: " + e.toString());
        }
        return macAddress;
    }

}
