package com.dong.recycler;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dong.recycler.listener.OnItemChildClickListener;
import com.dong.recycler.listener.OnItemChildLongClickListener;
import com.dong.recycler.listener.OnItemClickListener;
import com.dong.recycler.listener.OnItemLongClickListener;
import com.dong.recycler.listener.RequestLoadMoreListener;
import com.dong.recycler.manage.AnimationManage;
import com.dong.recycler.manage.ItemTypeManage;
import com.dong.recycler.view.LoadMoreView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author <dr_dong>
 * @time 2017/4/10 14:13
 */
public abstract class BaseRecyclerAdapter<T extends ItemTypeEntity, H extends BaseViewHolder> extends RecyclerView.Adapter<H> {

    public static final int HEADER_VIEW = 0x00000111;
    public static final int LOADING_VIEW = 0x00000222;
    public static final int FOOTER_VIEW = 0x00000333;
    public static final int EMPTY_VIEW = 0x00000555;
    private static final String TAG = BaseRecyclerAdapter.class.getSimpleName();
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected int mLayoutResId;
    protected List<T> mData;
    private LoadMoreView mLoadMoreView;
    private RecyclerView mRecyclerView;
    private ItemTypeManage<T> mItemTypeManage;

    //load more
    private int mAutoLoadMoreSize = 1;
    private boolean mNextLoadEnable = false;
    private boolean mLoadMoreEnable = false;
    private boolean mLoading = false;
    private boolean mFirstOnlyEnable = true;
    //animation
    private int mDuration = 300;
    private boolean mOpenAnimationEnable = false;
    private AnimationManage mAnimationManage;
    private int mLastPosition = -1;

