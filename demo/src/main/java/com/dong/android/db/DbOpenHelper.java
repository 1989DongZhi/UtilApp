package com.dong.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "dong";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "dong";

    public static final String ID = "_id";
    public static final String KEY = "key";
    public static final String VALUE = "value";


    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table if not exists " + TABLE_NAME + " ( "
                + ID + " Integer primary key autoincrement,"
                + KEY + " text UNIQUE,"
                + VALUE + " text)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
