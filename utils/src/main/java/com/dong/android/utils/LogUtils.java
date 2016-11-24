package com.dong.android.utils;

import android.util.Log;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/24.
 * 描述：日志输出工具类
 */

public class LogUtils {

    private static String TAG = "===>" + Thread.currentThread().getStackTrace()[3].getClassName() +
            "===>" + Thread.currentThread().getStackTrace()[3].getLineNumber() + "===>";

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

}
