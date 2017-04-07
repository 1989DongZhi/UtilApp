package com.dong.android.utils.log;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/24.
 * 描述：日志输出工具类
 */

public class LogUtils {

    private static final String TAG = LogUtils.class.getSimpleName();

    /**
     * 调试开关,日志控制,默认打开,发布时关闭
     */
    private static boolean isDebug = true;

    public static void setLogDebug(boolean logDebug) {
        isDebug = logDebug;
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isDebug)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (isDebug)
            Log.v(tag, msg, tr);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (isDebug)
            Log.d(tag, msg, tr);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (isDebug)
            Log.i(tag, msg, tr);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (isDebug)
            Log.w(tag, msg, tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isDebug)
            Log.e(tag, msg, tr);
    }

    /**
     * 打印Map
     */
    public static void m(Map map) {
        Set set = map.entrySet();
        if (set.isEmpty()) {
            printLog('D', "[]");
            return;
        }

        int i = 0;
        String[] s = new String[set.size()];
        for (Object aSet : set) {
            Map.Entry entry = (Map.Entry) aSet;
            s[i] = entry.getKey() + " = " + entry.getValue() + ",\n";
            i++;
        }
        printLog('V', s);
    }

    /**
     * 打印JSON
     *
     * @param jsonStr
     */
    public static void j(String jsonStr) {
        if (isDebug) {
            int jsonIndent = 4;
            String message;
            try {
                if (jsonStr.startsWith("{")) {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    message = jsonObject.toString(jsonIndent); //这个是核心方法
                } else if (jsonStr.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    message = jsonArray.toString(jsonIndent);
                } else {
                    message = jsonStr;
                }
            } catch (JSONException e) {
                message = jsonStr;
            }
            String[] lines = message.split(System.lineSeparator());
            printLog('D', lines);
        }
    }

    public static void i(String... msg) {
        if (isDebug)
            printLog('I', msg);
    }

    public static void d(String... msg) {
        if (isDebug)
            printLog('D', msg);
    }

    public static void w(String... msg) {
        if (isDebug)
            printLog('W', msg);
    }

    public static void e(String... msg) {
        if (isDebug)
            printLog('E', msg);
    }

    public static void v(String... msg) {
        if (isDebug)
            printLog('V', msg);
    }

    /**
     * 打印log
     *
     * @param type
     * @param msg
     */
    private static void printLog(char type, String... msg) {
        printHunk(type, getLogLocation() + " ===> Thread: " + Thread.currentThread().getName() + " ===");
        if (msg == null || msg.length == 0) {
            return;
        }
        printHunk(type, "===| msg:");
        for (String str : msg) {
            printHunk(type, "===| " + str);
        }
        printHunk(type, "================================");
    }

    /**
     * 打印Log被调用的位置
     */
    private static String getLogLocation() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        int i = 0;
        for (StackTraceElement e : stack) {
            if (e.getClassName().equals(LogUtils.class.getName())) {
                break;
            } else {
                i++;
            }
        }
        i += 3;
        String className = stack[i].getFileName();
        String methodName = stack[i].getMethodName();
        int lineNumber = stack[i].getLineNumber();
        return "===> Location: (" + className + ":" + lineNumber + ") " + methodName + "()";
    }

    /**
     * 同意打印
     *
     * @param type
     * @param str
     */
    private static void printHunk(char type, String str) {
        switch (type) {
            case 'I':
                Log.i(TAG, str);
                break;
            case 'D':
                Log.d(TAG, str);
                break;
            case 'V':
                Log.v(TAG, str);
                break;
            case 'W':
                Log.w(TAG, str);
                break;
            case 'E':
                Log.e(TAG, str);
                break;
            case 'A':
                Log.wtf(TAG, str);
                break;
            default:
                break;
        }
    }

}
