package com.dissertation.toaok.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dissertation.toaok.model.Account;
import com.dissertation.toaok.model.ReaderInfo;

/**
 * Created by TOAOK on 2017/10/16.
 */

public class BBMSSharedPreferencesUtil {

    public static final String ACCOUNT_KEY = "account_key";
    private static final String LOGIN_STATUS_KEY = "login_status_key";
    private static final String READER_INFO_KEY = "readerInfo_key";

    private static BBMSSharedPreferencesUtil mPerferencesUtil = null;
    private static boolean isLoggedIn;
    private SharedPreferences mPerferences;
    private Account mAccount;
    private ReaderInfo mReaderInfo;


    private BBMSSharedPreferencesUtil(Context context) {
        mPerferences = context.getSharedPreferences("ShardPreferencesUtil", Context.MODE_PRIVATE | Context.MODE_APPEND);
    }

    public static void initSharedPreferencesUtil(Context context) {
        if (mPerferencesUtil == null) {
            mPerferencesUtil = new BBMSSharedPreferencesUtil(context);
        }
    }

    public static BBMSSharedPreferencesUtil getInstance() {
        return mPerferencesUtil;
    }

    //添加账号
    public void setAccount(Account account) {
        isLoggedIn = true;
        mPerferences.edit().putString(ACCOUNT_KEY, BBMSUtils.obj2Str(account)).commit();
        mPerferences.edit().putBoolean(LOGIN_STATUS_KEY, isLoggedIn).commit();
        mAccount = account;
    }

    //获取账号
    public Account getAccount() {
        if (mAccount == null) {
            Object obj = BBMSUtils.str2Obj(mPerferences.getString(ACCOUNT_KEY, ""));
            if (obj != null) {
                mAccount = (Account) obj;
            }
        }
        return mAccount;
    }

    //删除账号
    public void deleteAccount(String key) {
        mPerferences.edit().putString(key, "").commit();
        mAccount = null;
    }

    //添加读者信息
    public void setReaderInfo(ReaderInfo readerInfo) {
        mPerferences.edit().putString(READER_INFO_KEY,BBMSUtils.obj2Str(readerInfo)).commit();
        mReaderInfo=readerInfo;
    }

    //获取读者信息
    public ReaderInfo getReaderInfo() {
        if(mReaderInfo==null){
            Object obj=BBMSUtils.str2Obj(mPerferences.getString(READER_INFO_KEY,""));
            if(obj!=null){
                mReaderInfo= (ReaderInfo) obj;
            }
        }
        return mReaderInfo;
    }

    //删除读者信息
    public void deleteReaderInfo() {

    }

    //获取登录状态
    public boolean getLoginStatus() {
        if (!isLoggedIn) {
            isLoggedIn = mPerferences.getBoolean(LOGIN_STATUS_KEY, false);
        }
        return isLoggedIn;
    }

    //添加本机标识
    public String getAccessPath() {
        BBMSURLHandle mURLHandle = new BBMSURLHandle();
        return mURLHandle.getURL("AccountServlet", "validation", mAccount);
    }

}
