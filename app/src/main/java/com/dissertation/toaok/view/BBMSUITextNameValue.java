package com.dissertation.toaok.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.dissertation.toaok.activity.R;
import com.dissertation.toaok.utils.BBMSUtils;

import org.json.JSONObject;

/**
 * Created by TOAOK on 2017/11/4.
 */

public class BBMSUITextNameValue extends BBMSUI {

    private TextView name;
    private TextView text;
    private TextView value;


    public BBMSUITextNameValue(Context context) {
        super(context);

        name = new TextView(context);
        name.setGravity(Gravity.CENTER_VERTICAL);
        name.setTextSize(14);
        layout.addView(name, new LayoutParams(0, BBMSUtils.dip2px(44), 3));

        text = new TextView(context);
        text.setGravity(Gravity.CENTER_VERTICAL);
        text.setTextSize(14);
        layout.addView(text, new LayoutParams(0, BBMSUtils.dip2px(44), 1));

        value = new TextView(context);
        value.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        value.setTextSize(14);
        layout.addView(value, new LayoutParams(0, BBMSUtils.dip2px(44), 1));
        setBackgroundColor(Color.WHITE);
    }

    public void setValue(JSONObject json){
        name.setText(BBMSUtils.setFontColorSpannableStringBuilder(json.optString("name"),0,getResources().getColor(R.color.colorRed,null)));
        text.setText(BBMSUtils.setFontColorSpannableStringBuilder(json.optString("text"),0,getResources().getColor(R.color.colorRed,null)));
        value.setText(BBMSUtils.setFontColorSpannableStringBuilder(json.optString("value"),0,getResources().getColor(R.color.colorRed,null)));
    }
}
