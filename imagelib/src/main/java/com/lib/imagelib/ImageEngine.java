package com.lib.imagelib.loader;

import android.content.Context;
import android.view.View;

import com.lib.imagelib.config.GlobalConfig;
import com.lib.imagelib.config.MemoryMode;
import com.lib.imagelib.config.SingleConfig;

/**
 * Created by yzd on 2018/3/17 0017.
 */

public final class ImageEngine {
    public static Context context;

    /**
     * 默认最大缓存
     */
    public static int CACHE_IMAGE_SIZE = 250;

    public static void init(final Context context) {
        init(context, CACHE_IMAGE_SIZE, GlobalConfig.Engine.GLIDELOADER);
    }

    public static void init(final Context context, GlobalConfig.Engine engine) {
        init(context, CACHE_IMAGE_SIZE, engine);
    }

    public static void init(final Context context, int cacheSizeInM, GlobalConfig.Engine engine) {
        init(context, cacheSizeInM, MemoryMode.NORMAL, engine);
    }

    public static void init(final Context context, int cacheSizeInM, int memoryMode, GlobalConfig.Engine engine) {
        init(context, cacheSizeInM, memoryMode, true, engine);
    }
    /**
     * @param context        上下文
     * @param cacheSizeInM   Glide默认磁盘缓存最大容量250MB
     * @param memoryMode     调整内存缓存的大小 LOW(0.5f) ／ NORMAL(1f) ／ HIGH(1.5f);
     * @param isInternalCD   true 磁盘缓存到应用的内部目录 / false 磁盘缓存到外部存
     */
    public static void init(final Context context, int cacheSizeInM, int memoryMode, boolean isInternalCD, GlobalConfig.Engine engine) {
        ImageEngine.context = context;
        GlobalConfig.init(context, cacheSizeInM, memoryMode, isInternalCD, engine);
    }

    /**
     * 获取当前的Loader
     * @return
     */
    public static ILoader getActualLoader() {
        return GlobalConfig.getLoader();
    }

    /**
     * 加载普通图片
     *
     * @param context
     * @return
     */
    public static SingleConfig.ConfigBuilder with(Context context) {
        return new SingleConfig.ConfigBuilder(context);
    }

    public static void trimMemory(int level) {
        getActualLoader().trimMemory(level);
    }

    public static void clearAllMemoryCaches() {
        getActualLoader().clearAllMemoryCaches();
    }

    public static void pauseRequests() {
        getActualLoader().pause();

    }

    public static void resumeRequests() {
        getActualLoader().resume();
    }

    /**
     *Cancel any pending loads Glide may have for the view and free any resources that may have been loaded for the view.
     * @param view
     */
    public static void clearMomoryCache(View view) {
        getActualLoader().clearMomoryCache(view);
    }


    /**
     * Clears disk cache.
     *
     * <p>
     *     This method should always be called on a background thread, since it is a blocking call.
     * </p>
     */
    public static void clearDiskCache() {
        getActualLoader().clearDiskCache();
    }

    /**
     * Clears as much memory as possible.
     */
    public static void clearMomory() {
        getActualLoader().clearMomory();
    }

}
