package com.dissertation.toaok.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BBMSCachesDatabase {

    private BBMSDatabase mDatabase;
    private static BBMSCachesDatabase sInstance=null;

    public static BBMSCachesDatabase getInstance(Context context) {
        synchronized (BBMSCachesDatabase.class) {
            if (sInstance == null) {
                sInstance = new BBMSCachesDatabase(context);
            }
        }
        return sInstance;
    }

    private BBMSCachesDatabase(Context context) {
        mDatabase = BBMSDatabase.getInstance(context);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Caches(key int primary key,value text)");
    }


    public void updateOrAdd(int key, String value) {
        SQLiteDatabase sqLiteDatabase = mDatabase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("value", value);
            /*
            * update方法中参数：
            * 表示更新表Caches，
            * 更新字段和数据由values来确定，
            * "key=?"表示where条件，"?"是占位符，
            * 占位符的值有后面的数组来定，使用数组是考虑到，可能有多个占位符
            * */

        if (sqLiteDatabase.update("Caches", values, "key=?",
                new String[]{String.valueOf(key)}) == 0) {
            values.put("key", key);
            sqLiteDatabase.insert("Caches", null, values);
        }
        sqLiteDatabase.close();
    }


    public String getValue(int key, String defaultValue) {
        SQLiteDatabase sqLiteDatabase = mDatabase.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select value from Caches where key=? ", new String[]{String.valueOf(key)});
        if (cursor.moveToFirst()) {
            defaultValue = cursor.getString(0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return defaultValue;
    }

}