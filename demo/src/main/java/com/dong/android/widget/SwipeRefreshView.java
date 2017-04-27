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
import com.dong.utils.log.LogUtils;

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
    private int downY = 0;

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
        mFooterView.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, UIUtils.dip2px(160)));

        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        LogUtils.e("===", "===mScaledTouchSlop===" + mScaledTouchSlop);
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (showLoadMore) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    LogUtils.e("===", "===ACTION_MOVE===" + downY);
                    LogUtils.e("===", "===ACTION_MOVE2===" + downY);
                    LogUtils.e("===", "===height===" + mFooterView.getLayoutParams().height);
                    if (downY <= mFooterView.getLayoutParams().height) {
                        mRecyclerView.setPadding(0, 0, 0, downY);
                        mRecyclerView.smoothScrollBy(0, downY);
                        mFooterView.setTranslationY(mFooterView.getLayoutParams().height - downY);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (showLoadMore && mFooterView.getTranslationY() == 0) {
                        loadData();
                        showLoadMore = false;
                    } else {
                        setLoadingClose();
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 处理加载数据的逻辑
     */
    private void loadData() {
        isLoading = true;
        LogUtils.e("===", "加载数据...");
        if (mOnLoadListener != null) {
            mOnLoadListener.onLoad();
        }
    }

    public void setLoadingClose() {
        if (isLoading) {
            LogUtils.e("===", "===setLoadingClose===");
            isLoading = false;
            removeView(mFooterView);
            mRecyclerView.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * 设置RecyclerView的滑动监听
     */
    private void setRecyclerViewOnScroll() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动，既是否向下滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LogUtils.e("===", "===onScrolled===dy===" + dy);
                if (!recyclerView.canScrollVertically(1) && !showLoadMore) {
                    showLoadMore();
                }
                if (showLoadMore) {
                    downY += dy;
                    if (downY > UIUtils.dip2px(160)) {
                        downY = UIUtils.dip2px(160);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void showLoadMore() {
        downY = 0;
        showLoadMore = true;
        removeView(mFooterView);
        LayoutParams layoutParams = (LayoutParams) mFooterView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(mFooterView, layoutParams);
        mFooterView.setTranslationY(mFooterView.getLayoutParams().height);
    }

    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i] > maxVal) maxVal = arr[i];
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
