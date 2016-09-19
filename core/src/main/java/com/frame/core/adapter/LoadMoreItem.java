package com.frame.core.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.core.R;
import com.frame.core.interf.AdapterItem;
import com.frame.core.interf.FootLoading;
import com.frame.core.util.PixelUtil;


/**
 * Created by Administrator on 2016/1/3.
 */
public class LoadMoreItem implements AdapterItem, FootLoading {

    private View itemView;
    private TextView tv;
    private int gloaH;

    private boolean isCanLoading = true;

    public void setCanLoading(boolean canLoading) {
        isCanLoading = canLoading;
    }

    public boolean isCanLoading() {
        return isCanLoading;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.loading_more;
    }

    @Override
    public void initItemViews(View itemView) {
        gloaH = PixelUtil.getScreenH();
        this.itemView = itemView ;
        tv = (TextView) itemView.findViewById(R.id.loading_tv);
        setIndexLoadMoreState(isCanLoading);
    }

    @Override
    public void onSetViews() {
        tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUpdateViews(Object model, int position) {

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
                ViewGroup.LayoutParams params = itemView.getLayoutParams();
                params.height = gloaH / 12;
                completeLoading();
            } else {
                ViewGroup.LayoutParams params = itemView.getLayoutParams();
                params.height = gloaH;
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
        tv.setText("--没有数据--");
    }

}