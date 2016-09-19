package com.frame.core.base;

import android.content.Context;

import com.frame.core.interf.presenter.BasePresenter;
import com.frame.core.interf.view.BaseMvpView;

/**
 * mvp中presenter的抽象类
 * Created by yzd on 2016/8/29.
 */
public abstract class BaseMvpPresenter<T extends BaseMvpView> implements BasePresenter {

    private Context mContext;
    private T view;

    public BaseMvpPresenter(Context mContext, T view) {
        this.mContext = mContext;
        this.view = view;
    }

    @Override
    public void initPage() {
        getView().initPageView();
        getView().initPageViewListener();
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
}
