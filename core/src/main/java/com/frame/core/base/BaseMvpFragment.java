package com.frame.core.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *
 * Created by yzd on 2016/8/29.
 */
public abstract class BaseMvpFragment<T extends BaseMvpPresenter> extends BaseFragment {

    protected T presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = initPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        super.init();
        presenter.init();
    }

    @Override
    public void initPageView() {
        presenter.initPage();
    }

    @Override
    public void initPageViewListener() {

    }

    @Override
    protected void process(Bundle savedInstanceState) {
        super.process(savedInstanceState);
        presenter.process(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.pause();
    }

    /**
     * 返回主持人
     */
    protected abstract @NonNull T initPresenter();
    
}
