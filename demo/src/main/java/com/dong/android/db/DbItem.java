package com.dong.android.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.dong.android.app.AppManager;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述: 数据库存储键值对的封装
 */
public class DbItem {

    private String key;
    private String value;

    public DbItem() {

    }

    public DbItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 数据库增删改查工具类
     */
    public static class DBDao {

        private static DBDao dbDao = new DBDao();

        private DbOpenHelper dbOpenHelper;

        private DBDao() {
            dbOpenHelper = new DbOpenHelper(AppManager.getAppContext());
        }

        public static DBDao getInstance() {
            return dbDao;
        }

        /**
         * 增，用insert向数据库中插入数据
         */
        public void add(@NonNull DbItem item) {
            if (hasItem(item.getKey())) {//如果数据库里面有相同的key就更新数据
                update(item);
                return;
            }
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DbOpenHelper.KEY, item.getKey());
            values.put(DbOpenHelper.VALUE, item.getValue());
            db.insert(DbOpenHelper.TABLE_NAME, null, values);
            db.close();
        }

        /**
         * 删，通过jsonKey删除数据
         */
        public void delete(@NonNull String key) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            db.delete(DbOpenHelper.TABLE_NAME, DbOpenHelper.KEY + "=?", new String[]{key});
            db.close();
        }

        /**
         * 改，修改指定id的数据
         */
        public void update(@NonNull DbItem item) {
            if (!hasItem(item.getKey())) {//如果数据库里面没有相同的key就添加数据
                add(item);
                return;
            }
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DbOpenHelper.KEY, item.getKey());
            values.put(DbOpenHelper.VALUE, item.getValue());
            db.update(DbOpenHelper.TABLE_NAME, values, DbOpenHelper.KEY + "=?", new String[]{item
                    .getKey()});
            db.close();
        }


        /**
         * 查询指定id的数据
         */
        public DbItem query(@NonNull String key) {
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            Cursor cursor = db.query(DbOpenHelper.TABLE_NAME, null, DbOpenHelper.KEY + "=?", new
                    String[]{key}, null, null, null);
            DbItem dbItem = null;
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                dbItem = new DbItem();
                String jsonKeys = cursor.getString(cursor.getColumnIndex(DbOpenHelper.KEY));
                String jsonValue = cursor.getString(cursor.getColumnIndex(DbOpenHelper.VALUE));
                dbItem.setKey(jsonKeys);
                dbItem.setValue(jsonValue);
            }
            cursor.close();
            db.close();
            return dbItem;
        }

        /**
         * 检查是否存在JsonKey
         */
        public boolean hasItem(@NonNull String key) {
            boolean exist = false;
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            Cursor cursor = db.query(DbOpenHelper.TABLE_NAME, null, DbOpenHelper.KEY + "=?",
                    new String[]{key}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                exist = true;
            }
            cursor.close();
            db.close();
            return exist;
        }
    }
}
