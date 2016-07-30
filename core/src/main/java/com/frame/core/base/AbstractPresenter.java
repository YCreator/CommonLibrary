package com.frame.core.base;

import android.content.Context;

/**
 * Created by yzd on 2016/5/26.
 */
public abstract class AbstractPresenter<T>{

    private Context mContext;
    private T view;

    public AbstractPresenter(Context mContext, T view) {
        this.mContext = mContext;
        this.view = view;
    }

    public Context getContext() {
        return mContext;
    }

    public Context getApplicationContext() {
        return mContext.getApplicationContext();
    }

    public T getView() {
        return view;
    }

    public BaseAppCompatActivity getBaseActivity() {
        if (mContext != null && mContext instanceof BaseAppCompatActivity) {
            return (BaseAppCompatActivity) mContext;
        }
        return null;
    }

}
