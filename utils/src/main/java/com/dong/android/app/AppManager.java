package com.dong.android.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;

import com.dong.android.BuildConfig;
import com.dong.android.utils.UIUtils;
import com.dong.android.utils.log.LogUtils;
import com.dong.android.utils.net.NetworkUtils;
import com.dong.android.utils.preferences.PreferencesUtils;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/23.
 * 描述：应用全局工具类
 */

public class AppManager extends Application {

    public static String PACKAGE_NAME;
    public static String PACKAGE_VERSION_NAME;
    public static int PACKAGE_VERSION_CODE;
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
        //初始化基本信息
        initBaseInfo();
        LogUtils.setLogDebug(BuildConfig.DEBUG);
        //初始化UI工具
        UIUtils.getInstance(mAppContext, mainThreadHandler);
        //初始化网络工具
        NetworkUtils.getInstance(mAppContext);
        //初始化异常获取工具
//        CrashHandler.getInstance(mAppContext);
        //初始化轻量级的数据Preferences
        PreferencesUtils.getInstance(mAppContext);
    }

    private void initBaseInfo() {
        PACKAGE_NAME = getPackageName();
        try {
            PackageInfo packageInfo = getPackageManager()
                    .getPackageInfo(PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            PACKAGE_VERSION_CODE = packageInfo.versionCode;
            PACKAGE_VERSION_NAME = packageInfo.versionName == null ? "null" : packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            PACKAGE_VERSION_CODE = -1;
            PACKAGE_VERSION_NAME = "null";
        }
    }
}
