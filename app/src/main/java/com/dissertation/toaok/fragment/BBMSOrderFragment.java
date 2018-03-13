package com.dissertation.toaok.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dissertation.toaok.activity.R;
import com.dissertation.toaok.base.BBMSBaseFragment;
import com.dissertation.toaok.base.BBMSTableAdapter;
import com.dissertation.toaok.model.BookInfo;
import com.dissertation.toaok.utils.BBMSUtils;
import com.dissertation.toaok.view.BBMSUICheck;
import com.dissertation.toaok.view.BBMSUIText;
import com.dissertation.toaok.view.BBMSUITextNameValue;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by TOAOK on 2017/11/3.
 */

public class BBMSOrderFragment extends BBMSBaseFragment {

    private static final String ARG_SUBSCRIBES = "subscribes";


    private RecyclerView mRecyclerView;
    private OrderAdapter mAdapter;

    private List<JSONObject> product;
    private List<JSONObject> readerinfo;
    private List<JSONObject> amounts;
    private List<JSONObject> payments;


    public static BBMSOrderFragment newInstance(ArrayList list) {
        BBMSOrderFragment fragment = new BBMSOrderFragment();
        Log.i("status", "BBMSOrderFragment hsa bean created!");
        if (list != null) {
            Bundle args = new Bundle();
            args.putParcelableArrayList(ARG_SUBSCRIBES, list);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        product = new LinkedList<>();
        readerinfo = new LinkedList();
        amounts = new LinkedList();
        payments = new LinkedList();


        setProduct();
        setReaderinfo(new JSONObject());
        setAmounts(new JSONObject());
        setPayments(new JSONObject());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmrnt_order, container, false);

        Typeface typeface = BBMSUtils.getTypeface(getContext(), "fonts/icomoon.ttf");

        //设置返回按钮
        TextView back = view.findViewById(R.id.back);
        back.setText("\ue901");
        back.setTypeface(typeface);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        //设置标题
        TextView title = view.findViewById(R.id.title);
        Paint paint = title.getPaint();
        paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        title.setText("订单结算");

        //order_bar
        LinearLayout linearLayout=view.findViewById(R.id.order_bar);

            TextView amount = view.findViewById(R.id.order_amount);
            amount.setText(BBMSUtils.setFontColorSpannableStringBuilder("合计：¥" + BBMSUtils.formatPrice(getArgument().size() * 3.0), "¥", ContextCompat.getColor(getContext(), R.color.colorRed)));
            TextView settlement=view.findViewById(R.id.order_settlement);
            settlement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    BBMSUtils.showMessage(getContext(),"该系统还未开启支付功能!!!");
                }
            });

        //实例化mRecycleryView ,并设值其样式
        mRecyclerView = view.findViewById(R.id.order_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        mAdapter = new OrderAdapter();
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }


    private List getArgument() {
        return getArguments().getParcelableArrayList(ARG_SUBSCRIBES);
    }


    private void setProduct() {
        List<BookInfo> list = getArgument();
        for (BookInfo bookInfo : list) {
            this.product.add(new JSONObject(BBMSUtils.parseMap("name", bookInfo.getBookName(), "text", "x1", "value", "3.00")));
        }
    }

    private void setReaderinfo(JSONObject readerinfo) {
        this.readerinfo.add(new JSONObject(BBMSUtils.parseMap("name", "读者", "value", "1")));
        this.readerinfo.add(new JSONObject(BBMSUtils.parseMap("name", "借阅期限", "value", "45天")));
        this.readerinfo.add(new JSONObject(BBMSUtils.parseMap("name", "备注", "value", "无")));
    }

    private void setAmounts(JSONObject amounts) {
        this.amounts.add(new JSONObject(BBMSUtils.parseMap("name", "合计", "value", product.size()*3.00+"¥")));
        this.amounts.add(new JSONObject(BBMSUtils.parseMap("name", "应付金额", "value", product.size()*3.00+"¥")));
    }

    private void setPayments(JSONObject payments) {
        this.payments.add(new JSONObject(BBMSUtils.parseMap("value", "alipay", "text", "支付宝支付")));
        this.payments.add(new JSONObject(BBMSUtils.parseMap("value", "alipay", "text", "微信支付")));
        this.payments.add(new JSONObject(BBMSUtils.parseMap("value", "alipay", "text", "校园卡支付")));
    }

    class OrderAdapter extends BBMSTableAdapter {

        @Override
        public int getSectionCount() {
            List list = getArgument();
            return list != null && list.size() > 0 ? 4 : 3;
        }

        @Override
        public View onCreateView(ViewGroup parent, int viewtype) {

            switch (viewtype) {
                case NOMARLTYPE:
                    BBMSUIText text = new BBMSUIText(parent.getContext());
                    text.setBackgroundColor(Color.WHITE);
                    return text;
                case HEADERTYPE:
                    TextView textView = new TextView(parent.getContext());
                    textView.setTextColor(0xff888888);
                    textView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.colorDivider));
                    textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, BBMSUtils.dip2px(22)));
                    textView.setVisibility(View.GONE);
                    return textView;
                case 2:
                    return new BBMSUICheck(parent.getContext());
                case 3:
                    return new BBMSUITextNameValue(getContext());
            }

            return super.onCreateView(parent, viewtype);
        }

        @Override
        public void onBindItemView(View view, int section, int row) {

            switch (section) {
                case 0:
                    BBMSUIText reader_view = (BBMSUIText) view;
                    reader_view.setValue(readerinfo.get(row));
                    break;
                case 1:
                    BBMSUIText ammount_view = (BBMSUIText) view;
                    ammount_view.setValue(amounts.get(row));
                    break;
                case 2:
                    BBMSUICheck check = (BBMSUICheck) view;
                    check.setValue(payments);
                    break;
                case 3:
                    BBMSUITextNameValue nameValue = (BBMSUITextNameValue) view;
                    nameValue.setValue(product.get(row));
                    break;
            }
            super.onBindItemView(view, section, row);
        }

        @Override
        public void onBindHeaderView(View view, int section) {
            if(section==2){
                TextView textView= (TextView) view;
                textView.setText("支付方式");
                textView.setVisibility(View.VISIBLE);
            }
            super.onBindHeaderView(view, section);
        }

        @Override
        protected int getItemsCountOfSection(int section) {
            switch (section) {
                case 0:
                    return readerinfo.size();
                case 1:
                    return amounts.size();
                case 2:
                    return payments.size()/payments.size();
                case 3:
                    return getArgument() == null ? 0 : getArgument().size();

            }
            return 0;
        }

        @Override
        public int getItemViewType(int section, int row) {

            switch (section) {
                case 0:/*读者信息*/
                case 1:/*合计金额*/
                    return NOMARLTYPE;
                case 2:/*支付方式*/
                    return 2;
                case 3:/*货物*/
                    return 3;
            }

            return super.getItemViewType(section, row);
        }
    }
}