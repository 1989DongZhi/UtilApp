package com.dong.utils.manager;

import android.app.Activity;

import java.util.Stack;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public class ActivitiesManager {
    public static Stack<Activity> activityStack = new Stack<>();

    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 移除Activity出堆栈
     */
    public static void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (activityStack.size() == 0)
            return null;
        else
            return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param index 若<0 则为反向序列号
     */
    public static void finishActivity(int index) {
        if (index < activityStack.size() && index + activityStack.size() > 0) {
            finishActivity((activityStack.size() + index) % activityStack.size());
        }
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        int size = activityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            if (activityStack.get(i).getClass().equals(cls)) {
                finishActivity(activityStack.get(i));
                break;
            }
        }
    }


    /**
     * 结束指定类名的Activity以外的Activity
     */
    public static void finishOtherActivities(Class<?> cls) {
        int size = activityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            if (!activityStack.get(i).getClass().equals(cls)) {
                finishActivity(activityStack.get(i));
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        int size = activityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            finishActivity(activityStack.get(i));
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     */
    public static Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 判断activity是否启动
     *
     * @param clazz
     * @return
     */
    public static boolean isActivityExist(Class<?> clazz) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    public static void exitApp() {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }

}
