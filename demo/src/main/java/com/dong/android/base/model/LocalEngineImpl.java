package com.dong.android.base.model;

import com.dong.android.app.AppManager;
import com.dong.android.db.DbItem;
import com.dong.android.utils.cache.ACache;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public class LocalEngineImpl implements LocalEngine {
    private static LocalEngineImpl localEngine = new LocalEngineImpl();
    private DbItem.DBDao dbDao;
    private ACache aCache;

    private LocalEngineImpl() {
        dbDao = dbDao.getInstance();
        aCache = ACache.get(AppManager.getAppContext());
    }

    /**
     * 获取BaseDao实例
     *
     * @return
     */
    public static LocalEngineImpl getInstance() {
        return localEngine;
    }

    @Override
    public void put(String key, String value) {
        aCache.put(key, value);
    }

    @Override
    public void put(String key, String value, int saveTime) {
        aCache.put(key, value, saveTime);
    }

    @Override
    public void put(String key, JSONObject value) {
        aCache.put(key, value);
    }

    @Override
    public void put(String key, JSONObject value, int saveTime) {
        aCache.put(key, value, saveTime);
    }

    @Override
    public void put(String key, Serializable value) {
        aCache.put(key, value);
    }

    @Override
    public void put(String key, Serializable value, int saveTime) {
        aCache.put(key, value, saveTime);
    }

    /**
     * 插入数据
     *
     * @param item
     */
    @Override
    public void add(DbItem item) {
        dbDao.add(item);
    }

    /**
     * /**
     * 删除数据
     *
     * @param key
     */
    @Override
    public void delete(String key) {
        dbDao.delete(key);
    }

    /**
     * 更新数据
     *
     * @param item
     */
    @Override
    public void update(DbItem item) {
        dbDao.update(item);
    }

    /**
     * 查询数据
     *
     * @param key
     * @return
     */
    @Override
    public DbItem query(String key) {
        return dbDao.query(key);
    }

    /**
     * 检查本地数据是否存在特定key的数据
     *
     * @param key
     * @return
     */
    @Override
    public boolean hasItem(String key) {
        return dbDao.hasItem(key);
    }
}
