package com.frame.core.base;

import android.content.Context;

import com.frame.core.interf.view.BaseMvpView;

/**
 * Created by yzd on 2017/2/5 0005.
 */

public class AbsBasePresenter<T extends BaseMvpView> {

    private Context mContext;
    private T view;

    public AbsBasePresenter(Context mContext, T view) {
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
}
