package com.lib.frescoloader;

import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.CountingMemoryCache;
import com.facebook.imagepipeline.cache.ImageCacheStatsTracker;

/**
 * Created by yzd on 2018/5/11 0011.
 */

public class MyImageCacheStatsTracker implements ImageCacheStatsTracker {
    @Override
    public void onBitmapCachePut() {

    }

    @Override
    public void onBitmapCacheHit(CacheKey cacheKey) {

    }

    @Override
    public void onBitmapCacheMiss() {

    }

    @Override
    public void onMemoryCachePut() {

    }

    @Override
    public void onMemoryCacheHit(CacheKey cacheKey) {

    }

    @Override
    public void onMemoryCacheMiss() {

    }

    @Override
    public void onStagingAreaHit(CacheKey cacheKey) {

    }

    @Override
    public void onStagingAreaMiss() {

    }

    @Override
    public void onDiskCacheHit() {

    }

    @Override
    public void onDiskCacheMiss() {

    }

    @Override
    public void onDiskCacheGetFail() {

    }

    @Override
    public void registerBitmapMemoryCache(CountingMemoryCache<?, ?> bitmapMemoryCache) {

    }

    @Override
    public void registerEncodedMemoryCache(CountingMemoryCache<?, ?> encodedMemoryCache) {

    }
}