package com.dong.recycler.listener;

import android.view.View;

import com.dong.recycler.BaseRecyclerAdapter;

/**
 * @author <dr_dong>
 * @time 2017/4/11 9:29
 * Interface definition for a callback to be invoked when an itemchild in this
 * view has been clicked
 */
public interface OnItemChildClickListener {
    /**
     * callback method to be invoked when an item in this view has been
     * click and held
     *
     * @param view     The view whihin the ItemView that was clicked
     * @param position The position of the view int the adapter
     */
    void onItemChildClick(BaseRecyclerAdapter adapter, View view, int position);
}