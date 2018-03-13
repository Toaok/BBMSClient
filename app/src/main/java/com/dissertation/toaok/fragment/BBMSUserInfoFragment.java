package com.dissertation.toaok.fragment;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dissertation.toaok.activity.BBMSReaderInfoActivity;
import com.dissertation.toaok.activity.R;
import com.dissertation.toaok.base.BBMSBaseFragment;
import com.dissertation.toaok.model.ReaderInfo;
import com.dissertation.toaok.utils.BBMS;
import com.dissertation.toaok.utils.BBMSSharedPreferencesUtil;
import com.dissertation.toaok.utils.BBMSUtils;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by TOAOK on 2017/10/13.
 */

public class BBMSUserInfoFragment extends BBMSBaseFragment implements View.OnClickListener, BBMS.Callback {

    private TextView mTitle;

    //用户信息
    private ImageView mUserImage;
    private TextView mUserName;
    private TextView mUserAccount;

    //借阅信息
    private LinearLayout mBorrow;
    private Button mWaitPayment;
    private Button mWaitBorrow;
    private Button mHaveBorrow;
    private Button mWaitReturn;
    private RecyclerView mBorrowRecyclerView;
    private BorrowAdapter mAdapter;


    //recycler数据源
    private List<JSONObject> mWaitPaymentData = new LinkedList<>();
    private List<JSONObject> mWaitBorrowData = new LinkedList<>();
    private List<JSONObject> mHaveBorrowData = new LinkedList<>();
    private List<JSONObject> mWaitReturnData = new LinkedList<>();

    public static BBMSUserInfoFragment newInstance() {
        BBMSUserInfoFragment fragment = new BBMSUserInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWaitPaymentData.clear();
        mWaitBorrowData.clear();
        mHaveBorrowData.clear();
        mWaitReturnData.clear();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        Typeface typeface = BBMSUtils.getTypeface(getContext(), "fonts/icomoon.ttf");

        //设置标题
        mTitle = view.findViewById(R.id.title);
        Paint paint = mTitle.getPaint();
        paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        mTitle.setText("用户");

        //用户信息
        mUserImage = view.findViewById(R.id.user_image);
        mUserName = view.findViewById(R.id.user_name);
        mUserAccount = view.findViewById(R.id.user_account);
        String account = BBMSSharedPreferencesUtil.getInstance().getAccount().getAccount();
        ReaderInfo readerInfo = BBMSSharedPreferencesUtil.getInstance().getReaderInfo();
        String name = "";
        if (readerInfo == null) {
            name = account;
        } else {
            name = readerInfo.getReaderName();
        }
        mUserAccount.setText(account);
        mUserName.setText(name);
        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(BBMSReaderInfoActivity.newIntent(getContext()));
            }
        });

        //借阅信息
        mBorrow = view.findViewById(R.id.user_borrow);
        //设置附件
        TextView accessory = view.findViewById(R.id.accessory);
        accessory.setText("\ue905");
        accessory.setTypeface(typeface);

        mWaitPayment = view.findViewById(R.id.wait_payment);
        mWaitPaymentData.add(new JSONObject(BBMSUtils.parseMap("status", ((TextView) mWaitPayment).getText())));
        mWaitPayment.setOnClickListener(this);

        mWaitBorrow = view.findViewById(R.id.wait_borrow);
        mWaitBorrowData.add(new JSONObject(BBMSUtils.parseMap("status", ((TextView) mWaitBorrow).getText())));
        mWaitBorrow.setOnClickListener(this);

        mHaveBorrow = view.findViewById(R.id.have_borrow);
        mHaveBorrowData.add(new JSONObject(BBMSUtils.parseMap("status", ((TextView) mHaveBorrow).getText())));
        mHaveBorrow.setOnClickListener(this);

        mWaitReturn = view.findViewById(R.id.wait_return);
        mWaitReturnData.add(new JSONObject(BBMSUtils.parseMap("status", ((TextView) mWaitReturn).getText())));
        mWaitReturn.setOnClickListener(this);

        //实例化RecyclerView控件，并设置样式和适配器
        mBorrowRecyclerView = view.findViewById(R.id.user_borrow_list);
        mBorrowRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBorrowRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        mAdapter = new BorrowAdapter();
        mBorrowRecyclerView.setAdapter(mAdapter);
        mAdapter.setDataSet(mWaitPaymentData);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wait_payment:
                mAdapter.setDataSet(mWaitPaymentData);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.wait_borrow:
                mAdapter.setDataSet(mWaitBorrowData);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.have_borrow:
                mAdapter.setDataSet(mHaveBorrowData);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.wait_return:
                mAdapter.setDataSet(mWaitReturnData);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void callback(String response) {

    }

    class BorrowAdapter extends RecyclerView.Adapter {

        private List<JSONObject> dataSet;

        public void setDataSet(List dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (getItemCount() > 1) {
                View item = inflater.inflate(R.layout.test, parent, false);
                return new ItemViewHolder(item);
            } else {
                TextView item = new TextView(parent.getContext());
                item.setGravity(Gravity.CENTER);
                item.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, BBMSUtils.dip2px(22)));
                return new ViewHolder(item);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemCount() > 1) {

            } else {
                ((ViewHolder) holder).bindStatus();
            }
        }

        @Override
        public int getItemCount() {
            int count = 1;
            if (dataSet != null && dataSet.size() > 0) {
                count = dataSet.size();
            }
            return count;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            public ItemViewHolder(View itemView) {
                super(itemView);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView status;

            public ViewHolder(View itemView) {
                super(itemView);
                status = (TextView) itemView;
            }

            public void bindStatus() {
                status.setText("你还没有" + dataSet.get(0).optString("status") + "的书籍");
            }
        }
    }


}
