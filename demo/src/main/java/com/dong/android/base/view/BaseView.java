package com.dong.android.base.view;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public interface BaseView {
    /**
     * 显示加载进度
     */
    void showLoading();

    /**
     * 取消加载进度
     */
    void dismissLoading();

    /**
     * 显示Error信息
     *
     * @param msg
     */
    void showError(String msg);

    /**
     * 显示网络Error信息
     */
    void showNetError();
}
