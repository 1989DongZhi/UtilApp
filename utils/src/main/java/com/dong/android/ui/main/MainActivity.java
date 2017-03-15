package com.dong.android.ui.main;

import com.dong.android.ui.gnative.AndroidNativeActivity;
import com.dong.android.ui.listsample.ListActivity;
import com.dong.android.ui.listsample.SampleData;
import com.dong.android.ui.test.GlideLoadImageActivity;
import com.dong.android.ui.test.SVGTintActivity;
import com.dong.android.ui.test.TestActivity;

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
    protected List<SampleData> sampleData() {
        final List<SampleData> sampleData = new ArrayList<>();
        sampleData.add(new SampleData("AndroidNative", AndroidNativeActivity.class.getName()));
        sampleData.add(new SampleData("Test", TestActivity.class.getName()));
        sampleData.add(new SampleData("Glide Load Image", GlideLoadImageActivity.class.getName()));
        sampleData.add(new SampleData("SVG Tint", SVGTintActivity.class.getName()));
        return sampleData;
    }
}
