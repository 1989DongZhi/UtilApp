package com.dong.android.utils.files;

import com.dong.android.utils.AppUtils;

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
        return AppUtils.getRes().getDimensionPixelSize(resId);
    }
}
