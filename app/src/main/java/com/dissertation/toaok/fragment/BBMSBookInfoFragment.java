package com.dissertation.toaok.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dissertation.toaok.activity.BBMSLoginActivity;
import com.dissertation.toaok.activity.R;
import com.dissertation.toaok.base.BBMSBaseFragment;
import com.dissertation.toaok.model.Account;
import com.dissertation.toaok.model.BookInfo;
import com.dissertation.toaok.model.Subscribe;
import com.dissertation.toaok.utils.BBMS;
import com.dissertation.toaok.utils.BBMSSharedPreferencesUtil;
import com.dissertation.toaok.utils.BBMSUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by TOAOK on 2017/9/21.
 */

public class BBMSBookInfoFragment extends BBMSBaseFragment implements BBMS.Callback {
    private static final String ARG_BOOK_INFO = "book_id";

    private TextView  mBack;

    private ImageView mBookCover;
    private TextView mBookAuthor;
    private TextView mBookClassify;
    private TextView mBookCnum;
    private TextView mBookISBN;
    private TextView mBookName;
    private TextView mBookPrice;
    private TextView mBookPublish;
    private TextView mBookSnum;
    private TextView mBookSummary;

    private Button mSubscribe;
    private Button mReturn;
    private BookInfo bookInfo;

    private boolean isSubacribe = false;//书本状态

    public static BBMSBookInfoFragment newInstance(BookInfo bookInfo) {
        BBMSBookInfoFragment fragment = new BBMSBookInfoFragment();
        Log.i("status", "BBMSBookInfoFragment hsa bean created!");
        if (bookInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_BOOK_INFO, bookInfo);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookinfo, container, false);


        mBack=view.findViewById(R.id.info_book_back);
        mBack.setText("\ue90b");
        mBack.setTypeface(BBMSUtils.getTypeface(getContext(),"fonts/icomoon.ttf"));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        //获取bookinfo
        bookInfo = getArgument();

        mBookAuthor = view.findViewById(R.id.info_book_author);
        mBookClassify = view.findViewById(R.id.info_book_classify);
        mBookCnum = view.findViewById(R.id.info_book_cnum);
        mBookCover = view.findViewById(R.id.info_book_cover);
        mBookISBN = view.findViewById(R.id.info_book_ISBN);
        mBookName = view.findViewById(R.id.info_book_name);
        mBookPrice = view.findViewById(R.id.info_book_price);
        mBookPublish = view.findViewById(R.id.info_book_publish);
        mBookSnum = view.findViewById(R.id.info_book_snum);
        mBookSummary = view.findViewById(R.id.info_book_sunmmary);
        mSubscribe = view.findViewById(R.id.info_book_subscribe);
        mReturn = view.findViewById(R.id.info_book_return);


        Account account = BBMSSharedPreferencesUtil.getInstance().getAccount();
        if (account != null) {
            BBMS.with(getContext()).send("AccountServlet", "querySubscribe", account.getAccountId(),BBMSBookInfoFragment.this, true);
        }

        if (bookInfo != null) {
            mBookAuthor.setText("作者：" + bookInfo.getBookAuthor());
            mBookClassify.setText("分类号：" + bookInfo.getBookClassify());
            mBookCnum.setText("库存量：" + bookInfo.getBookCnum());
            Glide.with(getActivity()).load(bookInfo.getBookCover()).into(mBookCover);
            mBookISBN.setText("ISBN：" + bookInfo.getBookIsbn());
            mBookName.setText(bookInfo.getBookName());
            Paint paint = mBookName.getPaint();
            paint.setFakeBoldText(true);
            mBookPrice.setText("价格：" + bookInfo.getBookPrice());
            mBookPublish.setText("出版社：" + bookInfo.getBookPublish());
            mBookSnum.setText("复本量：" + bookInfo.getBookSnum());
            mBookSummary.setText("简介：" + bookInfo.getBookSummary());

            mSubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Subscribe subscribe = BBMSUtils.createSubscribe(bookInfo);
                    if (subscribe != null) {
                        BBMS.with(getContext()).send("AccountServlet", "subscribe", subscribe, BBMSBookInfoFragment.this, false);
                    } else {
                        startActivity(BBMSLoginActivity.newIntent(getContext(),false));
                    }
                }
            });
            mReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Subscribe subscribe = BBMSUtils.createSubscribe(bookInfo);
                    if (subscribe != null) {
                        BBMS.with(getContext()).send("AccountServlet", "unsubscribe", subscribe, BBMSBookInfoFragment.this, false);
                    } else {
                        startActivity(BBMSLoginActivity.newIntent(getContext(),false));
                    }
                }
            });
        }
        return view;
    }

    private BookInfo getArgument() {
        Bundle args = this.getArguments();
        if (args != null) {
            return (BookInfo) args.get(ARG_BOOK_INFO);
        } else return null;

    }


    @Override
    public void callback(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                analyseResult(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void analyseResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        if(result.startsWith("{\"subscribe\":")){
            List<BookInfo> subscribes=BBMSUtils.json2Object(jsonObject.optJSONArray("subscribe"));
            for(BookInfo bookInfo:subscribes){
                if(bookInfo.getBookIsbn().equals(this.bookInfo.getBookIsbn())){
                    mSubscribe.setVisibility(View.GONE);
                    mReturn.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        else if (result.startsWith("{\"append_status\":")) {
            subscribeBook(jsonObject.optInt("append_status"));
        } else if (result.startsWith("{\"delete_status\":")) {
            unsubscribeBook(jsonObject.optInt("delete_status"));
        }
    }

    //订阅
    private void subscribeBook(int status) {
        switch (status) {
            case -1:
                BBMSUtils.showMessage(getContext(), "该图书信息正在完善中...");
                break;
            case 0:
                BBMSUtils.showMessage(getContext(), "图书已订阅...");
            case 1:
                BBMSUtils.showMessage(getContext(), "订阅成功...");
                mSubscribe.setVisibility(View.GONE);
                mReturn.setVisibility(View.VISIBLE);
                break;
        }
    }

    //退订
    private void unsubscribeBook(int status) {
        switch (status) {
            case -1:
                BBMSUtils.showMessage(getContext(), "该图书信息正在完善中...");
                break;
            case 0:
                BBMSUtils.showMessage(getContext(), "图书已退订...");
            case 1:
                BBMSUtils.showMessage(getContext(), "退订成功...");
                mReturn.setVisibility(View.GONE);
                mSubscribe.setVisibility(View.VISIBLE);
                break;
        }
    }

}


