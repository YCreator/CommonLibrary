package com.frame.core.net.download;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by yzd on 2017/5/11.
 */

public class DownLoadSubscriber<T> implements Observer<T> {
    private ProgressCallBack<T> fileCallBack;

    public DownLoadSubscriber(ProgressCallBack<T> fileCallBack) {
        this.fileCallBack = fileCallBack;
    }

    @Override
    public void onError(Throwable e) {
        if (fileCallBack != null)
            fileCallBack.onError(e);
    }

    @Override
    public void onComplete() {
        if (fileCallBack != null)
            fileCallBack.onCompleted();
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (fileCallBack != null)
            fileCallBack.onStart();
    }

    @Override
    public void onNext(T t) {
        if (fileCallBack != null)
            fileCallBack.onSuccess(t);
    }
}