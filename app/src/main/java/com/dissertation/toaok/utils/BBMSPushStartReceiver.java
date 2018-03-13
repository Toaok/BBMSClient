package com.dissertation.toaok.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by TOAOK on 2017/9/13.
 */

public class BBMSPushStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast","has receiver start service broadcast ");
        context.startService(new Intent(context,BBMSPushService.class));
        Log.i("Broadcast","service has started ");
    }
}
