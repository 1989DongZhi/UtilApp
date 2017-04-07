package com.dong.android.base.model;

import com.dong.android.db.DbItem;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public interface LocalEngine {

    /**
     * 保存 String数据 到 缓存中
     */
    void put(String key, String value);

    /**
     * 保存 String数据 到 缓存中
     */
    void put(String key, String value, int saveTime);

    /**
     * 保存 JSONObject数据 到 缓存中
     */
    void put(String key, JSONObject value);

    /**
     * 保存 JSONObject数据 到 缓存中
     */
    void put(String key, JSONObject value, int saveTime);

    /**
     * 保存 Serializable数据 到 缓存中
     */
    void put(String key, Serializable value);

    /**
     * 保存 Serializable数据到 缓存中
     */
    void put(String key, Serializable value, int saveTime);

    /**
     * 插入数据
     */
    void add(DbItem item);

    /**
     * 删除数据
     */
    void delete(String key);

    /**
     * 更新数据
     */
    void update(DbItem item);

    /**
     * 查询数据
     */
    DbItem query(String key);

    /**
     * 检查本地数据是否存在特定key的数据
     */
    boolean hasItem(String key);

}
