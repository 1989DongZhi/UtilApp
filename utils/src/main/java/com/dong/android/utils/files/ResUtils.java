package com.dong.android.utils.files;

import com.dong.android.app.AppManager;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/27.
 * 描述：
 */

public class ResUtils {

    /**
     * 获取Pixel
     *
     * @param resId dip资源ID
     * @return
     */
    public static int getDimenSize(int resId) {
        return AppManager.getRes().getDimensionPixelSize(resId);
    }
}
