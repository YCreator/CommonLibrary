package com.frame.core.base;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by yzd on 2018/5/14 0014.
 */

public abstract class BaseDialog extends Dialog implements IBaseDialog {

    private boolean hasView = false;
    private View mViewContent;

    public BaseDialog(@NonNull Context context) {
        this(context, false);
    }

    public BaseDialog(@NonNull Context context, boolean hasView) {
        super(context);
        this.hasView = hasView;
    }

    public BaseDialog(@NonNull Context context, int themeResId, boolean hasView) {
        super(context, themeResId);
        this.hasView = hasView;
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, boolean hasView) {
        super(context, cancelable, cancelListener);
        this.hasView = hasView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (hasView) {
            mViewContent = LayoutInflater.from(this.getContext()).inflate(initPageLayoutID(), null);
            setContentView(mViewContent);
            ButterKnife.bind(this, mViewContent);
        } else {
            setContentView(initPageLayoutID());
            ButterKnife.bind(this, this);
        }
        initView();
        initListener();
        process(savedInstanceState);
    }

    @Override
    public void show() {
        if (this.isShowing()) {
            return;
        }
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    public View getContentView() {
        if (!hasView) throw new IllegalArgumentException("hasView is false");
        return mViewContent;
    }


    @LayoutRes
    protected abstract int initPageLayoutID();
}
