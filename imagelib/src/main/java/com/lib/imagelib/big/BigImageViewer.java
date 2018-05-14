package com.lib.imagelib.big;

import android.net.Uri;

import com.lib.imagelib.big.loader.BigLoader;

/**
 * Created by yzd on 2018/5/10 0010.
 */

public final class BigImageViewer {
    private static volatile BigImageViewer sInstance;

    private final BigLoader mImageLoader;

    private BigImageViewer(BigLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public static void initialize(BigLoader imageLoader) {
        sInstance = new BigImageViewer(imageLoader);
    }

    public static BigLoader imageLoader() {
        if (sInstance == null) {
            throw new IllegalStateException("You must initialize BigImageViewer before use it!");
        }
        return sInstance.mImageLoader;
    }

    public static void prefetch(Uri... uris) {
        if (uris == null) {
            return;
        }

        BigLoader imageLoader = imageLoader();
        for (Uri uri : uris) {
            imageLoader.prefetch(uri);
        }
    }
}
