package com.frame.core.base;

import android.content.Context;

import com.frame.core.BaseApplication;
import com.frame.core.interf.presenter.BasePresenter;
import com.frame.core.interf.view.BaseMvpView;
import com.frame.core.rx.Lifeful;

/**
 * mvp中presenter的抽象类
 * Created by yzd on 2016/8/29.
 */
public abstract class BaseMvpPresenter<T extends BaseMvpView> implements BasePresenter {

    private Context mContext;
    private T view;
    private Lifeful lifeful;

    public BaseMvpPresenter(T view) {
        this.view = view;
    }

    @Deprecated
    public BaseMvpPresenter(Context mContext, T view) {
        this.mContext = mContext;
        this.view = view;
    }

    public BaseMvpPresenter(T view, Lifeful lifeful) {
        this.view = view;
        this.lifeful = lifeful;
    }

    @Deprecated
    public BaseMvpPresenter(Context mContext, T view, Lifeful lifeful) {
        this.mContext = mContext;
        this.view = view;
        this.lifeful = lifeful;
    }

    @Override
    public void initPage() {
        getView().initView();
        getView().initListener();
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

    public Lifeful getLifeful() {
        return lifeful;
    }
}
