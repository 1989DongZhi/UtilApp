package com.dong.android.utils;

import android.util.Log;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/24.
 * 描述：日志输出工具类
 */

public class LogUtils {

    /**
     * 调试开关,日志控制,默认打开,发布时关闭
     */
    public static boolean DEBUG = true;
    private static String TAG = "===>" + Thread.currentThread().getStackTrace()[3].getClassName() +
            "===>" + Thread.currentThread().getStackTrace()[3].getLineNumber() + "===>";

    public static void setLogDebug(boolean logDebug) {
        DEBUG = logDebug;
    }

    public static void e(String msg) {
        if (DEBUG)
            e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Log.e(tag, msg);
    }

    public static void w(String msg) {
        if (DEBUG)
            w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG)
            Log.w(tag, msg);
    }

    public static void i(String msg) {
        if (DEBUG)
            i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    public static void d(String msg) {
        if (DEBUG)
            d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    public static void v(String msg) {
        if (DEBUG)
            v(TAG, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

}
