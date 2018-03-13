package com.dissertation.toaok.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.dissertation.toaok.utils.BBMSUtils;

import org.json.JSONObject;

/**
 * Created by TOAOK on 2017/11/3.
 */

public class BBMSUIText extends BBMSUI  {

    public TextView text;
    public TextView desc;

    public BBMSUIText(Context context) {
        super(context);

        text = new TextView(context);
        text.setGravity(Gravity.CENTER_VERTICAL);
        text.setTextSize(16);
        layout.addView(text, LayoutParams.WRAP_CONTENT, BBMSUtils.dip2px(44));

        desc = new TextView(context);
        desc.setSingleLine(true);
        desc.setGravity(Gravity.CENTER_VERTICAL);
        desc.setTextColor(0xff999999);
        desc.setTextSize(16);
        desc.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        layout.addView(desc, new LayoutParams(0, BBMSUtils.dip2px(44), 1));
    }


    public void setValue(JSONObject json) {
        text.setText(json.optString("name"));
        desc.setText(json.optString("value"));
    }
}
