package com.dissertation.toaok.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dissertation.toaok.activity.R;
import com.dissertation.toaok.utils.BBMSUtils;

/**
 * Created by TOAOK on 2017/11/2.
 */

public abstract class BBMSTableAdapter extends RecyclerView.Adapter {


    public static final int HEADERTYPE = 1;
    public static final int NOMARLTYPE = 0;
    public static final int FOOTERTYPE = -1;

    private int[] sectiones;

    //设置内边距属性
    public interface ItemPadding {
        int getItemPaddingLeft();

        int getItemPaddingRight();
    }

    public BBMSTableAdapter() {
        super();
        this.registerAdapterDataObserver(new DataObserver());
    }

//    public int getPosition(int section,int row){
//        int index=0;
//        int c=0;
//        while (c<section){
//            index+=sectiones[c]+2;
//            c++;
//        }
//        index++;
//
//        return index+row;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(onCreateView(parent, viewType));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //绑定数据
        holder.itemView.setId(position);

        ViewItem viewItem = (ViewItem) holder.itemView;

        int index = -1;
        int sectionLength = sectiones.length;
        int c = 0;
        while (c < sectionLength) {
            index++;
            if (index == position) {
                onBindHeaderView(viewItem.SourecView, c);
                viewItem.draw = -1;
                viewItem.start = viewItem.end = 0;
                //CheckDraw(viewItem);
                if (viewItem.SourecView instanceof TextView) {
                    if (sectiones[c] == 0) {
                        viewItem.draw = 0;
                    }
                    TextView textView = (TextView) viewItem.SourecView;
                    if (TextUtils.isEmpty(textView.getText())) {
                        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, defaultStart / 2));
                    } else {
                        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }
                return;
            }
            int scount = sectiones[c];
            index += scount;
            if (position <= index) {
                int row = position - index + scount - 1;
                if (row + 1 == scount) {
                    viewItem.draw = 0;
                } else {
                    viewItem.draw = -1;
                    viewItem.start = defaultStart;
                }
                onBindItemView(viewItem.SourecView, c, row);
                return;
            }
            index++;
            if (index == position) {
                viewItem.draw = 1;
                viewItem.end = viewItem.start = 0;
                onBindFootView(viewItem.SourecView, c);
                if (viewItem.SourecView instanceof TextView) {
                    if (sectiones[c] == 0) {
                        viewItem.draw = 0;
                    }
                    TextView textView = (TextView) viewItem.SourecView;
                    if (TextUtils.isEmpty(textView.getText())) {
                        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, defaultStart));
                    } else {
                        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }
                return;
            }
            c++;
        }

    }

    public void onBindItemView(View view, int section, int row) {
    }

    public void onBindFootView(View view, int section) {
    }

    public void onBindHeaderView(View view, int section) {

    }


    @Override
    public int getItemCount() {
        if (sectiones == null) {
            reloadesctiones();
        }
        int sectionLength = sectiones.length;
        int conunt = sectionLength * 2;
        for (int i = 0; i < sectionLength; ++i) {
            conunt += sectiones[i];
        }
        return conunt;
    }

    @Override
    public int getItemViewType(int position) {
        int index = -1;
        int sectionLength = sectiones.length;
        int c = 0;//循环变量
        while (c < sectionLength) {
            index++;
            if (index == position) {
                return getHeaderViewType(c);
            }
            int scount = sectiones[c];
            index += scount;
            if (position <= index) {
                return getItemViewType(c, position - index + scount - 1);
            }
            index++;
            if (index == position) {
                return getFootViewType(c);
            }
            c++;
        }


        return super.getItemViewType(position);
    }

    public int getItemViewType(int section, int row) {
        return NOMARLTYPE;
    }


    public int getFootViewType(int section) {
        return FOOTERTYPE;
    }

    public int getHeaderViewType(int section) {
        return HEADERTYPE;
    }


    private void CheckDraw(ViewItem viewItem) {
        if (viewItem.SourecView instanceof ItemPadding) {

        }


    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(createViewItem(itemView));
        }
    }

    public static final int defaultStart = BBMSUtils.dip2px(15);

    class ViewItem extends LinearLayout {


        public ViewItem(Context context) {
            super(context);
        }

        private int start, end, draw;
        public View SourecView;


        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            if (draw != 0) {
                Paint p = new Paint();
                p.setColor(0xffd9d9d9);
                p.setStrokeWidth(1f);
                int width = this.getWidth();
                int height = this.getHeight();
                if (draw > 0) {
                    canvas.drawLine(start, 0, width - end, 0, p);
                } else {
                    canvas.drawLine(start, height - 1, width - end, height - 1, p);
                }
            }
        }
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            reloadesctiones();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            reloadesctiones();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            reloadesctiones();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            reloadesctiones();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            reloadesctiones();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            reloadesctiones();
        }
    }

    private ViewItem createViewItem(View itemView) {

        ViewItem item = new ViewItem(itemView.getContext());
        item.SourecView = itemView;
        item.addView(itemView);
        item.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return item;
    }

    public View onCreateView(ViewGroup parent, int viewtype) {
        switch (viewtype) {
            case NOMARLTYPE:
            case FOOTERTYPE:
            case HEADERTYPE:
                TextView textView = new TextView(parent.getContext());
                textView.setTextColor(0xff888888);
                textView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.colorDivider));
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, BBMSUtils.dip2px(22)));
                return textView;
        }
        return null;
    }

    private void reloadesctiones() {
        sectiones = new int[this.getSectionCount()];
        for (int i = 0; i < sectiones.length; ++i) {
            sectiones[i] = this.getItemsCountOfSection(i);
        }
    }

    protected abstract int getItemsCountOfSection(int section);

    //获取section的个数
    public int getSectionCount() {
        return 1;
    }
}
