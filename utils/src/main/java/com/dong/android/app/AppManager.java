package com.dong.android.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import com.dong.android.BuildConfig;
import com.dong.android.utils.LogUtils;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/23.
 * 描述：应用全局工具类
 */

public class AppManager extends Application {

    private static Context mAppContext;
    private static Handler mainThreadHandler;
    private static Resources resources;
    private AppManager instance;

    public static Context getAppContext() {
        return mAppContext;
    }

    public static Resources getRes() {
        return resources;
    }

    public static Handler getMainThreadHandler() {
        return mainThreadHandler;
    }

    public AppManager getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mAppContext = getApplicationContext();
        resources = mAppContext.getResources();
        mainThreadHandler = new Handler();
        LogUtils.setLogDebug(BuildConfig.DEBUG);
    }
}
