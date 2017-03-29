package com.dong.android.ui.test;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.dong.android.R;
import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.base.view.BaseActivity;
import com.dong.android.utils.LogUtils;
import com.dong.android.utils.UIUtils;
import com.dong.android.utils.eventbus.EventUtils;
import com.dong.android.utils.preferences.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    public static final String TAG = TestActivity.class.getSimpleName();

    @OnClick({R.id.test_text1, R.id.test_text2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.test_text1:
                showDialog();
                break;
            case R.id.test_text2:
                testDemo();
                break;
            default:
                break;
        }

    }


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
//        testDemo();
    }

    private void testDemo() {
        UIUtils.showToast(PreferencesUtils.getString(mContext, "test_sp", "默认"));
        LogUtils.e("log test");
        EventBus.getDefault().post(new EventUtils<String>(EventUtils.T_CODE_TEST, EventUtils.A_CODE_TEST, "test EventBus"));
        String json = "{\"name\":\"tom\",\"sex\":\"男\",\"age\":\"24\", \"list\":[1,2,3,4,5],\"end\":\"最后一个\"}";
        LogUtils.j(json);
    }

    private void showDialog() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("点击了text1")
                .setPositiveButton("确认", (dialog, which) -> {
                    LogUtils.e("点击了确认");
                }).setNegativeButton("取消", null).show();
    }

}