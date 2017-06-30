package com.frame.core.base;

import android.content.Context;

import com.frame.core.BaseApplication;
import com.frame.core.rx.Lifeful;

/**
 * Created by yzd on 2016/5/26.
 *
 */
public abstract class AbstractPresenter<T>{

    private Context mContext;
    private T view;
    private Lifeful lifeful;

    public AbstractPresenter(T view) {
        this.view = view;
    }

    @Deprecated
    public AbstractPresenter(Context mContext, T view) {
        this.mContext = mContext;
        this.view = view;
    }

    public AbstractPresenter(T view, Lifeful lifeful) {
        this.view = view;
        this.lifeful = lifeful;
    }

    @Deprecated
    public AbstractPresenter(Context mContext, T view, Lifeful lifeful) {
        this.mContext = mContext;
        this.view = view;
        this.lifeful = lifeful;
    }

    @Deprecated
    public Context getContext() {
        return mContext;
    }

    public Context getApplicationContext() {
        return BaseApplication.get_context();
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

    public Lifeful getLifeful() {
        return lifeful;
    }

}
