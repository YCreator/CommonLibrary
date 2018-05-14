package com.lib.imagelib.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.lib.imagelib.config.SingleConfig;
import com.lib.imagelib.utils.DownLoadImageService;

import java.io.File;

/**
 * Created by yzd on 2018/3/17 0017.
 */

public interface ILoader {

    void init(Context context, int cacheSizeInM, int memoryMode, boolean isInternalCD);

    void request(SingleConfig config);

    void pause();

    void resume();

    void clearMomory();

    void clearAllMemoryCaches();

    void clearMomoryCache(View view);

    void clearMomoryCache(String url);

    void clearDiskCache();

    void clearCacheByUrl(String url);

    long getCacheSize();

    File getFileFromDiskCache(String url);

    void getFileFromDiskCache(String url,FileGetter getter);

    boolean isCached(String url);

    void trimMemory(int level);

    void onLowMemory();

    Bitmap loadBitmap(Context context, String url);

    void download(String url,FileGetter getter);

    void saveImageIntoGallery(DownLoadImageService downLoadImageService);

}