    //header footer empty
    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    private FrameLayout mEmptyLayout;
    private boolean mIsUseEmpty = true;
    private boolean mHeadAndEmptyEnable;
    private boolean mFootAndEmptyEnable;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemChildLongClickListener mOnItemChildLongClickListener;
    private RequestLoadMoreListener mRequestLoadMoreListener;
    private SpanSizeLookup mSpanSizeLookup;


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public BaseRecyclerAdapter(@LayoutRes int layoutResId, List<T> data, @NonNull RecyclerView recyclerView) {
        this.mData = data == null ? new ArrayList<T>() : data;
        mRecyclerView = recyclerView;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        H baseViewHolder;
        this.mContext = parent.getContext();
        this.mLayoutInflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case HEADER_VIEW:
                baseViewHolder = createBaseViewHolder(mHeaderLayout);
                break;
            case EMPTY_VIEW:
                baseViewHolder = createBaseViewHolder(mEmptyLayout);
                break;
            case FOOTER_VIEW:
                baseViewHolder = createBaseViewHolder(mFooterLayout);
                break;
            case LOADING_VIEW:
                baseViewHolder = getLoadingView(parent);
                break;
            default:
                baseViewHolder = onCreateDefViewHolder(parent, viewType);
                bindViewClickListener(baseViewHolder);
        }
        return (H) baseViewHolder.setAdapter(this);

    }

    /**
     * if you want to use subclass of BaseViewHolder in the adapter,
     * you must override the method to create new ViewHolder.
     *
     * @param view view
     * @return new ViewHolder
     */
    protected H createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericHClass(temp);
            temp = temp.getSuperclass();
        }
        H k = createGenericHInstance(z, view);
        return null != k ? k : (H) new BaseViewHolder(view);
    }

    /**
     * get generic parameter H
     *
     * @param z
     * @return
     */
    private Class getInstancedGenericHClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                }
            }
        }
        return null;
    }

    /**
     * try to create Generic H instance
     *
     * @param z
     * @param view
     * @return
     */
    private H createGenericHInstance(Class z, View view) {
        try {
            Constructor constructor;
            String buffer = Modifier.toString(z.getModifiers());
            String className = z.getName();
            // inner and unstatic class
            if (className.contains("$") && !buffer.contains("static")) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                return (H) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                return (H) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private H getLoadingView(ViewGroup parent) {
        View view = getItemView(mLoadMoreView.getLayoutId(), parent);
        H holder = createBaseViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
                    mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
                    notifyItemChanged(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount());
                }
            }
        });
        return holder;
    }

    protected H onCreateDefViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mLayoutResId;
        if (mItemTypeManage != null) {
            layoutId = mItemTypeManage.getLayoutId(viewType);
        }
        return createBaseViewHolder(parent, layoutId);
    }

    protected H createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return createBaseViewHolder(getItemView(layoutResId, parent));
    }

    private void bindViewClickListener(final H baseViewHolder) {
        if (baseViewHolder == null || baseViewHolder.getConvertView() == null) {
            return;
        }
        final View view = baseViewHolder.getConvertView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnItemClickListener() != null && baseViewHolder != null) {
                    getOnItemClickListener().onItemClick(BaseRecyclerAdapter.this, v, baseViewHolder.getLayoutPosition() - getHeaderLayoutCount());
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (getOnItemLongClickListener() != null && baseViewHolder != null) {
                    return getOnItemLongClickListener().onItemLongClick(BaseRecyclerAdapter.this, v, baseViewHolder.getLayoutPosition() - getHeaderLayoutCount());
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        int count;
        if (getEmptyViewCount() == 1) {
            count = 1;
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                count++;
            }
            if (mFootAndEmptyEnable && getFooterLayoutCount() != 0) {
                count++;
            }
        } else {
            count = getHeaderLayoutCount() + mData.size() + getFooterLayoutCount() + getLoadMoreViewCount();
        }
        return count;
    }

    /**
     * if show empty view will be return 1 or not will be return 0
     *
     * @return
     */
    public int getEmptyViewCount() {
        if (mEmptyLayout == null || mEmptyLayout.getChildCount() == 0) {
            return 0;
        }
        if (!mIsUseEmpty) {
            return 0;
        }
        if (mData.size() != 0) {
            return 0;
        }
        return 1;
    }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    public int getHeaderLayoutCount() {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * if addFooterView will be return 1, if not will be return 0
     */
    public int getFooterLayoutCount() {
        if (mFooterLayout == null || mFooterLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * Load more view count
     *
     * @return 0 or 1
     */
    public int getLoadMoreViewCount() {
        if (mRequestLoadMoreListener == null || !mLoadMoreEnable) {
            return 0;
        }
        if (!mNextLoadEnable && mLoadMoreView.isLoadEndMoreGone()) {
            return 0;
        }
        if (mData.isEmpty()) {
            return 0;
        }
        return 1;
    }

    /**
     * To bind different types of holder and solve different the bind events
     *
     * @param holder
     * @param positions
     * @see #getDefItemViewType(int)
     */
    @Override
    public void onBindViewHolder(H holder, int positions) {
        switch (holder.getItemViewType()) {
            case 0:
                convert(holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
                break;
            case LOADING_VIEW:
                mLoadMoreView.convert(holder);
                break;
            case HEADER_VIEW:
                break;
            case EMPTY_VIEW:
                break;
            case FOOTER_VIEW:
                break;
            default:
                convert(holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
                break;
        }
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * simple to solve item will layout using all
     * {@link #setFullSpan(H)}
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(H holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW) {
            setFullSpan(holder);
        } else {
            addAnimation(holder);
        }
    }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected void setFullSpan(H holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder
                    .itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private void addAnimation(H holder) {
        if (mOpenAnimationEnable && (!mFirstOnlyEnable || holder.getLayoutPosition() > mLastPosition)) {
            for (Animator anim : mAnimationManage.getAnimators(holder.itemView)) {
                anim.setDuration(mDuration).start();
                anim.setInterpolator(new LinearInterpolator());
            }
            mLastPosition = holder.getLayoutPosition();
        }
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW) {
                        return gridManager.getSpanCount();
                    } else {
                        return mSpanSizeLookup == null ? 0 :
                                mSpanSizeLookup.getSpanSize(gridManager, position - getHeaderLayoutCount());
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getEmptyViewCount() == 1) {
            boolean header = mHeadAndEmptyEnable && getHeaderLayoutCount() != 0;
            switch (position) {
                case 0:
                    return header ? HEADER_VIEW : EMPTY_VIEW;
                case 1:
                    return header ? EMPTY_VIEW : FOOTER_VIEW;
                case 2:
                    return FOOTER_VIEW;
                default:
                    return EMPTY_VIEW;
            }
        }
        autoLoadMore(position);
        int numHeaders = getHeaderLayoutCount();
        if (position < numHeaders) {
            return HEADER_VIEW;
        } else {
            int adjPosition = position - numHeaders;
            int adapterCount = mData.size();
            if (adjPosition < adapterCount) {
                return getDefItemViewType(adjPosition);
            } else {
                adjPosition = adjPosition - adapterCount;
                int numFooters = getFooterLayoutCount();
                if (adjPosition < numFooters) {
                    return FOOTER_VIEW;
                } else {
                    return LOADING_VIEW;
                }
            }
        }
    }

    private void autoLoadMore(int position) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        if (position < getItemCount() - mAutoLoadMoreSize) {
            return;
        }
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_DEFAULT) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
        if (!mLoading) {
            mLoading = true;
            if (getRecyclerView() != null) {
                getRecyclerView().post(new Runnable() {
                    @Override
                    public void run() {
                        mRequestLoadMoreListener.onLoadMoreRequested();
                    }
                });
            } else {
                mRequestLoadMoreListener.onLoadMoreRequested();
            }
        }
    }

    protected RecyclerView getRecyclerView() {
        if (mRecyclerView == null) {
            throw new RuntimeException("please bind recyclerView first!");
        }
        return mRecyclerView;
    }

    protected int getDefItemViewType(int position) {
        if (mItemTypeManage != null) {
            Object item = mData.get(position);
            if (item instanceof ItemTypeEntity) {
                return ((ItemTypeEntity) item).getItemType();
            }
            return super.getItemViewType(position);
        }
        return super.getItemViewType(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    //**************** data ********************

    /**
     * setting up a new instance to data;
     *
     * @param data
     */
    public void setNewData(List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        if (mRequestLoadMoreListener != null) {
            mNextLoadEnable = true;
            mLoadMoreEnable = true;
            mLoading = false;
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        }
        mLastPosition = -1;
        if (getHeaderLayoutCount() == 1 && data != null) {
            notifyItemRangeChanged(getHeaderLayoutCount(), data.size());
        } else {
            notifyDataSetChanged();
        }
    }

    /**
     * add one new data in to certain location
     *
     * @param position
     */
    public void addData(int position, T data) {
        mData.add(position, data);
        notifyItemInserted(position + getHeaderLayoutCount());
        compatibilityDataSizeChanged(1);
    }

    /**
     * add one new data
     */
    public void addData(T data) {
        mData.add(data);
        notifyItemInserted(mData.size() + getHeaderLayoutCount());
        compatibilityDataSizeChanged(1);
    }

    /**
     * add new data in to certain location
     *
     * @param position
     */
    public void addData(int position, List<T> data) {
        mData.addAll(position, data);
        notifyItemRangeInserted(position + getHeaderLayoutCount(), data.size());
        compatibilityDataSizeChanged(data.size());
    }

    /**
     * additional data;
     *
     * @param newData
     */
    public void addData(List<T> newData) {
        this.mData.addAll(newData);
        notifyItemRangeInserted(mData.size() - newData.size() + getHeaderLayoutCount(), newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    public void remove(int position) {
        mData.remove(position);
        int internalPosition = position + getHeaderLayoutCount();
        notifyItemRemoved(internalPosition);
        compatibilityDataSizeChanged(0);
        notifyItemRangeChanged(internalPosition, mData.size() - internalPosition);
    }

    /**
     * change data
     */
    public void setData(int index, T data) {
        mData.set(index, data);
        notifyItemChanged(index + getHeaderLayoutCount());
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = mData == null ? 0 : mData.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    /**
     * Get the data of list
     *
     * @return
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    public T getItem(int position) {
        if (position != -1)
            return mData.get(position);
        else
            return null;
    }

    /**
     * @param layoutResId ID for an XML layout resource to load
     * @param parent      Optional view to be the parent of the generated hierarchy or else simply an object that
     *                    provides a set of LayoutParams values for root of the returned
     *                    hierarchy
     * @return view will be return
     */
    protected View getItemView(int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }

    public View getViewByPosition(int position, int viewId) {
        return getViewByPosition(getRecyclerView(), position, viewId);
    }

    public View getViewByPosition(RecyclerView recyclerView, int position, int viewId) {
        BaseViewHolder viewHolder = (BaseViewHolder) recyclerView.findViewHolderForLayoutPosition(position);
        return viewHolder.getView(viewId);
    }

    public ItemTypeManage<T> getItemTypeManage() {
        return mItemTypeManage;
    }

    public void setItemTypeManage(ItemTypeManage<T> itemTypeManage) {
        mItemTypeManage = itemTypeManage;
    }

    //******************* head **************************
    public LinearLayout getLayout(boolean flag) {
        return flag ? mHeaderLayout : mFooterLayout;
    }

    public int addView(View view, boolean flag) {
        return addView(view, -1, flag);
    }

    public int addView(View view, int index, boolean flag) {
        return addView(view, index, LinearLayout.VERTICAL, flag);
    }

    public int addView(View view, int index, int orientation, boolean flag) {
        int result;
        LinearLayout layout = getLayout(flag);
        if (layout == null) {
            layout = new LinearLayout(view.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = layout.getChildCount();
        result = (index < 0 || index > childCount) ? childCount : index;
        layout.addView(view, result);
        if (layout.getChildCount() == 1) {
            int position = flag ? getHeaderViewPosition() : getFooterViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return result;
    }

    public int setView(View view, boolean flag) {
        return setView(view, 0, LinearLayout.VERTICAL, flag);
    }

    public int setView(View view, int index, boolean flag) {
        return setView(view, index, LinearLayout.VERTICAL, flag);
    }

    public int setView(View view, int index, int orientation, boolean flag) {
        LinearLayout layout = getLayout(flag);
        if (layout == null || layout.getChildCount() <= index) {
            return addView(view, index, orientation, flag);
        } else {
            layout.removeViewAt(index);
            layout.addView(view, index);
            return index;
        }
    }

    public void removeHeaderView(View header) {
        if (getHeaderLayoutCount() != 0) {
            mHeaderLayout.removeView(header);
            if (mHeaderLayout.getChildCount() == 0) {
                int position = getHeaderViewPosition();
                if (position != -1) {
                    notifyItemRemoved(position);
                }
            }
        }
    }

    public void removeAllHeaderView() {
        if (getHeaderLayoutCount() != 0) {
            mHeaderLayout.removeAllViews();
            int position = getHeaderViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    private int getHeaderViewPosition() {
        //Return to header view notify position
        if (getEmptyViewCount() == 1) {
            if (mHeadAndEmptyEnable) {
                return 0;
            }
        } else {
            return 0;
        }
        return -1;
    }

    //******************* foot **************************

    public void removeFooterView(View footer) {
        if (getFooterLayoutCount() != 0) {
            mFooterLayout.removeView(footer);
            if (mFooterLayout.getChildCount() == 0) {
                int position = getFooterViewPosition();
                if (position != -1) {
                    notifyItemRemoved(position);
                }
            }
        }
    }

    public void removeAllFooterView() {
        if (getFooterLayoutCount() != 0) {
            mFooterLayout.removeAllViews();
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    private int getFooterViewPosition() {
        //Return to footer view notify position
        if (getEmptyViewCount() == 1) {
            int position = 1;
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                position++;
            }
            if (mFootAndEmptyEnable) {
                return position;
            }
        } else {
            return getHeaderLayoutCount() + mData.size();
        }
        return -1;
    }

    //******************* empty **************************

    public void setEmptyView(int layoutResId, ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
        setEmptyView(view);
    }

    public void setEmptyView(int layoutResId) {
        setEmptyView(layoutResId, getRecyclerView());
    }

    public void setHeaderAndEmpty(boolean isHeadAndEmpty) {
        setHeaderFooterEmpty(isHeadAndEmpty, false);
    }

    public void setHeaderFooterEmpty(boolean isHeadAndEmpty, boolean isFootAndEmpty) {
        mHeadAndEmptyEnable = isHeadAndEmpty;
        mFootAndEmptyEnable = isFootAndEmpty;
    }

    public void isUseEmpty(boolean isUseEmpty) {
        mIsUseEmpty = isUseEmpty;
    }

    public View getEmptyView() {
        return mEmptyLayout;
    }

    public void setEmptyView(View emptyView) {
        boolean insert = false;
        if (mEmptyLayout == null) {
            mEmptyLayout = new FrameLayout(emptyView.getContext());
            final RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mEmptyLayout.setLayoutParams(layoutParams);
            insert = true;
        }
        mEmptyLayout.removeAllViews();
        mEmptyLayout.addView(emptyView);
        mIsUseEmpty = true;
        if (insert && getEmptyViewCount() == 1) {
            int position = 0;
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                position++;
            }
            notifyItemInserted(position);
        }
    }


    //************************ LoadMore *********************

    public void setLoadMoreView(LoadMoreView loadingView) {
        this.mLoadMoreView = loadingView;
    }

    public void setAutoLoadMoreSize(int autoLoadMoreSize) {
        if (autoLoadMoreSize > 1) {
            mAutoLoadMoreSize = autoLoadMoreSize;
        }
    }

    /**
     * @return Whether the Adapter is actively showing load
     * progress.
     */
    public boolean isLoading() {
        return mLoading;
    }

    /**
     * Refresh end, no more data
     */
    public void loadMoreEnd() {
        loadMoreEnd(false);
    }

    /**
     * Refresh end, no more data
     *
     * @param gone if true gone the load more view
     */
    public void loadMoreEnd(boolean gone) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mNextLoadEnable = false;
        mLoadMoreView.setLoadMoreEndGone(gone);
        if (gone) {
            notifyItemRemoved(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount());
        } else {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
            notifyItemChanged(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount());
        }
    }

    /**
     * Refresh complete
     */
    public void loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount());
    }

    /**
     * Refresh failed
     */
    public void loadMoreFail() {
        if (getLoadMoreViewCount() != 0) {
            mLoading = false;
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
            notifyItemChanged(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount());
        }
    }

    /**
     * Set the enabled state of load more.
     *
     * @param enable True if load more is enabled, false otherwise.
     */
    public void setEnableLoadMore(boolean enable) {
        int oldLoadMoreCount = getLoadMoreViewCount();
        mLoadMoreEnable = enable;
        int newLoadMoreCount = getLoadMoreViewCount();

        if (oldLoadMoreCount == 1 && newLoadMoreCount == 0) {
            notifyItemRemoved(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount());
        } else if (oldLoadMoreCount == 0 && newLoadMoreCount == 1) {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
            notifyItemInserted(getHeaderLayoutCount() + mData.size() + getFooterLayoutCount());
        }
    }

    /**
     * Returns the enabled status for load more.
     *
     * @return True if load more is enabled, false otherwise.
     */
    public boolean isLoadMoreEnable() {
        return mLoadMoreEnable;
    }


//************************ Animation *********************

    /**
     * Sets the duration of the animation.
     *
     * @param duration The length of the animation, in milliseconds.
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void openLoadAnimation(@AnimationManage.AnimationType int animationType) {
        openLoadAnimation();
        mAnimationManage.setAnimationType(animationType);
    }

    public void openLoadAnimation(Animator[] animators) {
        openLoadAnimation();
        mAnimationManage.setCustomAnimator(animators);
    }

    /**
     * To open the animation when loading
     */
    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
        if (mAnimationManage == null) {
            mAnimationManage = new AnimationManage();
        }
    }

    /**
     * {@link #addAnimation(H)}
     *
     * @param firstOnly true just show anim when first loading false show anim when load the data every time
     */
    public void isFirstOnly(boolean firstOnly) {
        this.mFirstOnlyEnable = firstOnly;
    }

    public void setNotDoAnimationCount(int count) {
        mLastPosition = count;
    }

//*********************************************

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void convert(H helper, T item);

    private void setRequestLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
        mNextLoadEnable = true;
        mLoadMoreEnable = true;
        mLoading = false;
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public final OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public final OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
    }

    public final OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return mOnItemChildLongClickListener;
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener listener) {
        mOnItemChildLongClickListener = listener;
    }

    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

}
