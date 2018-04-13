package com.lib.imagelib.pub;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by yzd on 2018/3/16 0016.
 */

public class GlidePubImpl implements Pub {

    @Override
    public void initOption() {

    }

    @Override
    public void load(String url, ImageView mView) {
        Glide.with(mView).load(url).into(mView);
    }

    @Override
    public void load(String url, ImageView mView, ImageOption option) {
        RequestOptions options = new RequestOptions()
                .error(option.getErrorShow())
                .placeholder(option.getLoadingShow());
        Glide.with(mView).load(url).apply(options).into(mView);
    }

    @Override
    public void load(String url, ImageView mView, ImagePubLoadingListener listener) {
        Glide.with(mView).load(url).into(mView);
    }

    @Override
    public void load(String url, ImageView mView, ImageOption option, ImagePubLoadingListener listener) {
        RequestOptions options = new RequestOptions()
                .error(option.getErrorShow())
                .placeholder(option.getLoadingShow());
        Glide.with(mView).load(url).apply(options).into(mView);
    }

}
