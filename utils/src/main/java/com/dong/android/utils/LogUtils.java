package com.dong.android.utils;

import android.util.Log;

import com.dong.android.app.AppManager;

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

    public static void setLogDebug(boolean logDebug) {
        DEBUG = logDebug;
    }

    public static void e(String msg) {
        if (DEBUG)
            e("===>" + Thread.currentThread().getStackTrace()[3].getClassName().replace(AppManager.PACKAGE_NAME + ".", "") +
                            "===>" + Thread.currentThread().getStackTrace()[3].getMethodName() +
                            "===>" + Thread.currentThread().getStackTrace()[3].getLineNumber() + "===>"
                    , msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Log.e(tag, msg);
    }

    public static void w(String msg) {
        if (DEBUG)
            w("===>" + Thread.currentThread().getStackTrace()[3].getClassName().replace(AppManager.PACKAGE_NAME + ".", "") +
                    "===>" + Thread.currentThread().getStackTrace()[3].getMethodName() +
                    "===>" + Thread.currentThread().getStackTrace()[3].getLineNumber() + "===>", msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG)
            Log.w(tag, msg);
    }

    public static void i(String msg) {
        if (DEBUG)
            i("===>" + Thread.currentThread().getStackTrace()[3].getClassName().replace(AppManager.PACKAGE_NAME + ".", "") +
                    "===>" + Thread.currentThread().getStackTrace()[3].getMethodName() +
                    "===>" + Thread.currentThread().getStackTrace()[3].getLineNumber() + "===>", msg);
    }

    public static void i(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    public static void d(String msg) {
        if (DEBUG)
            d("===>" + Thread.currentThread().getStackTrace()[3].getClassName().replace(AppManager.PACKAGE_NAME + ".", "") +
                    "===>" + Thread.currentThread().getStackTrace()[3].getMethodName() +
                    "===>" + Thread.currentThread().getStackTrace()[3].getLineNumber() + "===>", msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    public static void v(String msg) {
        if (DEBUG)
            v("===>" + Thread.currentThread().getStackTrace()[3].getClassName().replace(AppManager.PACKAGE_NAME + ".", "") +
                    "===>" + Thread.currentThread().getStackTrace()[3].getMethodName() +
                    "===>" + Thread.currentThread().getStackTrace()[3].getLineNumber() + "===>", msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG)
            Log.v(tag, msg);
    }

}
