package com.lib.imagelib.utils;

/**
 * Created by yzd on 2018/5/10 0010.
 */

public class ThreadPoolFactory {
    static ThreadPoolProxy	mNormalPool;
    static ThreadPoolProxy	mDownLoadPool;

    /**得到一个普通的线程池*/
    public static ThreadPoolProxy getNormalPool() {
        if (mNormalPool == null) {
            synchronized (ThreadPoolProxy.class) {
                if (mNormalPool == null) {
                    mNormalPool = new ThreadPoolProxy(5, 5, 3000);
                }
            }
        }
        return mNormalPool;
    }
    /**得到一个下载的线程池*/
    public static ThreadPoolProxy getDownLoadPool() {
        if (mDownLoadPool == null) {
            synchronized (ThreadPoolProxy.class) {
                if (mDownLoadPool == null) {
                    mDownLoadPool = new ThreadPoolProxy(3, 6, 6000);
                }
            }
        }
        return mDownLoadPool;
    }
}
