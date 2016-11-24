package com.dong.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dong.android.utils.LogUtils;
import com.dong.android.utils.UIUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
    }

    private void test() {
        LogUtils.e("test");
        UIUtils.showToast("test");
        UIUtils.showToast(R.string.test_toast);
    }

}
