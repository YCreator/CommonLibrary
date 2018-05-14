package com.lib.imagelib.loader;

import android.graphics.Bitmap;

/**
 * Created by yzd on 2018/5/11 0011.
 */

public interface ImageListener {
    void onSuccess(String filePath, int width, int height, Bitmap bitmap, int bWidth, int bHeight);

    //void onSuccess(InputStream is);

    void onFail(Throwable e);
}
