package com.dong.android.utils.preferences;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

/**
 * @author <dr_dong>
 * @time 2017/3/15 16:48
 */
public class PreferencesUtils {

    public static final String SP_URI = "content://com.dong.android.sp";
    private static final Uri sUri = Uri.parse(SP_URI);

    public static void putString(Context context, String key, String value) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putString(PreferencesProvider.EXTRA_VALUE, value);
        context.getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_STRING, null, data);
    }

    public static String getString(Context context, String key, String defValue) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putString(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
        Bundle replyData = context.getContentResolver().call(sUri, PreferencesProvider.METHOD_GET_STRING, null, data);
        return replyData.getString(PreferencesProvider.EXTRA_VALUE, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putBoolean(PreferencesProvider.EXTRA_VALUE, value);
        context.getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_BOOLEAN, null, data);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putBoolean(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
        Bundle replyData = context.getContentResolver().call(sUri, PreferencesProvider.METHOD_GET_BOOLEAN, null, data);
        return replyData.getBoolean(PreferencesProvider.EXTRA_VALUE, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putInt(PreferencesProvider.EXTRA_VALUE, value);
        context.getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_INT, null, data);
    }

    public static int getInt(Context context, String key, int defValue) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putInt(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
        Bundle replyData = context.getContentResolver().call(sUri, PreferencesProvider.METHOD_GET_INT, null, data);
        return replyData.getInt(PreferencesProvider.EXTRA_VALUE, defValue);
    }

    public static void putFloat(Context context, String key, float value) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putFloat(PreferencesProvider.EXTRA_VALUE, value);
        context.getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_FLOAT, null, data);
    }

    public static float getFloat(Context context, String key, float defValue) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putFloat(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
        Bundle replyData = context.getContentResolver().call(sUri, PreferencesProvider.METHOD_GET_FLOAT, null, data);
        return replyData.getFloat(PreferencesProvider.EXTRA_VALUE, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putLong(PreferencesProvider.EXTRA_VALUE, value);
        context.getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_LONG, null, data);
    }

    public static long getLong(Context context, String key, long defValue) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putLong(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
        Bundle replyData = context.getContentResolver().call(sUri, PreferencesProvider.METHOD_GET_LONG, null, data);
        return replyData.getLong(PreferencesProvider.EXTRA_VALUE, defValue);
    }


}
