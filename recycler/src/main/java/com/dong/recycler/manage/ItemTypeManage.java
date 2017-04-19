package com.dong.recycler.manage;

import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;

import java.util.List;

/**
 * @author <dr_dong>
 * @time 2017/4/10 15:25
 */
public abstract class ItemTypeManage<T> {
    private static final int DEFAULT_VIEW_TYPE = -0xff;
    private SparseIntArray layouts;
    private boolean autoMode, selfMode;

    public ItemTypeManage(SparseIntArray layouts) {
        this.layouts = layouts;
    }

    public ItemTypeManage() {
    }

    public final int getDefItemViewType(List<T> data, int position) {
        T item = data.get(position);
        return item != null ? getItemType(item) : DEFAULT_VIEW_TYPE;
    }

    /**
     * get the item type from specific entity.
     *
     * @param t entity
     * @return item type
     */
    protected abstract int getItemType(T t);

    public final int getLayoutId(int type) {
        return this.layouts.get(type);
    }

    private void addItemType(int type, @LayoutRes int layoutResId) {
        if (this.layouts == null) {
            this.layouts = new SparseIntArray();
        }
        this.layouts.put(type, layoutResId);
    }

    /**
     * auto increase type vale, start from 0.
     *
     * @param layoutResIds layout id arrays
     * @return MultiTypeDelegate
     */
    public ItemTypeManage registerItemTypeAutoIncrease(@LayoutRes int... layoutResIds) {
        autoMode = true;
        checkMode(selfMode);
        for (int i = 0; i < layoutResIds.length; i++) {
            addItemType(i, layoutResIds[i]);
        }
        return this;
    }

    /**
     * set your own type one by one.
     *
     * @param type        type value
     * @param layoutResId layout id
     * @return MultiTypeDelegate
     */
    public ItemTypeManage registerItemType(int type, @LayoutRes int layoutResId) {
        selfMode = true;
        checkMode(autoMode);
        addItemType(type, layoutResId);
        return this;
    }

    private void checkMode(boolean mode) {
        if (mode) {
            throw new RuntimeException("Don't mess two register mode");
        }
    }

}
