package com.frame.core.mvvm.base;

/**
 * Created by yzd on 2018/2/8 0008.
 */

public interface IBaseViewModel {
    /**
     * View的界面创建时回调
     */
    void onCreate();

    /**
     * View的界面销毁时回调
     */
    void onDestroy();

    /**
     * 注册RxBus
     */
    void registerRxBus();
    /**
     * 移除RxBus
     */
    void removeRxBus();
}
