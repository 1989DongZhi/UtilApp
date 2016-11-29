package com.dong.android.ui.main.presenter;

import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.ui.main.model.MainModelImpl;
import com.dong.android.ui.main.view.MainView;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public class MainPresenter extends BasePresenter<MainView, MainModelImpl> {

    private static final String TAG = MainPresenter.class.getSimpleName();

    @Override
    protected MainModelImpl getModelImpl() {
        return new MainModelImpl();
    }
}
