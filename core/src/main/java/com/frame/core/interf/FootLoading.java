package com.frame.core.interf;

/**
 * Created by yzd on 2016/7/31.
 */
public interface FootLoading {

    /**
     * 第一页加载数据完成时调用的方法
     * @param isCanLoadMore
     */
    void setIndexLoadMoreState(boolean isCanLoadMore);

    /**
     * 上拉加载完成调用的方法
     * @param isCanLoadMore
     */
    void setLoadMoreState(boolean isCanLoadMore);

    /**
     * 加载数据调用的方法
     */
    void loadingMore();

    /**
     * 加载完毕
     */
    void completeLoading();

    /**
     * 没数据可加载
     */
    void noDataToLoad();

    /**
     * 无数据
     */
    void nothing();
}
