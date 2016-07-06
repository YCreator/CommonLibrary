package com.frame.core.interf;

/**
 *Activity基类接口
 * Created by Administrator on 2015/12/2.
 */
public interface IBaseView {

    /**
     * 初始化控件布局
     */
    void initPageView();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化监听事件
     */
    void initPageViewListener();

}
