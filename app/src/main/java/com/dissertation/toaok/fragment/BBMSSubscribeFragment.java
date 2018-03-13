package com.dissertation.toaok.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dissertation.toaok.activity.BBMSBookInfoActivity;
import com.dissertation.toaok.activity.BBMSOrderActivity;
import com.dissertation.toaok.activity.R;
import com.dissertation.toaok.base.BBMSBaseFragment;
import com.dissertation.toaok.model.Account;
import com.dissertation.toaok.model.BookInfo;
import com.dissertation.toaok.model.Subscribe;
import com.dissertation.toaok.utils.BBMS;
import com.dissertation.toaok.utils.BBMSEvent;
import com.dissertation.toaok.utils.BBMSSharedPreferencesUtil;
import com.dissertation.toaok.utils.BBMSUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TOAOK on 2017/10/12.
 */

public class BBMSSubscribeFragment extends BBMSBaseFragment implements BBMS.Callback, BBMSEvent.OnEventListener {

    //控件
    private TextView mTitle;
    private RecyclerView mRecyclerView;
    private SubscribeInfoAdapter mAdapter;

    private LinearLayout mSubscribeBar;
    private CheckBox mChooseAll;
    private TextView mTotalPrice;
    private TextView mBorrow;

    //数据
    private boolean chooseAll = false;
    private ArrayList<BookInfo> mSubscribe;
    private ArrayList<BookInfo> mChooseList;

    public static BBMSSubscribeFragment newInstance() {

        BBMSSubscribeFragment fragment = new BBMSSubscribeFragment();
        Log.i("status", "BBMSSubscribeFragment has bean created!");

//        if (bookInfo!=null) {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(ARG_BOOK_INFO, bookInfo);
//            fragment.setArguments(bundle);
//        }

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscribe = new ArrayList();
        mChooseList = new ArrayList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_subscribe_info, container, false);
        //设置标题
        mTitle = view.findViewById(R.id.title);
        Paint paint = mTitle.getPaint();
        paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        mTitle.setText("订阅");

        //隐藏layout_bar
        mSubscribeBar = view.findViewById(R.id.subscribe_info_bar);
        if (mSubscribe.size() <= 0)
            mSubscribeBar.setVisibility(View.GONE);

        mChooseAll = mSubscribeBar.findViewById(R.id.subscribe_info_chooser);
        mTotalPrice = mSubscribeBar.findViewById(R.id.subscribe_info_amount);
        mBorrow = mSubscribeBar.findViewById(R.id.subscribe_info_borrow);

        mBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChooseList.size() > 0) {
                    startActivity(BBMSOrderActivity.newIntent(getContext(), mChooseList));
                }else {
                    BBMSUtils.showMessage(getContext(),"请选择你要借阅的图书！！！");
                }
            }
        });


        //实例化RecyclerView控件，并设置样式和适配器
        mRecyclerView = view.findViewById(R.id.subscribe_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        mAdapter = new SubscribeInfoAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //如果当前用户处于登录状态，请求订阅信息
        Account account = BBMSSharedPreferencesUtil.getInstance().getAccount();
        if (account != null) {
            BBMS.with(getContext()).send("AccountServlet", "querySubscribe", account.getAccountId(), BBMSSubscribeFragment.this, true);
        }

        mChooseAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mChooseList.size() > 0) {
                        mChooseList.clear();
                    }
                    mChooseList.addAll(mSubscribe);
                    mAdapter.notifyDataSetChanged();
                } else if (mChooseList.size() == mSubscribe.size()) {
                    mChooseList.clear();
                    mAdapter.notifyDataSetChanged();
                }

                updataSubscribeBar();

            }
        });

        return view;
    }

    @Override
    public void callback(String response) {
        handler(response);
    }

    @Override
    public void onHandler(BBMSEvent be) {
        String json = be.getBookinfos();
        try {
            handler(new JSONArray(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handler(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        handler(jsonObject.optJSONArray("subscribe"));
    }

    private void handler(JSONArray response) {
        if (response != null) {
            try {
                mSubscribe = BBMSUtils.json2Object(response);
                mAdapter.setDataSet(mSubscribe);
                if (mSubscribe.size() > 0) {
                    mSubscribeBar.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void updataSubscribeBar() {
        if (mChooseList.size() == mSubscribe.size()) {
            chooseAll = true;
        } else {
            chooseAll = false;
        }
        mChooseAll.setChecked(chooseAll);
        if (mChooseList.size() > 0) {
            mTotalPrice.setText(BBMSUtils.setFontColorSpannableStringBuilder("合计：¥" + BBMSUtils.getTotalPrice(mChooseList), "¥", ContextCompat.getColor(getContext(), R.color.colorRed)));
        } else {
            mTotalPrice.setText("合计：¥0");
        }
    }


    class SubscribeInfoAdapter extends RecyclerView.Adapter {

        private List<BookInfo> dataSet;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_subscribe, parent, false);
            return new SubscribeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((SubscribeViewHolder) holder).bindSubscribe(dataSet.get(position));
        }

        @Override
        public int getItemCount() {
            int count = 0;
            if (dataSet != null) {
                count = dataSet.size();
            }
            return count;
        }

        class SubscribeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private BookInfo mBookInfo;
            private TextView mSubscribeBookName;
            private TextView mSubscribePrice;
            private TextView mSubscribeQuantity;
            private ImageView mSubscribeCover;
            private TextView mSubscribeDelete;
            private CheckBox mSubscribeChoose;

            public SubscribeViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                mSubscribeBookName = itemView.findViewById(R.id.subscribe_book_name);
                mSubscribePrice = itemView.findViewById(R.id.subscribe_book_price);
                mSubscribeQuantity = itemView.findViewById(R.id.subscribe_quantity);
                mSubscribeChoose = itemView.findViewById(R.id.subscribe_choose);

                mSubscribeCover = itemView.findViewById(R.id.subscribe_book_cover);
                mSubscribeDelete = itemView.findViewById(R.id.subscribe_delete);
                mSubscribeDelete.setText("\ue900");
                mSubscribeDelete.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                mSubscribeDelete.setTypeface(BBMSUtils.getTypeface(getContext(), "fonts/icomoon.ttf"));

            }

            public void bindSubscribe(BookInfo subscribe) {

                this.mBookInfo = subscribe;

                Glide.with(getActivity()).load(mBookInfo.getBookCover()).into(mSubscribeCover);

                TextPaint paint = mSubscribeBookName.getPaint();
                paint.setFakeBoldText(true);
                mSubscribeBookName.setText(mBookInfo.getBookName());
                mSubscribeQuantity.setText("x1");
                mSubscribePrice.setText(BBMSUtils.setFontColorSpannableStringBuilder("¥" + mBookInfo.getBookPrice(), 0, ContextCompat.getColor(getContext(), R.color.colorRed)));
                mSubscribeDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Subscribe subscribe = BBMSUtils.createSubscribe(mBookInfo);
                        BBMS.with(getContext()).send("AccountServlet", "unsubscribe", subscribe, BBMSSubscribeFragment.this, false);
                    }
                });

                if (mChooseList.indexOf(mBookInfo) >= 0) {
                    mSubscribeChoose.setChecked(true);
                } else {
                    mSubscribeChoose.setChecked(false);
                }
                mSubscribeChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            if (mChooseList.indexOf(mBookInfo) < 0) {
                                mChooseList.add(mBookInfo);
                            }
                        } else {
                            mChooseList.remove(mBookInfo);
                        }
                        updataSubscribeBar();
                    }
                });
            }

            @Override
            public void onClick(View view) {
                startActivity(BBMSBookInfoActivity.newIntent(BBMSSubscribeFragment.this.getContext(),mBookInfo));
            }
        }

        public void setDataSet(List<BookInfo> dataSet) {
            this.dataSet = dataSet;
        }
    }
}
