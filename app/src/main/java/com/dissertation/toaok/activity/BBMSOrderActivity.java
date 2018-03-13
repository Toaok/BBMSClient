package com.dissertation.toaok.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.dissertation.toaok.base.BBMSBaseActivity;
import com.dissertation.toaok.fragment.BBMSOrderFragment;

import java.util.ArrayList;

/**
 * Created by TOAOK on 2017/11/4.
 */

public class BBMSOrderActivity extends BBMSBaseActivity {

    private static final String EXTRA_SUBSCIBES = "subscribes";

    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        return BBMSOrderFragment.newInstance(intent.getParcelableArrayListExtra(EXTRA_SUBSCIBES));
    }

    public static Intent newIntent(Context packageContext, ArrayList list) {
        Intent intent = new Intent(packageContext, BBMSOrderActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_SUBSCIBES, list);
        return intent;

    }

}
