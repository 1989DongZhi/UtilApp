package com.dong.android.ui.gnative;

import com.dong.android.ui.listsample.ListActivity;
import com.dong.android.ui.listsample.SampleData;
import com.dong.android.utils.UIUtils;
import com.dong.android.utils.preferences.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <dr_dong>
 * @time 2017/1/19 14:03
 */
public class AndroidNativeActivity extends ListActivity {

    public static final String TAG = AndroidNativeActivity.class.getSimpleName();

    @Override
    protected List<SampleData> sampleData() {
        final List<SampleData> sampleData = new ArrayList<>();
        sampleData.add(new SampleData("Basic", TestBasicActivity.class.getName()));
        sampleData.add(new SampleData("Basic2", TestBasic2Activity.class.getName()));
        sampleData.add(new SampleData("Empty", TestEmptyActivity.class.getName()));
        sampleData.add(new SampleData("Fullscreen", TestFullscreenActivity.class.getName()));
        sampleData.add(new SampleData("Login", TestLoginActivity.class.getName()));
        sampleData.add(new SampleData("Navigation", TestNavigationActivity.class.getName()));
        sampleData.add(new SampleData("Scrolling", TestScrollingActivity.class.getName()));
        sampleData.add(new SampleData("Settings", TestSettingsActivity.class.getName()));
        sampleData.add(new SampleData("Tabs", TestTabsActivity.class.getName()));
        sampleData.add(new SampleData("TabSpinner", TestTabSpinnerActivity.class.getName()));
        sampleData.add(new SampleData("TabSwipe", TestTabSwipeActivity.class.getName()));
        UIUtils.showToast(PreferencesUtils.getString(mContext, "test_ss", "默认"));
        return sampleData;
    }
}
