package com.dissertation.toaok.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.dissertation.toaok.base.BaseHistoryStorage;
import com.dissertation.toaok.model.SearchHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TOAOK on 2017/10/30.
 */

public class BBMSDbHistoryStorage extends BaseHistoryStorage {

    private static final int DEFALUT_INT = 5;
    private static BBMSDbHistoryStorage sInstance = null;
    private static SimpleDateFormat mDataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private int maxItem;
    private BBMSDatabase mBBMSDatabase;

    private BBMSDbHistoryStorage(Context context, int maxItem) {
        mBBMSDatabase = BBMSDatabase.getInstance(context.getApplicationContext());
        this.maxItem = maxItem;
    }

    public static BBMSDbHistoryStorage getInstance(Context context, int maxItem) {

        synchronized (BBMSDbHistoryStorage.class) {
            if (sInstance == null) {
                sInstance = new BBMSDbHistoryStorage(context, maxItem);
            }
        }
        return sInstance;
    }

    public static BBMSDbHistoryStorage getInstance(Context context) {
        synchronized (BBMSDbHistoryStorage.class) {
            if (sInstance == null) {
                sInstance = new BBMSDbHistoryStorage(context.getApplicationContext(), DEFALUT_INT);
            }
        }
        return sInstance;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + SearchHistoryColumns.NAME + "("
                + SearchHistoryColumns.TIMESEARCHED + " Long primary key not null,"
                + SearchHistoryColumns.SEARCHSTRING + " Text not null)");
    }

    @Override
    public void save(String searchString) {
        if (TextUtils.isEmpty(searchString)) {
            return;
        }

        SQLiteDatabase db = mBBMSDatabase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SearchHistoryColumns.TIMESEARCHED, generateKey());
        values.put(SearchHistoryColumns.SEARCHSTRING, searchString);
        db.insert(SearchHistoryColumns.NAME, null, values);

        Cursor older = null;
        try {
            older = db.query(SearchHistoryColumns.NAME, new String[]{SearchHistoryColumns.TIMESEARCHED}, null, null, null, null, SearchHistoryColumns.TIMESEARCHED + "ASC");

            while (older != null && older.getCount() > maxItem) {
                older.moveToPosition(older.getCount() - maxItem);
                long tomeOfRecordToKeep = older.getLong(0);
                db.delete(SearchHistoryColumns.NAME, SearchHistoryColumns.TIMESEARCHED + "<?", new String[]{String.valueOf(tomeOfRecordToKeep)});
            }
        } finally {
            if (older != null) {
                older.close();
            }
        }
        db.close();
    }

    @Override
    public void remove(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SQLiteDatabase db = mBBMSDatabase.getWritableDatabase();
        db.delete(SearchHistoryColumns.NAME, SearchHistoryColumns.TIMESEARCHED + "=?", new String[]{key});
    }

    @Override
    public void clear() {
        SQLiteDatabase db=mBBMSDatabase.getWritableDatabase();
        db.execSQL("delete from "+SearchHistoryColumns.NAME);
    }

    @Override
    public String generateKey() {
        return mDataFormat.format(new Date());
    }

    @Override
    public List<SearchHistory> getHistory() {
        SQLiteDatabase db = mBBMSDatabase.getReadableDatabase();

        Cursor searches = db.query(SearchHistoryColumns.NAME,
                new String[]{SearchHistoryColumns.SEARCHSTRING, SearchHistoryColumns.TIMESEARCHED}, null, null, null, null,
                SearchHistoryColumns.TIMESEARCHED + " DESC", String.valueOf(maxItem));

        List<SearchHistory> searchHistorys = new ArrayList<>();
        try {


            if (searches != null && searches.moveToFirst()) {
                int searchStringIndex = searches.getColumnIndex(SearchHistoryColumns.SEARCHSTRING);
                int timeSearched = searches.getColumnIndex(SearchHistoryColumns.TIMESEARCHED);

                do {
                    SearchHistory searchHistory = new SearchHistory();
                    searchHistory.setSearchString(searches.getString(searchStringIndex));
                    searchHistory.setKey(searches.getString(timeSearched));
                    searchHistorys.add(searchHistory);

                } while (searches.moveToNext());
            }
        } finally {
            if (searches != null) {
                searches.close();
            }
        }
        db.close();
        return searchHistorys;
    }


    public interface SearchHistoryColumns {
        //table name
        String NAME = "searchHisatory";
        //what was searched
        String SEARCHSTRING = "searchstring";
        //time of search
        String TIMESEARCHED = "timesearched";
    }

}
