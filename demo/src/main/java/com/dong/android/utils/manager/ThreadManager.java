package com.dong.android.utils.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <dr_dong>
 * @time 2017/4/1 13:14
 */
public class ThreadManager {

    private static final Object mLock = new Object();
    private static final Object mDownloadLock = new Object();
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2;
    private static volatile ThreadPoolExecutor mPool = null;
    private static volatile ThreadPoolExecutor mLargePool = null;

    /**
     * 获取一个用于执行耗时任务的线程池
     */
    public static ExecutorService getPool() {
        if (mPool == null) {
            synchronized (mLock) {
                if (mPool == null) {
                    mPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(CORE_POOL_SIZE);
                }
            }
        }
        return mPool;
    }

    /**
     * 获取下载线程池
     */
    public static ExecutorService getLargePool() {
        if (mLargePool == null) {
            synchronized (mDownloadLock) {
                if (mLargePool == null) {
                    mLargePool = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
                }
            }
        }
        return mLargePool;
    }


    /**
     * 取消线程池中某个还未执行的任务
     */
    public synchronized void cancel(Runnable run) {
        if (mPool != null && !(mPool.isShutdown() || mPool.isTerminating())) {
            mPool.getQueue().remove(run);
        }
    }

    /**
     * 查看线程池中是否存在特定的任务
     */
    public synchronized boolean contains(Runnable run) {
        return mPool != null && !(mPool.isShutdown() || mPool.isTerminating()) &&
                mPool.getQueue().contains(run);
    }

    /**
     * 平滑关闭线程池
     */
    public void stop() {
        if (mPool != null && !(mPool.isShutdown() || mPool.isTerminating())) {
            mPool.shutdown();
        }
    }
}
