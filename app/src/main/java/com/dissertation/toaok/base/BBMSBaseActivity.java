package com.dissertation.toaok.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.dissertation.toaok.activity.R;
import com.dissertation.toaok.utils.BBMSSharedPreferencesUtil;

/**
 * Created by TOAOK on 2017/9/18.
 */

public abstract class BBMSBaseActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    private ActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setNavigationBarColor(getResources().getColor(R.color.colorAccent,null));
        }

        setContentView(R.layout.activity_fragment);

        //初始化SharedPreferencesUtil
        BBMSSharedPreferencesUtil.initSharedPreferencesUtil(this.getApplicationContext());

        //获取FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //通过fragmentManager获得Fragment
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

}
