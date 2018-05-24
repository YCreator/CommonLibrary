package com.frame.core.mvvm.base;

/**
 * Created by yzd on 2018/2/8 0008.
 */

public interface IBaseActivity {

    /**
     * 初始化界面传递参数
     */
    void initParam();
    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();
}
