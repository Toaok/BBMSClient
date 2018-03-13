package com.dissertation.toaok.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dissertation.toaok.activity.BBMSSearchResultActivity;
import com.dissertation.toaok.activity.R;

/**
 * Created by TOAOK on 2017/10/30.
 */

public class BBMSSearchFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_search_layout, container, false);

        return view;
    }
    private void submit(String s) {
        if (!TextUtils.isEmpty(s)) {
            startActivity(BBMSSearchResultActivity.newIntent(getContext(), s, false));
        }
    }
}
