package com.dong.android.base.presenter;

import com.dong.android.base.view.BaseView;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public interface Presenter<V extends BaseView> {

    /**
     * 给Presenter设置对应的View引用
     *
     * @param view
     */
    void attachView(V view);

    /**
     * Presenter中的view引用设置为null
     */
    void detachView();

//    void detachView(boolean retainInstance);
}
