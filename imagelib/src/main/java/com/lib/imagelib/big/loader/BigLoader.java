package com.lib.imagelib.big.loader;

import android.net.Uri;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.view.View;

import com.lib.imagelib.big.view.BigImageView;

import java.io.File;

/**
 * Created by yzd on 2018/5/10 0010.
 */

public interface BigLoader {
    void loadImage(Uri uri);

    View showThumbnail(BigImageView parent, Uri thumbnail, int scaleType);

    void prefetch(Uri uri);

    interface Callback {
        @UiThread
        void onCacheHit(File image);

        @WorkerThread
        void onCacheMiss(File image);

        @WorkerThread
        void onStart();

        @WorkerThread
        void onProgress(int progress);

        @WorkerThread
        void onFail();

        @WorkerThread
        void onFinish();
    }
}
