package com.dong.android.ui.listsample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dong.android.R;
import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.base.view.BaseActivity;

import java.util.List;

import butterknife.BindView;

/**
 * 作者：<Dr_dong>
 * 日期：2016/12/11.
 * 描述：
 */

public abstract class ListActivity extends BaseActivity {

    @BindView(R.id.list)
    ListView listView;

    private List<SampleData> sampleData;

    @Override
    protected int getRootView() {
        return R.layout.activity_list;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void setListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            intent.setClassName(getPackageName(), sampleData.get(position).page);
            intent.putExtra("title", sampleData.get(position).title);
            startActivity(intent);
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String title = getIntent().getStringExtra("title");
        if (title != null) {
            setTitle(title);
        }
        sampleData = sampleData();
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, sampleData));
    }

    protected abstract List<SampleData> sampleData();
}
