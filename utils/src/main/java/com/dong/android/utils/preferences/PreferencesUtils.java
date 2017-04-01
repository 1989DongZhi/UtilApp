package com.dong.android.utils.preferences;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * @author <dr_dong>
 * @time 2017/3/15 16:48
 */
public class PreferencesUtils {

    public static final String TAG = PreferencesUtils.class.getSimpleName();
    public static final String SP_URI = "content://com.dong.android.sp";
    private static final Uri sUri = Uri.parse(SP_URI);
    private static PreferencesUtils instance;
    private static Context context;

    private PreferencesUtils(Context context) {
        this.context = context;
    }

    public static PreferencesUtils getInstance(Context context) {
        if (context == null) {
            Log.e(TAG, "Context is null", new NullPointerException());
            return null;
        }
        if (instance == null) {
            instance = new PreferencesUtils(context);
        }
        return instance;
    }

    private static boolean hasContext() {
        if (context == null) {
            Log.e(TAG, "Context is null", new NullPointerException());
            return false;
        }
        return true;
    }

    public static void put(String key, String value) {
        if (hasContext()) {
            Bundle data = new Bundle();
            data.putString(PreferencesProvider.EXTRA_KEY, key);
            data.putString(PreferencesProvider.EXTRA_VALUE, value);
            context.getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_STRING, null, data);
        }
    }

    public static String get(String key, String defValue) {
        if (hasContext()) {
            Bundle data = new Bundle();
            data.putString(PreferencesProvider.EXTRA_KEY, key);
            data.putString(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
            Bundle replyData = context.getContentResolver().call(sUri, PreferencesProvider.METHOD_GET_STRING, null, data);
            return replyData.getString(PreferencesProvider.EXTRA_VALUE, defValue);
        } else {
            return defValue;
        }
    }

    public static void put(String key, boolean value) {
        if (hasContext()) {
            Bundle data = new Bundle();
            data.putString(PreferencesProvider.EXTRA_KEY, key);
            data.putBoolean(PreferencesProvider.EXTRA_VALUE, value);
            context.getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_BOOLEAN, null, data);
        }
    }

    public static boolean get(String key, boolean defValue) {
        if (hasContext()) {
            Bundle data = new Bundle();
            data.putString(PreferencesProvider.EXTRA_KEY, key);
            data.putBoolean(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
            Bundle replyData = context.getContentResolver().call(sUri, PreferencesProvider.METHOD_GET_BOOLEAN, null, data);
            return replyData.getBoolean(PreferencesProvider.EXTRA_VALUE, defValue);
        } else {
            return defValue;
        }

    }

    public static void put(String key, int value) {
        if (hasContext()) {
            Bundle data = new Bundle();
            data.putString(PreferencesProvider.EXTRA_KEY, key);
            data.putInt(PreferencesProvider.EXTRA_VALUE, value);
            context.getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_INT, null, data);
        }
    }

    public static int get(String key, int defValue) {
        if (hasContext()) {
            Bundle data = new Bundle();
            data.putString(PreferencesProvider.EXTRA_KEY, key);
            data.putInt(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
            Bundle replyData = context.getContentResolver().call(sUri, PreferencesProvider.METHOD_GET_INT, null, data);
            return replyData.getInt(PreferencesProvider.EXTRA_VALUE, defValue);
        } else {
            return defValue;
        }
    }

    public static void put(String key, float value) {
        if (hasContext()) {
            Bundle data = new Bundle();
            data.putString(PreferencesProvider.EXTRA_KEY, key);
            data.putFloat(PreferencesProvider.EXTRA_VALUE, value);
            context.getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_FLOAT, null, data);
        }
    }

    public static float get(String key, float defValue) {
        if (hasContext()) {
            Bundle data = new Bundle();
            data.putString(PreferencesProvider.EXTRA_KEY, key);
            data.putFloat(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
            Bundle replyData = context.getContentResolver().call(sUri, PreferencesProvider.METHOD_GET_FLOAT, null, data);
            return replyData.getFloat(PreferencesProvider.EXTRA_VALUE, defValue);
        } else {
            return defValue;
        }
    }

    public static void put(String key, long value) {
        if (hasContext()) {
            Bundle data = new Bundle();
            data.putString(PreferencesProvider.EXTRA_KEY, key);
            data.putLong(PreferencesProvider.EXTRA_VALUE, value);
            context.getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_LONG, null, data);
        }
    }

    public static long get(String key, long defValue) {
        if (hasContext()) {
            Bundle data = new Bundle();
            data.putString(PreferencesProvider.EXTRA_KEY, key);
            data.putLong(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
            Bundle replyData = context.getContentResolver().call(sUri, PreferencesProvider.METHOD_GET_LONG, null, data);
            return replyData.getLong(PreferencesProvider.EXTRA_VALUE, defValue);
        } else {
            return defValue;
        }
    }


}
