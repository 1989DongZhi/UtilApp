package com.dong.android.ui.test;

import android.os.Bundle;

import com.dong.android.R;
import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.base.view.BaseActivity;

public class TestActivity extends BaseActivity {
    public static final String TAG = TestActivity.class.getSimpleName();

    @Override
    protected int getRootView() {
        return R.layout.activity_test;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {


    }

}