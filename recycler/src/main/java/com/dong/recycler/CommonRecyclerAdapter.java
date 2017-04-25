package com.dong.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dong.recycler.manage.ItemTypeManage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <dr_dong>
 * @time 2017/4/19 11:45
 */
public abstract class CommonRecyclerAdapter<T extends ItemTypeEntity>
        extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = CommonRecyclerAdapter.class.getSimpleName();
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mData;
    protected ItemTypeManage<T> itemTypeManage;
    private SparseArray<Integer> layouts;

    public CommonRecyclerAdapter(@NonNull ItemTypeManage<T> itemTypeManage,
                                 @NonNull List<T> data,
                                 @NonNull RecyclerView recyclerView) {
        this.itemTypeManage = itemTypeManage;
        this.mData = data == null ? new ArrayList<T>() : data;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder;
        this.mContext = parent.getContext();
        this.mLayoutInflater = LayoutInflater.from(mContext);
        return new BaseViewHolder(mLayoutInflater.inflate(itemTypeManage.getLayoutId(viewType), parent, false));
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        convert(holder, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemTypeManage != null) {
            return itemTypeManage.getDefItemViewType(mData, position);
        } else {
            return 0;
        }
    }

    protected abstract BaseViewHolder convert(BaseViewHolder holder, T item);

}
