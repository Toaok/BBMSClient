package com.dissertation.toaok.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.dissertation.toaok.base.BBMSBaseActivity;
import com.dissertation.toaok.fragment.BBMSSearchResultFragment;

/**
 * Created by TOAOK on 2017/9/20.
 */

public class BBMSSearchResultActivity extends BBMSBaseActivity {
    private static final String EXTRA_SEARCH_STRING="com.dissertation.toaok.activity.search_string";
    private static final String EXTRA_SEARCH_STATUS="com.com.dissertation.toaok.activity.search_status";

    public static Intent newIntent(Context packageContext, String searchSting,boolean searchStatus){
        Intent intent=new Intent(packageContext,BBMSSearchResultActivity.class);
        intent.putExtra(EXTRA_SEARCH_STATUS,searchStatus);
        intent.putExtra(EXTRA_SEARCH_STRING,searchSting);
        return intent;
    }
    public static Intent newIntent(Context packageContext, boolean searchStatus){
        Intent intent=new Intent(packageContext,BBMSSearchResultActivity.class);
        intent.putExtra(EXTRA_SEARCH_STATUS,searchStatus);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Log.i("status","BBMSSearchResultActivity is running");
        boolean searchStatus=getIntent().getBooleanExtra(EXTRA_SEARCH_STATUS,false);
        if(!searchStatus) {
            String searchString = (String) getIntent().getSerializableExtra(EXTRA_SEARCH_STRING);
            return BBMSSearchResultFragment.newInstance(searchString);
        }else {
            return BBMSSearchResultFragment.newInstance(searchStatus);
        }

    }
}
