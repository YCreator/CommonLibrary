package com.frame.core.adapter;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.frame.core.R;
import com.frame.core.interf.FootLoading;
import com.frame.core.util.PixelUtil;

/**
 * Created by yzd on 2016/7/16.
 */
public class LvLoadItem implements FootLoading {

    private View itemView;
    private TextView tv;

    private boolean isCanLoading = true;
    private int gloaH;
    private AbsListView.LayoutParams params;

    public LvLoadItem(Context context) {
        itemView = LayoutInflater.from(context).inflate(R.layout.loading_more, null);
        gloaH = PixelUtil.getScreenH();
        params = new AbsListView.LayoutParams(PixelUtil.getScreenW(),gloaH / 12);
        itemView.setLayoutParams(params);
        tv = (TextView) itemView.findViewById(com.frame.core.R.id.loading_tv);
    }

    public View getItemView() {
        return itemView;
    }

    public void bindListviewFoot(ListView listView) {
        listView.addFooterView(itemView);
    }

    public void setBackground(@ColorInt  int colorInt) {
        itemView.setBackgroundColor(colorInt);
    }

    public void setCanLoading(boolean canLoading) {
        isCanLoading = canLoading;
    }

    public boolean isCanLoading() {
        return isCanLoading;
    }

    /**
     * 第一页加载数据完成时调用的方法
     * @param isCanLoadMore
     */
    @Override
    public void setIndexLoadMoreState(boolean isCanLoadMore) {
        this.isCanLoading = isCanLoadMore;
        if (itemView != null) {
            if (isCanLoadMore) {
                params.height = gloaH / 12;
                completeLoading();
            } else {
                params.height = gloaH / 2;
                //params.height = PixelUtil.getScreenMetrics(itemView.getContext()).y;
                itemView.setLayoutParams(params);
                nothing();
            }
        }
    }

    /**
     * 上拉加载完成调用的方法
     * @param isCanLoadMore
     */
    @Override
    public void setLoadMoreState(boolean isCanLoadMore) {
        this.isCanLoading = isCanLoadMore;
        if (itemView != null) {
            if (isCanLoadMore) {
                completeLoading();
            } else {
                noDataToLoad();
            }
        }
    }

    /**
     * 加载数据调用的方法
     */
    @Override
    public void loadingMore() {
        if (itemView != null) {
            tv.setText("加载中...");
        }
    }

    @Override
    public void completeLoading() {
        tv.setText("上拉加载");
    }

    @Override
    public void noDataToLoad() {
        tv.setText("--加载完毕--");
    }

    @Override
    public void nothing() {
        nothing("--没有数据--");
    }

    public void nothing(String msg) {
        tv.setText(msg);
    }
}
