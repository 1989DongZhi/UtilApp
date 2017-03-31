package com.dong.android.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author <dr_dong>
 * @time 2017/3/31 14:44
 * 小工具，处理一些简单的事务
 */
public class GadgetUtils {

    public static String getCurrentTime() {
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault());
        String time = sdf.format(date);
        return time;
    }

}
