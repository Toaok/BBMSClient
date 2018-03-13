package com.dissertation.toaok.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.dissertation.toaok.utils.BBMS;
import com.dissertation.toaok.utils.BBMSEvent;
import com.dissertation.toaok.utils.BBMSUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TOAOK on 2017/10/23.
 */

public abstract class BBMSBaseFragment extends Fragment {
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonStr = intent.getStringExtra("BBMSData");
            if (!TextUtils.isEmpty(jsonStr)) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BBMSEvent be = new BBMSEvent(jsonObject.optString("account",""), jsonObject.optString("subscribe",""), jsonObject.optString("user_info",""));

                if (BBMSBaseFragment.this instanceof BBMSEvent.OnEventListener)
                    ((BBMSEvent.OnEventListener) BBMSBaseFragment.this).onHandler(be);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BBMSBaseFragment.this instanceof BBMSEvent.OnEventListener) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BBMS.class.getName());
            Log.i("Broadcast", "registerReceiver");
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BBMSBaseFragment.this instanceof BBMSEvent.OnEventListener) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
        }
    }
}
