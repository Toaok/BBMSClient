package com.dissertation.toaok.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.dissertation.toaok.base.BBMSBaseActivity;
import com.dissertation.toaok.fragment.BBMSLoginFragment;

public class BBMSLoginActivity extends BBMSBaseActivity {
    private static final String EXTRA_IS_SLIDING = "com.dissertation.toaok.activity.user_id";

    @Override
    protected Fragment createFragment() {
        Log.i("status", "BBMSLoginActivity is running");
        boolean isSliding=getIntent().getBooleanExtra(EXTRA_IS_SLIDING,false);
        return BBMSLoginFragment.newInstance(isSliding);
    }

    public static Intent newIntent(Context packageContext,boolean isSliding) {
        Intent intent = new Intent(packageContext, BBMSLoginActivity.class);
        intent.putExtra(EXTRA_IS_SLIDING,isSliding);
        return intent;
    }

}
