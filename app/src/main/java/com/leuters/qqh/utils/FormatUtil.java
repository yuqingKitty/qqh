package com.leuters.qqh.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public static final String FMT_YMD = "yyyy/MM/dd";

    public static String long2StrDate(long time) {
        if (time <= 0) {
            return  "";
        }
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FMT_YMD);
        return simpleDateFormat.format(date);
    }

}
