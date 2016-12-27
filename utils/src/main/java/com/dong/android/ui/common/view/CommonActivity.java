package com.dong.android.ui.common.view;

import android.os.Bundle;

import com.dong.android.base.view.BaseActivity;
import com.dong.android.ui.common.model.CommonModel;
import com.dong.android.ui.common.presenter.CommonPresenter;

/**
 * 作者：<Dr_dong>
 * 日期：2016/12/11.
 * 描述：
 */

public class CommonActivity extends BaseActivity<CommonPresenter> implements CommonModel {
    @Override
    protected int getRootView() {
        return 0;
    }

    @Override
    protected CommonPresenter createPresenter() {
        return null;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
