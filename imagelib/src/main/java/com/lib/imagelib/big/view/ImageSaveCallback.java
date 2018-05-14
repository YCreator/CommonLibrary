package com.lib.imagelib.big.view;

/**
 * Created by yzd on 2018/5/10 0010.
 */

public interface ImageSaveCallback {
    void onSuccess(String uri);

    void onFail(Throwable t);
}
