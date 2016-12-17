package com.frame.core.rx;

import com.frame.core.exception.ResponseException;
import com.frame.core.util.TLog;

/**
 *
 * Created by yzd on 2016/12/17 0017.
 */

public abstract class DefaultSubscriber<T> extends rx.Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ResponseException) {
            ResponseException exception = (ResponseException) e;
            TLog.i("sub_error_code", exception.getErrorCode()+"");

        }
        TLog.i("sub_error", e.getMessage());
    }
}
