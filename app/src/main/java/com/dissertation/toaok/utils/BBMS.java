package com.dissertation.toaok.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.dissertation.toaok.database.BBMSCachesDatabase;
import com.dissertation.toaok.model.Account;
import com.dissertation.toaok.model.Subscribe;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by TOAOK on 2017/9/13.
 */

public class BBMS {

    public interface Callback {
        void callback(String response);
    }

    private static class MBinder extends Binder {
        public BBMS client;

        public MBinder(BBMS client) {
            this.client = client;
        }

    }

    private Context mContext;

    private BBMSCachesDatabase mCache;

    private BBMSURLHandle mURLHandle;


    private BBMS(Context context) {
        this.mContext = context;
        this.mCache = BBMSCachesDatabase.getInstance(context);
        this.mURLHandle = new BBMSURLHandle();
    }


    public static BBMS with(Context context) {
        ApplicationInfo info = context.getApplicationInfo();
        if (info.metaData == null) {
            info.metaData = new Bundle();
        }
        MBinder client = (MBinder) info.metaData.getBinder("BBMS");
        if (client == null) {
            client = new MBinder(new BBMS(context.getApplicationContext()));
            info.metaData.putBinder("BBMSClient", client);
            Intent result = new Intent();
            result.setAction("com.example.toaok.field.BBMS");
            context.sendBroadcast(result);
        }
        return client.client;
    }


    //请求查询图书请求（不带页数）
    public void send(String model, String cmd, String sw, Callback callback, boolean shared) {
        this.send(model, cmd, sw, 0, callback, false);
    }

    //请求查询图书请求（带页数）
    public BBMS send(String model, String cmd, String sw, int page, Callback callback, boolean shared) {
        String url = mURLHandle.getURL(model, cmd, sw, page);
        return this.send(url, callback, shared);
    }


    //请求校验用户登录信息
    public void send(String model, String cmd, Account account, Callback callback, boolean shared) {
        String url = mURLHandle.getURL(model, cmd, account);
        this.send(url, callback, shared);
    }

    public void send(String model, String cmd, Account account, boolean shared) {
        String url = mURLHandle.getURL(model, cmd, account);
        this.send(url, null, shared);
    }

    //请求查询订单信息
    public void send(String model, String cmd, int accountId, Callback callback, boolean shared) {
        String url = mURLHandle.getURL(model, cmd, accountId);
        this.send(url, callback, shared);
    }
    public void send(String model, String cmd, int accountId,boolean shared){
        send(model,cmd,accountId,null,shared);
    }
    //添加订阅信息
    public void send(String model, String cmd, Subscribe subscribe, Callback callback, boolean shared) {
        String url = mURLHandle.getURL(model, cmd, subscribe);
        this.send(url, callback, shared);
    }

    //执行请求
    public BBMS send(String url, Callback callback, boolean shared) {
        Log.i("URL", url.toString());
        BBMSRunnable run = new BBMSRunnable(url, callback, shared);
        run.post();
        return this;
    }

    private int req_runcode;

    private class BBMSRunnable extends Handler {

        private BBMS.Callback mHandlerCallback;
        private String mURL;
        private String results;
        private boolean mShared;

        private boolean isCallback = false;

        public BBMSRunnable(String url, BBMS.Callback callback, boolean shared) {
            this.mHandlerCallback = callback;
            this.mURL = url;
            this.mShared = shared;
        }

        private int jsonKey;

        public void post() {

            //获取URL的hashcode
            int key = mURL.hashCode();
            if (this.mShared && this.mHandlerCallback != null) {
                String json = mCache.getValue(key, "");
                //如果数据库中存储的数据不为空
                if (!TextUtils.isEmpty(json)) {
                    this.mHandlerCallback.callback(json);
                    jsonKey = json.hashCode();
                    this.isCallback = true;
                }
            }

            if (req_runcode == key) {
                return;
            } else {
                req_runcode = key;
            }
            //启动线程再次访问数据，如果数据有变化进行更新。
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BBMSRunnable.this.run();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    req_runcode = 0;
                }
            }).start();

        }

        private void run() throws IOException {
            Log.i("URL:", mURL);
            results = BBMSUtils.HttpGet(mURL);
            this.sendMessage(this.obtainMessage(0, this));
        }

        @Override
        public void handleMessage(Message msg) {

            BBMSRunnable run = (BBMSRunnable) msg.obj;

            if (msg.what == 0) {
                String results = run.results;

                Log.i("result", results);

                check(results);

                int jhcode = run.mURL.hashCode();

                if (mHandlerCallback != null) {

                    if (results.startsWith("[") || results.startsWith("{")) {
                        //判断获取的json和数据库中的json值是否相同
                        if (run.jsonKey != jhcode) {
                            //如果不相同更新数据
                            if (run.mShared) {
                                mCache.updateOrAdd(mURL.hashCode(), results);
                            }
                        }
                    }
                    if (!run.isCallback) {
                        run.mHandlerCallback.callback(results);
                    }
                }
            }
        }

        private synchronized void check(String results) {
            URL url = null;
            try {
                url = new URL(mURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            String path = url.getPath();
            String model = path.substring(path.lastIndexOf("/") + 1);

            if (model.equals("AccountServlet")) {
                Intent result = new Intent();
                result.setAction(BBMS.class.getName());
                result.putExtra("BBMSData", results);
                Log.i("Broadcast", "sendBroadcast");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(result);
            }
        }
    }
}
