package com.dissertation.toaok.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dissertation.toaok.utils.BBMSUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by TOAOK on 2017/11/4.
 */

public class BBMSUICheck extends BBMSUI {

    public RadioGroup mGroup;

    private RadioButton mRadioButton;

    public BBMSUICheck(Context context) {
        super(context);

        mGroup = new RadioGroup(context);
        mGroup.setOrientation(VERTICAL);
        layout.addView(mGroup, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }


    public void setValue(List<JSONObject> list) {
        for (int i=0;i<list.size();++i) {
            JSONObject json=list.get(i);
            mRadioButton=new RadioButton(getContext());
            mRadioButton.setText(json.optString("text"));
            mGroup.addView(mRadioButton,i, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, BBMSUtils.dip2px(44)));
        }
    }
}
