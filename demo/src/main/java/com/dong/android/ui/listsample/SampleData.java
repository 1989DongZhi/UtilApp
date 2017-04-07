package com.dong.android.ui.listsample;

/**
 * 作者：<Dr_dong>
 * 日期：2016/12/11.
 * 描述：
 */

public class SampleData {

    public String title;
    public String page;

    public SampleData(String title, String page) {
        this.title = title;
        this.page = page;
    }

    @Override
    public String toString() {
        return title;
    }
}
