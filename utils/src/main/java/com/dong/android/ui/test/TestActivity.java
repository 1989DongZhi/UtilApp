package com.dong.android.ui.test;

import android.os.Bundle;

import com.dong.android.R;
import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.base.view.BaseActivity;
import com.dong.android.utils.LogUtils;
import com.dong.android.utils.UIUtils;
import com.dong.android.utils.eventbus.EventUtils;
import com.dong.android.utils.preferences.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;

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
        UIUtils.showToast(PreferencesUtils.getString(mContext, "test_sp", "默认"));
        LogUtils.e("log test");
        EventBus.getDefault().post(new EventUtils<String>(EventUtils.T_CODE_TEST, EventUtils.A_CODE_TEST, "test EventBus"));
    }

}