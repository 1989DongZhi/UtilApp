package com.dong.android.utils.net;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author <dr_dong>
 * @time 2017/4/1 11:11
 */
public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static NetworkUtils instance;
    private static Context context;

    private NetworkUtils(Context context) {
        this.context = context;
    }

    public static NetworkUtils getInstance(Context context) {
        if (context == null) {
            Log.e(TAG, "Context is null", new NullPointerException());
            return null;
        }
        if (instance == null) {
            instance = new NetworkUtils(context);
        }
        return instance;
    }

    private static Context getContext() {
        if (context == null) {
            Log.e(TAG, "NetworkUtils cannot be instantiated", new UnsupportedOperationException());
        }
        return context;
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isAvailable()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi() {
        ConnectivityManager connectivity = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivity != null && null != connectivity.getActiveNetworkInfo() && connectivity.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }


    /**
     * 使用SSL不信任的证书
     */
    public static void useUntrustedCertificate() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
