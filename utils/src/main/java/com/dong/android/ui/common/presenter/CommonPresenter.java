package com.dong.android.ui.common.presenter;

import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.ui.common.model.CommonModelImpl;
import com.dong.android.ui.common.view.CommonView;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public class CommonPresenter extends BasePresenter<CommonView, CommonModelImpl> {

    private static final String TAG = CommonPresenter.class.getSimpleName();

    @Override
    protected CommonModelImpl getModelImpl() {
        return new CommonModelImpl();
    }
}
