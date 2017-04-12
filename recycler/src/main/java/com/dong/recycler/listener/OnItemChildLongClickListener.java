package com.dong.recycler.listener;

import android.view.View;

import com.dong.recycler.BaseRecyclerAdapter;

/**
 * @author <dr_dong>
 * @time 2017/4/11 9:28
 * Interface definition for a callback to be invoked when an childView in this
 * view has been clicked and held.
 */
public interface OnItemChildLongClickListener {
    /**
     * callback method to be invoked when an item in this view has been
     * click and held
     *
     * @param view     The childView whihin the itemView that was clicked and held.
     * @param position The position of the view int the adapter
     * @return true if the callback consumed the long click ,false otherwise
     */
    boolean onItemChildLongClick(BaseRecyclerAdapter adapter, View view, int position);
}
