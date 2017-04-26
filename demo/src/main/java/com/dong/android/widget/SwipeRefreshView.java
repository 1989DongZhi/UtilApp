package com.dong.android.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dong.android.R;
import com.dong.utils.UIUtils;
import com.dong.utils.log.LogUtils;

/**
 * @author <dr_dong>
 * @time 2017/4/26 11:36
 */
public class SwipeRefreshView extends RelativeLayout {

    private final int mScaledTouchSlop;
    private final View mFooterView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private OnLoadListener mOnLoadListener;
    /**
     * 正在加载状态
     */
    private boolean isLoading = false;

    public SwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充底部加载布局
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        refreshLayout = new SwipeRefreshLayout(context);
        refreshLayout.setLayoutParams(new SwipeRefreshLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        removeView(refreshLayout);
        addView(refreshLayout);

        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        refreshLayout.removeView(mRecyclerView);
        refreshLayout.addView(mRecyclerView);
        setRecyclerViewOnScroll();

        mFooterView = LayoutInflater.from(context).inflate(R.layout.swipe_refresh_load_more, this, false);
        mFooterView.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(48)));

        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        LogUtils.e("====" + mScaledTouchSlop);
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 处理加载数据的逻辑
     */
    private void loadData() {
        isLoading = true;
        removeView(mFooterView);
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(48));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(mFooterView, layoutParams);
        LogUtils.e("===", "===" + mFooterView.getLayoutParams().height);
        mRecyclerView.setPadding(0, 0, 0, mFooterView.getLayoutParams().height);
        LogUtils.e("加载数据...");
        if (mOnLoadListener != null) {
            mOnLoadListener.onLoad();
        }
    }

    public void setLoadingClose() {
        isLoading = false;
        removeView(mFooterView);
        mRecyclerView.setPadding(0, 0, 0, 0);
    }

    /**
     * 设置RecyclerView的滑动监听
     */
    private void setRecyclerViewOnScroll() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动，既是否向下滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int[] lastVisiblePositions = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);
                    int lastVisiblePos = getMaxElem(lastVisiblePositions);
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部
                    if (lastVisiblePos == (totalItemCount - 1) && isSlidingToLast && !isLoading) {
                        //加载更多功能的代码
                        UIUtils.showToast("加载更多");
                        loadData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if (dy > 0) {
                    //大于0表示，正在向下滚动
                    isSlidingToLast = true;
                } else {
                    //小于等于0 表示停止或向上滚动
                    isSlidingToLast = false;
                }

            }
        });
    }

    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i] > maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.mOnLoadListener = listener;
    }

    /**
     * 上拉加载的接口回调
     */

    public interface OnLoadListener {
        void onLoad();
    }
}
