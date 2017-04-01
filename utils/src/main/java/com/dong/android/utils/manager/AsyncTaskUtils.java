package com.dong.android.utils.manager;

import com.dong.android.base.view.BaseView;
import com.dong.android.utils.UIUtils;

/**
 * @author <dr_dong>
 * @time 2017/4/1 13:13
 */
public abstract class AsyncTaskUtils<T> {

    private T t;
    private BaseView baseView;

    public AsyncTaskUtils() {
        this(null);
    }

    /**
     * 自动处理ProgressBar
     *
     * @param baseView
     */
    public AsyncTaskUtils(BaseView baseView) {
        this.baseView = baseView;
    }

    public void exec() {
        if (null != baseView) baseView.showLoading();
        TaskRunnable backgroundTask = new TaskRunnable();
        ThreadManager.getPool().execute(backgroundTask);
    }

    /**
     * 后台线程池执行
     *
     * @return
     */
    protected abstract T runInBackground();

    /**
     * 后台任务执行完毕后到UI线程执行
     *
     * @param t
     */

    protected abstract void onPostExecute(T t);

    private class TaskRunnable implements Runnable {

        @Override
        public void run() {
            t = runInBackground();
            UIUtils.post(() -> {
                onPostExecute(t);
                if (null != baseView) baseView.dismissLoading();
            });
        }
    }
}