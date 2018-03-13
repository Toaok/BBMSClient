package com.dissertation.toaok.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.dissertation.toaok.base.BBMSBaseActivity;
import com.dissertation.toaok.fragment.BBMSReaderInfoFragment;

/**
 * Created by TOAOK on 2017/11/28.
 */

public class BBMSReaderInfoActivity extends BBMSBaseActivity {
    @Override
    protected Fragment createFragment() {
        return BBMSReaderInfoFragment.newInstance();
    }
    public static Intent newIntent(Context packageContext){
        Intent intent=new Intent(packageContext,BBMSReaderInfoActivity.class);
        return intent;
    }
}
