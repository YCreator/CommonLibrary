package com.lib.imagelib;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by yzd on 2018/3/16 0016.
 */

public class ImageLoaderPubImpl implements Pub {
    @Override
    public void initOption() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(ImagePub.mContext, "");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ImagePub.mContext)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3) //线程池没加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .diskCache(new LimitedAgeDiskCache(cacheDir, cacheDir, new HashCodeFileNameGenerator(), 3 * 24 * 60 * 60))
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void load(String url, ImageView mView) {
        ImageLoader.getInstance().displayImage(url, mView);
    }

    @Override
    public void load(String url, ImageView mView, ImageOption option) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(option.isCacheInMemory())
                .cacheOnDisk(option.isCacheOnDisk())
                .showImageOnLoading(option.getLoadingShow())
                .showImageOnFail(option.getErrorShow())
                .build();
        ImageLoader.getInstance().displayImage(url, mView, options);
    }

    @Override
    public void load(String url, ImageView mView, final ImagePubLoadingListener listener) {
        ImageLoader.getInstance().displayImage(url, mView, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                listener.onLoadingStarted(imageUri, (ImageView) view);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                listener.onLoadingFailed(imageUri, (ImageView) view, null);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                listener.onLoadingComplete(imageUri, (ImageView) view, loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                listener.onLoadingCancelled(imageUri, (ImageView) view);
            }
        });
    }

    @Override
    public void load(String url, ImageView mView, ImageOption option, final ImagePubLoadingListener listener) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(option.isCacheInMemory())
                .cacheOnDisk(option.isCacheOnDisk())
                .showImageOnLoading(option.getLoadingShow())
                .showImageOnFail(option.getErrorShow())
                .build();
        ImageLoader.getInstance().displayImage(url, mView, options, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                listener.onLoadingStarted(imageUri, (ImageView) view);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                listener.onLoadingFailed(imageUri, (ImageView) view, null);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                listener.onLoadingComplete(imageUri, (ImageView) view, loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                listener.onLoadingCancelled(imageUri, (ImageView) view);
            }
        });
    }
}
