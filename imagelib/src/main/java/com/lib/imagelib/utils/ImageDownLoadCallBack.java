package com.lib.imagelib.utils;

import android.graphics.Bitmap;

/**
 * Created by yzd on 2018/3/19 0019.
 */

public interface ImageDownLoadCallBack {

    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}
