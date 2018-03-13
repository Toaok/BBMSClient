package com.dissertation.toaok.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.dissertation.toaok.fragment.BBMSMainFragment;
import com.dissertation.toaok.fragment.BBMSSubscribeFragment;
import com.dissertation.toaok.fragment.BBMSUserInfoFragment;
import com.dissertation.toaok.model.Account;
import com.dissertation.toaok.utils.BBMSSharedPreferencesUtil;
import com.dissertation.toaok.utils.BBMSUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TOAOK on 2017/9/19.
 */

public class BBMSPagerActivity extends FragmentActivity {

    public static final int SEARCH_PAGE_INDEX = 0;
    public static final int SUBSCRIBE_PAGE_INDEX = 1;
    public static final int USER_INFO_PAGE_INDEX = 2;

    private static final String EXTRA_CURRENT_INDEX = "com.dissertation.toaok.activity.current_index";
    private static final String EXTRA_USER = "com.dissertation.toaok.activity.user";


    private ViewPager mViewPager;

    private TextView mSearchTab;
    private TextView mSubscribeTab;
    private TextView mUserInfoTab;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setNavigationBarColor(getResources().getColor(R.color.colorAccent,null));
        }

        setContentView(R.layout.activity_fragment);

        setContentView(R.layout.activity_pager);
        initView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        mViewPager.getCurrentItem();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (!BBMSUtils.checkUserIsLogin()) {
                    startActivity(BBMSLoginActivity.newIntent(BBMSPagerActivity.this,true));
                    finish();
                } else {
                    setTabBackground(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Intent intent = this.getIntent();
        if (intent != null) {
            mViewPager.setCurrentItem(intent.getIntExtra(EXTRA_CURRENT_INDEX, 0));
        }
    }

    private void initView() {
        BBMSSharedPreferencesUtil.initSharedPreferencesUtil(this.getApplicationContext());

        mViewPager = findViewById(R.id.activity_pager_view_pager);


        mSearchTab = findViewById(R.id.search_tab);
        mSubscribeTab = findViewById(R.id.subscribe_info_tab);
        mUserInfoTab = findViewById(R.id.user_info_tab);

        setTabBackground(SEARCH_PAGE_INDEX);

        mSearchTab.setOnClickListener(new OnClickListener(SEARCH_PAGE_INDEX));
        mSubscribeTab.setOnClickListener(new OnClickListener(SUBSCRIBE_PAGE_INDEX));
        mUserInfoTab.setOnClickListener(new OnClickListener(USER_INFO_PAGE_INDEX));

        fragmentList = new ArrayList<>();
        fragmentList.add(BBMSMainFragment.newInstance());
        fragmentList.add(BBMSSubscribeFragment.newInstance());
        fragmentList.add(BBMSUserInfoFragment.newInstance());


    }

    public static Intent newIntent(Context packageContext, int currentIndex) {
        Intent intent = new Intent(packageContext, BBMSPagerActivity.class);
        intent.putExtra(EXTRA_CURRENT_INDEX, currentIndex);
        return intent;
    }

    public static Intent newIntent(Context packageContext, Account account) {
        Intent intent = new Intent(packageContext, BBMSPagerActivity.class);
        intent.putExtra(EXTRA_USER, account);
        return intent;
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, BBMSPagerActivity.class);
        return intent;
    }

    class OnClickListener implements View.OnClickListener {
        private int index;

        public OnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {

            if (index > 0 && !BBMSUtils.checkUserIsLogin()) {
                startActivity(BBMSLoginActivity.newIntent(BBMSPagerActivity.this,false));
            } else {
                setTabBackground(index);
                mViewPager.setCurrentItem(index);
            }
        }
    }

    private void setTabBackground(int index) {
        switch (index) {
            case SEARCH_PAGE_INDEX:
                initTabBackground();
                mSearchTab.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                break;
            case SUBSCRIBE_PAGE_INDEX:
                initTabBackground();
                mSubscribeTab.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                break;
            case USER_INFO_PAGE_INDEX:
                initTabBackground();
                mUserInfoTab.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

                break;
        }
    }

    private void initTabBackground() {
        Typeface typeface = BBMSUtils.getTypeface(this, "fonts/icomoon.ttf");
        mSearchTab.setTextColor(ContextCompat.getColor(this, R.color.colorFootBar));
        mSearchTab.setText("\ue909");
        mSearchTab.setTypeface(typeface);
        mSubscribeTab.setTextColor(ContextCompat.getColor(this, R.color.colorFootBar));
        mSubscribeTab.setText("\ue902");
        mSubscribeTab.setTypeface(typeface);
        mUserInfoTab.setTextColor(ContextCompat.getColor(this, R.color.colorFootBar));
        if (BBMSUtils.checkUserIsLogin()) {
            mUserInfoTab.setText("\ue907");
        } else {
            mUserInfoTab.setText("\ue906");
        }
        mUserInfoTab.setTypeface(typeface);
    }

}
