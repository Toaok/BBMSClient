package com.dissertation.toaok.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dissertation.toaok.utils.BBMSUtils;

/**
 * Created by TOAOK on 2017/11/3.
 */

public class BBMSUI extends LinearLayout {


    protected LinearLayout layout;
    protected TextView accessory;//附件

    public BBMSUI(Context context) {
        super(context);
        int padding = BBMSUtils.dip2px(15);
        this.setBackgroundColor(Color.WHITE);
        this.setPadding(padding, 0, padding, 0);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        layout = new LinearLayout(context);
        layout.setOrientation(HORIZONTAL);
        accessory = new TextView(context);

        this.addView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        this.addView(accessory, new LinearLayout.LayoutParams(BBMSUtils.dip2px(20), LinearLayout.LayoutParams.MATCH_PARENT));
        accessory.setGravity(Gravity.CENTER);

        accessory.setTypeface(BBMSUtils.getTypeface(context, "fonts/icomoon.ttf"));
        accessory.setText("\ue905");
        accessory.setTextSize(24);
        accessory.setTextColor(0xffc8c8cd);
        accessory.setVisibility(View.GONE);
    }

    public void setAccessory(boolean accessory) {
        int padding = BBMSUtils.dip2px(15);

        this.setPadding(padding, 0, accessory ? 4 : padding, 0);

        this.accessory.setVisibility(accessory ? VISIBLE : GONE);
    }

    public boolean getAccessory() {
        return accessory.getVisibility() == VISIBLE;
    }

}
