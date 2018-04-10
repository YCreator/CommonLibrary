package com.lib.imagelib;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by yzd on 2018/3/17 0017.
 */

public interface ImagePubLoadingListener {

    void onLoadingStarted(String var1, ImageView var2);

    void onLoadingFailed(String var1, ImageView var2, ImageException var3);

    void onLoadingComplete(String var1, ImageView var2, Bitmap var3);

    void onLoadingCancelled(String var1, ImageView var2);
}
