package com.lib.imagelib.pub;

import android.widget.ImageView;

/**
 * Created by yzd on 2018/3/16 0016.
 */

public interface Pub {

    void initOption();

    void load(String url, ImageView mView);

    void load(String url, ImageView mView, ImageOption option);

    void load(String url, ImageView mView, ImagePubLoadingListener listener);

    void load(String url, ImageView mView, ImageOption option, ImagePubLoadingListener listener);
}
