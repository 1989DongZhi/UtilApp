package com.dong.android.net;

import com.dong.utils.preferences.PreferencesUtils;

/**
 * @author <dr_dong>
 * @time 2017/4/1 11:27
 */
public class CookieUtils {

    private static final String JSESSIONID_KEY = "jsessionid";
    private static final String DOMAIN_KEY = "cookie_domain";
    private static final String JSESSIONID_TIME = "jsessionidtime";
    private static final long SIX_DAYS = 6 * 24 * 60 * 60 * 1000;

    public static String getSessionIdFromPref() {
        long jsessionid_time = PreferencesUtils.get(JSESSIONID_TIME, System.currentTimeMillis());
        if ((System.currentTimeMillis() - jsessionid_time) > SIX_DAYS) {
            return "";
        }
        return PreferencesUtils.get(JSESSIONID_KEY, "");
    }

    public static void addSessionIdToPref(String jsessionid) {
        PreferencesUtils.put(JSESSIONID_KEY, jsessionid);
        PreferencesUtils.put(JSESSIONID_TIME, System.currentTimeMillis());
    }

    public static String getDomainFromPref() {
        long jsessionid_time = PreferencesUtils.get(JSESSIONID_TIME, System.currentTimeMillis());
        if ((System.currentTimeMillis() - jsessionid_time) > SIX_DAYS) {
            return "";
        }
        return PreferencesUtils.get(DOMAIN_KEY, "");
    }

    public static void addDomainToPref(String domain) {
        PreferencesUtils.put(DOMAIN_KEY, domain);
        PreferencesUtils.put(JSESSIONID_TIME, System.currentTimeMillis());
    }

}
