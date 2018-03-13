package com.dissertation.toaok.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.dissertation.toaok.base.BBMSBaseActivity;
import com.dissertation.toaok.fragment.BBMSBookInfoFragment;
import com.dissertation.toaok.model.BookInfo;

import java.io.Serializable;

/**
 * Created by TOAOK on 2017/9/21.
 */

public class BBMSBookInfoActivity extends BBMSBaseActivity{
    private static final String EXTRA_BOOK_INFO="com.dissertation.toaok.activity.book_info";
    @Override
    protected Fragment createFragment() {
        BookInfo bookInfo= (BookInfo) getIntent().getSerializableExtra(EXTRA_BOOK_INFO);
        return BBMSBookInfoFragment.newInstance(bookInfo);
    }

    public static Intent newIntent(Context packageContext, BookInfo bookinfo){
        Intent intent=new Intent(packageContext,BBMSBookInfoActivity.class);
        intent.putExtra(EXTRA_BOOK_INFO,bookinfo);
        return intent;
    }
}
