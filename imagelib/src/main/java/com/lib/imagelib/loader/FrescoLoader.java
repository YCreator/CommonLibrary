package com.lib.imagelib.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.lib.imagelib.config.Contants;
import com.lib.imagelib.config.SingleConfig;
import com.lib.imagelib.utils.DownLoadImageService;

/**
 * Created by yzd on 2018/3/19 0019.
 */

public class FrescoLoader implements ILoader {

    @Override
    public void init(Context context, int cacheSizeInM, int memoryMode, boolean isInternalCD) {
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryName(Contants.DEFAULT_DISK_CACHE_DIR)
                .setBaseDirectoryPath(isInternalCD ? context.getCacheDir() : context.getExternalCacheDir())
                .setMaxCacheSize(cacheSizeInM * 1024 * 1024)
                .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setMainDiskCacheConfig(diskCacheConfig)
                .build();
        Fresco.initialize(context, config);
    }

    @Override
    public void request(SingleConfig config) {

    }

    @Override
    public void pause() {
        Fresco.getImagePipeline().pause();
    }

    @Override
    public void resume() {
        Fresco.getImagePipeline().resume();
    }

    @Override
    public void clearDiskCache() {
        Fresco.getImagePipeline().clearDiskCaches();
    }

    @Override
    public void clearMomoryCache(View view) {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    @Override
    public void clearMomory() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    @Override
    public boolean isCached(String url) {
        return false;
    }

    @Override
    public void trimMemory(int level) {

    }

    @Override
    public void clearAllMemoryCaches() {
        Fresco.getImagePipeline().clearCaches();
    }

    @Override
    public Bitmap loadBitmap(Context context, String url) {
        return null;
    }

    @Override
    public void saveImageIntoGallery(DownLoadImageService downLoadImageService) {

    }
}
