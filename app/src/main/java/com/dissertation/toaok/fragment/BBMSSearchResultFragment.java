package com.dissertation.toaok.fragment;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dissertation.toaok.activity.BBMSBookInfoActivity;
import com.dissertation.toaok.activity.BBMSSearchResultActivity;
import com.dissertation.toaok.activity.R;
import com.dissertation.toaok.model.BookInfo;
import com.dissertation.toaok.utils.BBMS;
import com.dissertation.toaok.utils.BBMSUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TOAOK on 2017/9/19.
 */

public class BBMSSearchResultFragment extends Fragment implements BBMS.Callback {


    private static final String ARG_SEARCH_STRING = "search_string";
    private static final String ARG_SEARCH_STATUS = "search_status";
    private static final String TAG = "SearchString";

    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private BookInfoAdapter mAdapter;
    private int page = 0;
    private int totalPage = -1;
    private boolean haveMore = false;
    private List mData = new ArrayList();

    public static BBMSSearchResultFragment newInstance(boolean searchStatus) {
        BBMSSearchResultFragment fragment = new BBMSSearchResultFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SEARCH_STATUS, searchStatus);
        fragment.setArguments(args);
        return fragment;
    }

    public static BBMSSearchResultFragment newInstance(String searchString) {
        BBMSSearchResultFragment fragment = newInstance(false);
        Log.i("status", "BBMSSearchResultFragment hsa bean created!");
        if (searchString != null) {
            fragment.getArguments().putSerializable(ARG_SEARCH_STRING, searchString);
        }
        return fragment;
    }

    private void updateUI(String results) throws JSONException {

        if (!TextUtils.isEmpty(results)) {
            JSONObject jsonObject = new JSONObject(results);
            //计算此次查询所得结果的页数
            if (totalPage == -1) {
                int searchTotal = jsonObject.optInt("searchTotal");
                totalPage = searchTotal / 10;
                if (searchTotal % 10 != 0) {
                    totalPage += 1;
                }
            }
            //没有更多数据了
            if (totalPage <= page) {
                haveMore = false;
            } else {
                haveMore = true;
            }
            if (totalPage > 0) {
                JSONArray jsonArray = jsonObject.optJSONArray("BookInfo");
                mData.addAll(BBMSUtils.json2Object(jsonArray));
                if (!mData.isEmpty()) {
                    mAdapter.setDataSet(mData);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSearchStatus()) {
            haveMore = false;
        }
    }

    protected boolean getSearchStatus() {
        return getArguments().getBoolean(ARG_SEARCH_STATUS);
    }

    protected String getArgument() {
        return (String) getArguments().get(ARG_SEARCH_STRING);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frament_search_result_list, container, false);

        //初始化查询控件
        Typeface typeface = BBMSUtils.getTypeface(getContext(), "fonts/icomoon.ttf");
        TextView back = view.findViewById(R.id.search_back);
        back.setText("\ue901");
        back.setTypeface(typeface);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        back.setTextColor(BBMSUtils.newSelector(0xffffffff, 0x33ffffff));
        final EditText searchContext = view.findViewById(R.id.search_content);

        searchContext.setFocusable(true);
        searchContext.requestFocus();
        if(!getSearchStatus()){
            searchContext.setText(getArgument());
        }
        searchContext.setBackground(BBMSUtils.newDrawable(getResources().getColor(R.color.colorWhiteBg, null), 0, 0, 1));
        searchContext.setHint(BBMSUtils.setFontColorSpannableStringBuilder(getResources().getString(R.string.search), 0, getResources().getColor(R.color.colorHint, null)));
        searchContext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == 0) {
                    submit(searchContext.getText().toString());
                    searchContext.clearFocus();
                    return true;
                }
                return false;
            }
        });
        TextView submit = view.findViewById(R.id.search_status);
        submit.setText("\ue909");
        submit.setTypeface(typeface);
        submit.setTextColor(BBMSUtils.newSelector(0xffffffff, 0x33ffffff));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(searchContext.getText().toString());
                searchContext.clearFocus();
            }
        });

        //初始化显示控件
        mProgressBar = view.findViewById(R.id.search_progressBar);
        mRecyclerView = view.findViewById(R.id.search_result_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new BookInfoAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        String searchString = getArgument();
        if (!TextUtils.isEmpty(searchString)) {
            BBMS.with(getContext()).send("BookServlet", "search", searchString, ++page, this, false);
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

        return view;
    }

    private void submit(String s) {
        if (!TextUtils.isEmpty(s)) {
            startActivity(BBMSSearchResultActivity.newIntent(getContext(), s, false));
            getActivity().finish();
        }
    }

    @Override
    public void callback(String response) {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(response)) {
            try {
                updateUI(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            mAdapter.notifyItemRemoved(mAdapter.getItemCount() - 1);
            BBMSUtils.showMessage(getContext(), "未访问到数据");
        }

    }

    class BookInfoAdapter extends RecyclerView.Adapter {

        private static final int FOOTVIEW_ITEM = -1;

        private List<BookInfo> dataSet;


        public void setDataSet(List dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if (viewType == FOOTVIEW_ITEM) {
                View footView = layoutInflater.inflate(R.layout.list_item_footview, parent, false);
                return new FootItemViewHolder(footView);
            } else {
                View normalItem = layoutInflater.inflate(R.layout.list_item_book_info, parent, false);
                return new BookInfoViewHolder(normalItem);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (position == getItemCount() - 1 && haveMore) {
                //mDataSource.refresh();
                BBMS.with(getContext()).send("BookServlet", "search", getArgument(), ++page, BBMSSearchResultFragment.this, false);
            } else if (position <= getItemCount() - 1) {
                BookInfo bookInfo = dataSet.get(position);
                ((BookInfoViewHolder) holder).bindBookInfo(bookInfo);
            }
        }

        @Override
        public int getItemCount() {
            int itemCount = 0;
            if (dataSet != null) {
                itemCount = dataSet.size();
            }
            return haveMore ? itemCount + 1 : itemCount;
        }

       /* public void isHasFootView(boolean hasFootView) {
            this.hasFootView=hasFootView;
        }*/

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1 && haveMore)
                return FOOTVIEW_ITEM;
            return super.getItemViewType(position);
        }

        class BookInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView mBookCover;
            private TextView mBookName;
            private TextView mBookAuthor;
            private TextView mBookSummary;

            private BookInfo mBookInfo;

            public BookInfoViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                mBookCover = itemView.findViewById(R.id.item_book_cover);
                mBookName = itemView.findViewById(R.id.item_book_name);
                mBookAuthor = itemView.findViewById(R.id.item_book_author);
                mBookSummary = itemView.findViewById(R.id.item_book_summary);
            }

            public void bindBookInfo(BookInfo bookInfo) {
                Glide.with(getActivity()).load(bookInfo.getBookCover()).into(mBookCover);
                this.mBookInfo = bookInfo;
                mBookName.setText(bookInfo.getBookName());
                TextPaint paint = mBookName.getPaint();
                paint.setFakeBoldText(true);
                mBookAuthor.setText(bookInfo.getBookAuthor());
                mBookSummary.setText(bookInfo.getBookSummary());
            }

            @Override
            public void onClick(View view) {
                startActivity(BBMSBookInfoActivity.newIntent(getContext(), mBookInfo));
            }
        }

        class FootItemViewHolder extends RecyclerView.ViewHolder {

            public FootItemViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
