package com.frame.core.net.okhttp;

import com.frame.core.entity.JsonEntity;

/**
 * Created by yzd on 2017/10/16 0016.
 */

public interface DCallback<T extends JsonEntity> {

    void onStart();

    void onEntity(T value);

    void onError(Throwable e);

    void onComplete();
}
