package com.dong.android.net;

/**
 * @author <dr_dong>
 * @time 2017/4/1 11:04
 */
public class ServerApi {

    //微信服务器
    public static final String WX_SERVER = "https://api.weixin.qq.com/";
    private static final String LOGON_SUFFIX = "login";
    private static final String LOGON_OUT_SUFFIX = "logout";
    public static String SERVER = "http://eat-admin.dothinkings.com/";
    // 登录
    public static String LOGON = SERVER + LOGON_SUFFIX;
    public static String LOGON_OUT = SERVER + LOGON_OUT_SUFFIX;
}
