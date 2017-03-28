package com.dong.android.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dong.android.ui.gnative.AndroidNativeActivity;
import com.dong.android.ui.listsample.ListActivity;
import com.dong.android.ui.listsample.SampleData;
import com.dong.android.ui.test.GlideLoadImageActivity;
import com.dong.android.ui.test.SVGTintActivity;
import com.dong.android.ui.test.TestActivity;
import com.dong.android.utils.LogUtils;
import com.dong.android.utils.eventbus.EventUtils;
import com.dong.android.utils.preferences.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：<Dr_dong>
 * 日期：2016/12/11.
 * 描述：
 */

public class MainActivity extends ListActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected List<SampleData> sampleData() {
        final List<SampleData> sampleData = new ArrayList<>();
        sampleData.add(new SampleData("AndroidNative", AndroidNativeActivity.class.getName()));
        sampleData.add(new SampleData("Glide Load Image", GlideLoadImageActivity.class.getName()));
        sampleData.add(new SampleData("SVG Tint", SVGTintActivity.class.getName()));
        sampleData.add(new SampleData("Test", TestActivity.class.getName()));
        addTestData();
        return sampleData;
    }

    private void addTestData() {
        PreferencesUtils.putString(mContext, "test_sp", "这是一条sp测试");
    }

    @Subscribe
    public void testEvent(EventUtils<String> event) {
        LogUtils.e("EventBus test");
        if (event.getTransmittedCode() == EventUtils.T_CODE_TEST
                && event.getAcceptanceCode() == EventUtils.A_CODE_TEST) {
            LogUtils.e(event.getT());
        }
    }

}
