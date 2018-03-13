package com.dissertation.toaok.utils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by TOAOK on 2017/9/13.
 */

public class BBMSPushService extends Service {

    private static final String ACTION_FILTER = "com.dissertation.toaok.Push";

    private PushPolling mPolling;

    //接收者，判断网络是否连接
    private ConnectivityChangeReceiver mReceiver = new ConnectivityChangeReceiver() {
        @Override
        protected void onDisconnected() {
            mPolling.stop();
        }

        @Override
        protected void onConnected() {
            mPolling.start();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("Broadcast","start create service");
        start();
        Log.i("Broadcast","start service");
        Log.i("Broadcast","start register receiver");
        this.registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        this.registerReceiver(mReceiver, new IntentFilter(ACTION_FILTER));
        Log.i("Broadcast","has registered receiver");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPolling != null) {
            mPolling.stop();
        }

        this.unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void callback(String response) {
        if (response == null) {
            return;
        }
        Intent result = new Intent();
        result.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        result.putExtra("BBMSData", response);
        result.setAction(BBMS.class.getName());
        this.sendBroadcast(result);
    }


    private void start() {
        Log.i("Broadcast","init SharedPreferencesUtil");
        BBMSSharedPreferencesUtil.initSharedPreferencesUtil(this);
        Log.i("Broadcast","init SharedPreferencesUtil successful");
        Log.i("Broadcast","get access path");
        String accessPath = BBMSSharedPreferencesUtil.getInstance().getAccessPath();
        Log.i("Broadcast","get access path successful");
        if (mPolling != null) {
            mPolling.stop();
        }
        mPolling = new PushPolling(accessPath);
        mPolling.start();
    }

    private class PushPolling extends Handler {

        private Polling polling;
        private URI pushURI;
        private String tip;
        private ExecutorService fixedThreadPool;

        public PushPolling(String uri) {
            this.pushURI = URI.create(uri);
            fixedThreadPool = Executors.newFixedThreadPool(2);
        }

        public void stop() {
            Log.i("Push", "stop");
            if (polling != null) {
                polling.run = false;
            }
        }

        public void start() {
            this.stop();

            try {
                polling = new Polling(pushURI.toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            fixedThreadPool.execute(polling);
            Log.i("Push", "thread_N");
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String jsonStr = (String) msg.obj;
                    callback(jsonStr);
                    break;
                case -1:
                    break;
                case -2:
                    break;

            }

        }

        private class Polling implements Runnable {

            private boolean run = false;
            private URL url;

            public Polling(String url) {
                try {
                    this.url = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            public Polling(URL url) {
                this.url = url;
            }

            @Override
            public void run() {
                run = true;
                while (run) {
                    String jsonStr = BBMSUtils.HttpGet(url);
                    if (jsonStr.length() > 0) {
                        PushPolling.this.handleMessage(Message.obtain(PushPolling.this, 0, jsonStr));
                        Log.i("Push", jsonStr);
                    }
                }
                this.run = false;
            }
        }
    }

    public abstract class ConnectivityChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*监听网络变化
            * ConnectivityManager主要管理和网络连接相关的操作
            * 相关的:TelephonyManager则管理和手机、运营商等的相关信息；
            *        WifiManager则管理和wifi相关的信息。
            * 想访问网络状态，首先得添加权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
            * NetworkInfo类包含了对wifi和mobile两种网络模式连接的详细描述,通过其getState()方法获取的State对象则代表着
            * 连接成功与否等状态。
            * */
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //检测当前网络连接是否可用
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            //网络连接状态
            boolean wifiConnected = false;//wifi
            boolean gprsConnected = false;//移动数据

            if (networkInfo != null) {
                //判断网络是否连接
                if (networkInfo.isConnected()) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        wifiConnected = true;
                    } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        gprsConnected = true;
                    }
                }
            }
            if (wifiConnected || gprsConnected) {
                onConnected();
                return;
            } else {
                onDisconnected();
                return;
            }
        }

        protected abstract void onDisconnected();

        protected abstract void onConnected();
    }


}
