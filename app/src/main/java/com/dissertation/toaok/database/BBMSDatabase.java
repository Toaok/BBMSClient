package com.dissertation.toaok.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TOAOK on 2017/10/30.
 */

public class BBMSDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bbms.db";
    private static final int VERSION = 1;
    private static BBMSDatabase sInstance = null;
    private Context mContext;

    public static  BBMSDatabase getInstance(Context context){
        synchronized (BBMSDatabase.class){
            if(sInstance==null){
                sInstance=new BBMSDatabase(context);
            }
        }
        return sInstance;
    }

    private BBMSDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        BBMSDbHistoryStorage.getInstance(mContext).onCreate(sqLiteDatabase);
        BBMSCachesDatabase.getInstance(mContext).onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
