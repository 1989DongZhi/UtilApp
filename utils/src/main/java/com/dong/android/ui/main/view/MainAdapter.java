package com.dong.android.ui.main.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.android.R;
import com.dong.android.utils.image.ImageLoader;
import com.dong.android.utils.image.TransformationUtils;

import java.util.List;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/27.
 * 描述：
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context mContext;
    private List<Integer> mDataSet;

    private String imgUrl = "http://att2.citysbs.com/jiaxing/2013/03/07/22/" +
            "225405_15301362668045387_850bdb0787bbea541e07995f0e15dec2.jpg";

    public MainAdapter(Context context, List<Integer> dataSet) {
        mContext = context;
        mDataSet = dataSet;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
        ImageLoader.display(imgUrl, holder.image, TransformationUtils.getType(mDataSet.get
                (position)));
        holder.title.setText(position + "");
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
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
}