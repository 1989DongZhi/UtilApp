package com.dong.android.ui.test;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.dong.android.R;
import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.base.view.BaseActivity;
import com.dong.android.utils.eventbus.EventUtils;
import com.dong.utils.GadgetUtils;
import com.dong.utils.UIUtils;
import com.dong.utils.data.FileUtils;
import com.dong.utils.log.LogUtils;
import com.dong.utils.preferences.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    public static final String TAG = TestActivity.class.getSimpleName();

    @OnClick({R.id.test_text1, R.id.test_text2, R.id.test_text3, R.id.test_text4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.test_text1:
//                showDialog();
                break;
            case R.id.test_text2:
//                testDemo();
                break;
            case R.id.test_text3:
//                saveFile();
                break;
            case R.id.test_text4:
                test();
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
    }

    private void showDialog() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("点击了text1")
                .setPositiveButton("确认", (dialog, which) -> {
                    LogUtils.e("点击了确认");
                }).setNegativeButton("取消", null).show();
    }

    private void testDemo() {
        UIUtils.showToast(PreferencesUtils.get("test_sp", "默认"));
        LogUtils.e("log test");
        EventBus.getDefault().post(new EventUtils<String>(EventUtils.T_CODE_TEST, EventUtils.A_CODE_TEST, "test EventBus"));
        String json = "{\"name\":\"tom\",\"sex\":\"男\",\"age\":\"24\", \"list\":[1,2,3,4,5],\"end\":\"最后一个\"}";
        LogUtils.j(json);
        ArrayList<String> list = new ArrayList<>();
        list.add("aaa000");
        list.add("aaa111");
        list.add("aaa222");
        list.add("aaa333");
        list.add("aaa444");
        for (int i = 0; i < 6; i++) {
            UIUtils.showToast(list.get(i));
        }
    }

    private void saveFile() {
    }

    private void test() {
        File file = FileUtils.createNewFile(FileUtils.getDirPath(mContext, FileUtils.DATA_PATH), "test.txt");
        LogUtils.e(GadgetUtils.formatTime(file.lastModified()));
    }

}