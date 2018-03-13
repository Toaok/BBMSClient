package com.dissertation.toaok.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dissertation.toaok.activity.R;
import com.dissertation.toaok.base.BBMSBaseFragment;

/**
 * Created by TOAOK on 2017/11/28.
 */

public class BBMSReaderInfoFragment extends BBMSBaseFragment {


    public static BBMSReaderInfoFragment newInstance() {
        BBMSReaderInfoFragment fragment = new BBMSReaderInfoFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reader_info, container, false);

        return view;
    }
}
