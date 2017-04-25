package com.dong.android.ui.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.android.R;
import com.dong.android.base.view.BaseActivity;
import com.dong.android.ui.common.presenter.CommonPresenter;
import com.dong.android.utils.image.ImageLoader;
import com.dong.android.utils.image.TransformationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GlideLoadImageActivity extends BaseActivity {


    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.list)
    RecyclerView recyclerView;

    @Override
    protected int getRootView() {
        return R.layout.activity_glide_load_image;
    }

    @Override
    protected CommonPresenter createPresenter() {
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    class MainAdapter extends RecyclerView.Adapter<ViewHolder> {

        private Context mContext;
        private List<Integer> mDataSet;

        private String imgUrl = "http://att2.citysbs.com/jiaxing/2013/03/07/22/" +
                "225405_15301362668045387_850bdb0787bbea541e07995f0e15dec2.jpg";

        public MainAdapter(Context context, List<Integer> dataSet) {
            mContext = context;
            mDataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item, parent,
                    false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ImageLoader.display(imgUrl, holder.image, TransformationUtils.getType(mDataSet.get
                    (position)));
            holder.title.setText(position + "");
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

    }
}
