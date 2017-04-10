package com.dong.utils;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/23.
 * 描述：界面显示工具
 * 主要包括 Toast、
 */

public class UIUtils {

    private static final String TAG = UIUtils.class.getSimpleName();
    private static UIUtils instance;
    private static Context context;
    private static Handler handler;

    private UIUtils(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public static UIUtils getInstance(Context context, Handler handler) {
        if (context == null) {
            Log.e(TAG, "Context is null", new NullPointerException());
            return null;
        }
        if (handler == null) {
            Log.e(TAG, "Handler is null", new NullPointerException());
            return null;
        }
        if (instance == null) {
            instance = new UIUtils(context, handler);
        }
        return instance;
    }

    private static Context getContext() {
        if (context == null) {
            Log.e(TAG, "UIUtils cannot be instantiated", new UnsupportedOperationException());
        }
        return context;
    }

    private static Handler getHandler() {
        if (handler == null) {
            Log.e(TAG, "UIUtils cannot be instantiated", new UnsupportedOperationException());
        }
        return handler;
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

    public static void showToast(@NonNull CharSequence body) {
        post(() -> Toast.makeText(getContext(), body, Toast.LENGTH_SHORT).show());
    }

    public static void showToast(@StringRes int resId) {
        post(() -> showToast(getContext().getResources().getText(resId)));
    }

    public static void showToastLong(@NonNull CharSequence body) {
        post(() -> Toast.makeText(getContext(), body, Toast.LENGTH_LONG).show());
    }

    public static void showToastLong(@StringRes int resId) {
        post(() -> showToastLong(getContext().getResources().getText(resId)));
    }

    /**
     * dip 转换 px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * dip 转换 px
     */
    public static int dip2px(float dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * px 转换 dip
     */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * px 转换 dip
     */
    public static int px2dip(float px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取Pixel
     *
     * @param resId dip资源ID
     * @return
     */
    public static int getDimenSize(int resId) {
        return getContext().getResources().getDimensionPixelSize(resId);
    }

}
