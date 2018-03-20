package com.lib.imagelib.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lib.imagelib.config.Contants;
import com.lib.imagelib.config.DiskCacheMode;
import com.lib.imagelib.config.ShapeMode;
import com.lib.imagelib.config.SingleConfig;
import com.lib.imagelib.utils.DownLoadImageService;
import com.lib.imagelib.utils.ImageUtil;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

/**
 * Created by yzd on 2018/3/19 0019.
 */

public class ImageLoader implements ILoader {
    @Override
    public void init(Context context, int cacheSizeInM, int memoryMode, boolean isInternalCD) {
        File cacheDirectory = isInternalCD ? context.getCacheDir() : context.getExternalCacheDir();
        File cacheDir = new File(cacheDirectory, Contants.DEFAULT_DISK_CACHE_DIR);
        try {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .memoryCacheExtraOptions(480, 800)
                    .threadPoolSize(3) //线程池没加载的数量
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                    .diskCache(new LruDiskCache(cacheDir, new HashCodeFileNameGenerator(), cacheSizeInM * 1024 * 1024))
                    .tasksProcessingOrder(QueueProcessingType.FIFO)
                    .build();
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void request(final SingleConfig config) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheOnDisk(config.getDiskCacheMode() != 0 && config.getDiskCacheMode() != DiskCacheMode.NONE);
        builder.showImageOnLoading(config.getPlaceHolderResId());
        builder.showImageOnFail(config.getErrorResId());
        builder.imageScaleType(ImageScaleType.EXACTLY);
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        if (config.getShapeMode() == ShapeMode.OVAL) {
            builder.displayer(new RoundedBitmapDisplayer(config.getRectRoundRadius()));
        }

        if (config.getTarget() instanceof ImageView) {
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(getRequest(config), (ImageView) config.getTarget(), builder.build(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if (config.getBitmapListener() != null) {
                        config.getBitmapListener().onFail();
                    }
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (config.getBitmapListener() != null) {
                        config.getBitmapListener().onSuccess(loadedImage);
                    }
                }
            });
        }

    }

    @Override
    public void pause() {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().pause();
    }

    @Override
    public void resume() {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().resume();
    }

    @Override
    public void clearDiskCache() {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().clearDiskCache();
    }

    @Override
    public void clearMomoryCache(View view) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().clearMemoryCache();
    }

    @Override
    public void clearMomory() {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().clearMemoryCache();
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
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().clearMemoryCache();
    }

    @Override
    public Bitmap loadBitmap(Context context, String url) {
        return com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(url);
    }

    @Override
    public void saveImageIntoGallery(DownLoadImageService downLoadImageService) {
        new Thread(downLoadImageService).start();
    }

    @Nullable
    private String getRequest(SingleConfig config) {
        if (!TextUtils.isEmpty(config.getUrl())) {
            Log.e("TAG", "getUrl : " + config.getUrl());
            return ImageUtil.appendUrl(config.getUrl());
        } else if (!TextUtils.isEmpty(config.getFilePath())) {
            Log.e("TAG", "getFilePath : " + config.getFilePath());
            return ImageUtil.appendUrl(config.getFilePath());
        } else if (!TextUtils.isEmpty(config.getContentProvider())) {
            Log.e("TAG", "getContentProvider : " + config.getContentProvider());
            return Uri.parse(config.getContentProvider()).toString();
        } else if (config.getResId() > 0) {
            Log.e("TAG", "getResId : " + config.getResId());
            return config.getResId() + "";
        } else if (config.getFile() != null) {
            Log.e("TAG", "getFile : " + config.getFile());
            return config.getFile().toString();
        } else if (!TextUtils.isEmpty(config.getAssertspath())) {
            Log.e("TAG", "getAssertspath : " + config.getAssertspath());
            return config.getAssertspath();
        } else if (!TextUtils.isEmpty(config.getRawPath())) {
            Log.e("TAG", "getRawPath : " + config.getRawPath());
            return config.getRawPath();
        }
        return "";
    }
}
