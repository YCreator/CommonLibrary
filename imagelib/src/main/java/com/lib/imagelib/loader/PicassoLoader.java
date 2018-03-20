package com.lib.imagelib.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.lib.imagelib.config.GlobalConfig;
import com.lib.imagelib.config.PriorityMode;
import com.lib.imagelib.config.ScaleMode;
import com.lib.imagelib.config.SingleConfig;
import com.lib.imagelib.utils.DownLoadImageService;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.IOException;

/**
 * Created by yzd on 2018/3/19 0019.
 */

public class PicassoLoader implements ILoader {

    @Override
    public void init(Context context, int cacheSizeInM, int memoryMode, boolean isInternalCD) {
        Picasso.Builder builder = new Picasso.Builder(context)
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .memoryCache(new LruCache(2 * 1024 * 1024));
        Picasso.setSingletonInstance(builder.build());
    }

    @Override
    public void request(final SingleConfig config) {
        RequestCreator request = Picasso.with(config.getContext()).load(config.getUrl());
        if (config.getPlaceHolderResId() != 0) {
            request.placeholder(config.getPlaceHolderResId());
        } else {
            request.noPlaceholder();
        }
        request.error(config.getErrorResId());
        int scaleMode = config.getScaleMode();

        switch (scaleMode) {
            case ScaleMode.CENTER_CROP:
                request.centerCrop();
                break;
            case ScaleMode.FIT_CENTER:
                request.fit();
                break;
            case ScaleMode.CENTER_INSIDE:
                request.centerInside();
                break;
            default:
                request.fit();
                break;
        }

        setPriority(config, request);
        request.config(Bitmap.Config.RGB_565);
        if (config.isAsBitmap()) {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if (config.getBitmapListener() != null)
                        config.getBitmapListener().onSuccess(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    if (config.getBitmapListener() != null)
                        config.getBitmapListener().onFail();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            request.into(target);
        } else {
            request.into((ImageView) config.getTarget());
        }

    }

    @Override
    public void pause() {
        Picasso.with(GlobalConfig.context).pauseTag("");
    }

    @Override
    public void resume() {
        Picasso.with(GlobalConfig.context).resumeTag("");
    }

    @Override
    public void clearDiskCache() {

    }

    @Override
    public void clearMomoryCache(View view) {

    }

    @Override
    public void clearMomory() {

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

    }

    @Override
    public Bitmap loadBitmap(Context context, String url) {
        try {
            return Picasso.with(context).load(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveImageIntoGallery(DownLoadImageService downLoadImageService) {
        new Thread(downLoadImageService).start();
    }


    /**
     * 设置加载优先级
     *
     * @param config
     * @param request
     */
    private void setPriority(SingleConfig config, RequestCreator request) {
        switch (config.getPriority()) {
            case PriorityMode.PRIORITY_LOW:
                request.priority(Picasso.Priority.LOW);
                break;
            case PriorityMode.PRIORITY_NORMAL:
                request.priority(Picasso.Priority.NORMAL);
                break;
            case PriorityMode.PRIORITY_HIGH:
                request.priority(Picasso.Priority.HIGH);
                break;
            default:
                request.priority(Picasso.Priority.NORMAL);
                break;
        }
    }
}
