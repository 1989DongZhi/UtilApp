package com.dong.android.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.dong.android.app.AppManager;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/23.
 * 描述：界面显示工具
 * 主要包括 Toast、
 */

public class UIUtils {

    public static Context getContext() {
        return AppManager.getAppContext();
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return AppManager.getMainThreadHandler();
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * 延时在主线程执行runnable
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    /**
     * 从主线程looper里面移除runnable
     */
    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    public static void showToast(final CharSequence body) {
        if (TextUtils.isEmpty(body)) return;
        post(() -> Toast.makeText(getContext(), body, Toast.LENGTH_SHORT).show());
    }

    public static void showToast(final int resId) {
        if (TextUtils.isEmpty(getContext().getResources().getText(resId))) return;
        post(() -> showToast(getContext().getResources().getText(resId)));
    }

    public static void showToastLong(final CharSequence body) {
        if (TextUtils.isEmpty(body)) return;
        post(() -> Toast.makeText(getContext(), body, Toast.LENGTH_LONG).show());
    }

    public static void showToastLong(final int resId) {
        if (TextUtils.isEmpty(getContext().getResources().getText(resId))) return;
        post(() -> showToastLong(getContext().getResources().getText(resId)));
    }

    /**
     * dip 转换 px
     */
    public static int dip2px(int dip) {
        final float scale = AppManager.getRes().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * dip 转换 px
     */
    public static int dip2px(float dip) {
        final float scale = AppManager.getRes().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * px 转换 dip
     */
    public static int px2dip(int px) {
        final float scale = AppManager.getRes().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * px 转换 dip
     */
    public static int px2dip(float px) {
        final float scale = AppManager.getRes().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

}
