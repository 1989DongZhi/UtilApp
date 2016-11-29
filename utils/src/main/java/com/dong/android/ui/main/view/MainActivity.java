package com.dong.android.ui.main.view;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.dong.android.R;
import com.dong.android.base.view.BaseActivity;
import com.dong.android.ui.main.presenter.MainPresenter;
import com.dong.android.utils.image.TransformationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {


    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.list)
    RecyclerView recyclerView;

    @Override
    protected int getRootView() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter createPresenter() {
        return null;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        test();
    }

    private void test() {
//        LogUtils.e("test");
//        UIUtils.showToast("test");
//        UIUtils.showToast(R.string.test_toast);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<Integer> dataSet = new ArrayList<>();
        dataSet.add(TransformationUtils.Mask);
        dataSet.add(TransformationUtils.NinePatchMask);
        dataSet.add(TransformationUtils.CropTop);
        dataSet.add(TransformationUtils.CropCenter);
        dataSet.add(TransformationUtils.CropBottom);
        dataSet.add(TransformationUtils.CropSquare);
        dataSet.add(TransformationUtils.CropCircle);
        dataSet.add(TransformationUtils.ColorFilter);
        dataSet.add(TransformationUtils.Grayscale);
        dataSet.add(TransformationUtils.RoundedCorners);
        dataSet.add(TransformationUtils.Blur);
        dataSet.add(TransformationUtils.Toon);
        dataSet.add(TransformationUtils.Sepia);
        dataSet.add(TransformationUtils.Contrast);
        dataSet.add(TransformationUtils.Invert);
        dataSet.add(TransformationUtils.Pixel);
        dataSet.add(TransformationUtils.Sketch);
        dataSet.add(TransformationUtils.Swirl);
        dataSet.add(TransformationUtils.Brightness);
        dataSet.add(TransformationUtils.Kuawahara);
        dataSet.add(TransformationUtils.Vignette);
        recyclerView.setAdapter(new MainAdapter(this, dataSet));
    }

}
