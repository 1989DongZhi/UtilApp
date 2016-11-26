package com.dong.android.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.dong.android.BuildConfig;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/23.
 * 描述：应用全局工具类
 */

public class AppUtils extends Application {

    private static Context mAppContext;
    private static Handler mainThreadHandler;
    private AppUtils instance;

    public static Context getAppContext() {
        return mAppContext;
    }

    public static Handler getMainThreadHandler() {
        return mainThreadHandler;
    }

    public AppUtils getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mAppContext = getApplicationContext();
        mainThreadHandler = new Handler();
        LogUtils.setLogDebug(BuildConfig.DEBUG);
    }
}
