package com.dong.android.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import com.dong.android.R;
import com.dong.utils.UIUtils;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author <dr_dong>
 * @time 2017/4/26 11:36
 */
public class SwipeRefreshView extends RelativeLayout {

    private final int mScaledTouchSlop;
    private final RelativeLayout mFooterView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private OnLoadListener mOnLoadListener;
    /**
     * 正在加载状态
     */
    private boolean isLoading = false;
    private boolean showLoadMore = false;
    private boolean isBottomLoadMore = false;
    private int downY = 0;
    private float y1;
    private boolean refreshY1 = false;

    public SwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充底部加载布局
//        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        refreshLayout = new SwipeRefreshLayout(context);
        refreshLayout.setLayoutParams(new SwipeRefreshLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        removeView(refreshLayout);
        addView(refreshLayout);

        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        refreshLayout.removeView(mRecyclerView);
        refreshLayout.addView(mRecyclerView);
        setRecyclerViewOnScroll();

        mFooterView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.swipe_refresh_load_more, this, false);
        mFooterView.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, UIUtils.dip2px(48)));

        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y1 = event.getY();
            case MotionEvent.ACTION_MOVE:
                if (refreshY1) {
                    refreshY1 = false;
                    y1 = event.getY();
                }
                if (isBottomLoadMore) {
                    initLoadMore(mRecyclerView);
                    downY = (int) (y1 - event.getY());
                    downY = downY < mFooterView.getLayoutParams().height * 2 ? downY : mFooterView.getLayoutParams().height * 2;
                }
                if (showLoadMore && downY <= mFooterView.getLayoutParams().height * 2) {
                    mRecyclerView.setPadding(0, 0, 0, downY / 2);
                    mRecyclerView.smoothScrollBy(0, downY / 2);
                    mFooterView.setTranslationY(mFooterView.getLayoutParams().height - downY / 2);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (showLoadMore) {
                    showLoadMore = false;
                    isBottomLoadMore = false;
                    isLoading = true;
                    if (mFooterView.getTranslationY() < 0.1f) {
                        loadData();
                    } else {
                        setLoadingClose();
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 设置RecyclerView的滑动监听
     */
    private void setRecyclerViewOnScroll() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1) && !showLoadMore && newState == SCROLL_STATE_DRAGGING) {
                    isBottomLoadMore = true;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                initLoadMore(recyclerView);
                if (showLoadMore && !isBottomLoadMore) {
                    isBottomLoadMore = true;
                    refreshY1 = true;
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initLoadMore(RecyclerView recyclerView) {
        if (!recyclerView.canScrollVertically(1) && !showLoadMore) {
            downY = 0;
            showLoadMore = true;
            removeView(mFooterView);
            LayoutParams layoutParams = (LayoutParams) mFooterView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            addView(mFooterView, layoutParams);
            mFooterView.setTranslationY(mFooterView.getLayoutParams().height);
        }
    }

    /**
     * 处理加载数据的逻辑
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            mOnLoadListener.onLoad();
        }
    }

    public void setLoadingClose() {
        if (isLoading) {
            isLoading = false;
            removeView(mFooterView);
            mRecyclerView.setPadding(0, 0, 0, 0);
        }
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
