package com.dong.android.base.model;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public abstract class BaseModel<E> {

    protected E mEngine;

    public BaseModel() {
        mEngine = createEngine();
    }

    /**
     * 获取MODEL
     *
     * @return
     */
    protected abstract E createEngine();


    /**
     * 获取负责远程(网络)数据加载的 Engine
     *
     * @return
     */
    public E getEngine() {
        return mEngine;
    }

    /**
     * 获取负责本地(数据库，文件)数据加载的 Dao
     *
     * @return
     */
    public LocalEngine getLocalEngine() {
        return LocalEngineImpl.getInstance();
    }

}
