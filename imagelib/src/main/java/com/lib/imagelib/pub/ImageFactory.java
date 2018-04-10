package com.lib.imagelib;

import android.widget.ImageView;

/**
 * Created by yzd on 2018/3/16 0016.
 */

public final class ImageFactory {

    private static ImageFactory mImgFac;
    private ImageLib imageLibType;
    private Pub imagePub;
    private String url;
    private ImageView mView;
    private ImageOption option;

    private ImageFactory() {
        if (ImagePub.mContext == null) {
            throw new IllegalArgumentException("ImagePub Context is null.");
        }
    }

    public static ImageFactory getInstance() {
        if (mImgFac == null) {
            mImgFac = new ImageFactory();
        }
        return mImgFac;
    }

    public ImageFactory init(ImageLib lib) {
        this.imageLibType = lib;
        switch (imageLibType) {
            case IMAGELOADER:
                imagePub = new GlidePubImpl();
                break;
            case GLIDE:
                imagePub = new ImageLoaderPubImpl();
                break;
        }
        return this;
    }

    public ImageFactory url(String url) {
        this.url = url;
        return this;
    }

    public ImageFactory view(ImageView view) {
        this.mView = view;
        return this;
    }

    public ImageFactory option(ImageOption option) {
        this.option = option;
        return this;
    }

    public void show() {
        if (url == null) {
            throw new IllegalArgumentException("url is null");
        }
        if (mView == null) {
            throw new IllegalArgumentException("view is null");
        }
        if (option == null) {
            imagePub.load(url, mView);
        } else {
            imagePub.load(url, mView, option);
        }
    }

    public void show(ImagePubLoadingListener listener) {
        if (url == null) {
            throw new IllegalArgumentException("url is null");
        }
        if (mView == null) {
            throw new IllegalArgumentException("view is null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener is null");
        }
        if (option == null) {
            imagePub.load(url, mView, listener);
        } else {
            imagePub.load(url, mView, option, listener);
        }

    }

    public ImageFactory initParam() {
        url = null;
        mView = null;
        option = null;
        return this;
    }

    public enum ImageLib {
        IMAGELOADER, GLIDE
    }
}
