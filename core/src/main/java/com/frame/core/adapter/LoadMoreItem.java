package com.frame.core.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.core.R;
import com.frame.core.interf.AdapterItem;
import com.frame.core.util.PixelUtil;


/**
 * Created by Administrator on 2016/1/3.
 */
public class LoadMoreItem implements AdapterItem {

    private View itemView;
    private TextView tv;

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
    public void setIndexLoadMoreState(boolean isCanLoadMore) {
        this.isCanLoading = isCanLoadMore;
        if (itemView != null) {
            if (isCanLoadMore) {
                completeLoading();
            } else {
                ViewGroup.LayoutParams params = itemView.getLayoutParams();
                params.height = PixelUtil.getScreenMetrics(itemView.getContext()).y;
                nothing();
            }
        }
    }

    /**
     * 上拉加载完成调用的方法
     * @param isCanLoadMore
     */
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
    public void loadingMore() {
        if (itemView != null) {
            tv.setText("加载中...");
        }
    }

    private void completeLoading() {
        tv.setText("上拉加载");
    }

    private void noDataToLoad() {
        tv.setText("--加载完毕--");
    }

    private void nothing() {
        tv.setText("--没有数据--");
    }

}
