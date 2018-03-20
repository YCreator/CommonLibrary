package com.lib.imagelib.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.lib.imagelib.config.SingleConfig;
import com.lib.imagelib.utils.DownLoadImageService;

/**
 * Created by yzd on 2018/3/17 0017.
 */

public interface ILoader {

    void init(Context context, int cacheSizeInM, int memoryMode, boolean isInternalCD);

    void request(SingleConfig config);

    void pause();

    void resume();

    void clearDiskCache();

    void clearMomoryCache(View view);

    void clearMomory();

    boolean  isCached(String url);

    void trimMemory(int level);

    void clearAllMemoryCaches();

    Bitmap loadBitmap(Context context, String url);

    void saveImageIntoGallery(DownLoadImageService downLoadImageService);

}
