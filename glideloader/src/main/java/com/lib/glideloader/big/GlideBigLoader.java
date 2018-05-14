package com.lib.glideloader.big;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lib.glideloader.R;
import com.lib.imagelib.big.event.CacheHitEvent;
import com.lib.imagelib.big.event.ErrorEvent;
import com.lib.imagelib.big.loader.BigLoader;
import com.lib.imagelib.big.progress.ProgressInterceptor;
import com.lib.imagelib.big.view.BigImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by yzd on 2018/5/10 0010.
 */

public final class GlideBigLoader implements BigLoader {

    private final RequestManager mRequestManager;

    private GlideBigLoader(Context context, OkHttpClient okHttpClient) {
        OkHttpClient client = okHttpClient.newBuilder().addNetworkInterceptor(new ProgressInterceptor()).build();
        Glide.get(context).getRegistry().replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
        mRequestManager = Glide.with(context);
    }

    public static GlideBigLoader with(Context context) {
        return with(context, null);
    }

    public static GlideBigLoader with(Context context, OkHttpClient okHttpClient) {
        return new GlideBigLoader(context, okHttpClient);
    }

    @Override
    public void loadImage(final Uri uri) {
        RequestBuilder<File> builder = mRequestManager.downloadOnly();
        builder.load(uri).into(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, Transition<? super File> transition) {
                if (resource.exists() && resource.isFile() && resource.length() > 100) {
                    Log.e("onResourceReady", "onResourceReady  --" + resource.getAbsolutePath());
                    EventBus.getDefault().post(new CacheHitEvent(resource, uri.toString()));
                } else {
                    Log.e("onloadfailed", "onLoadFailed  --" + uri.toString());
                    EventBus.getDefault().post(new ErrorEvent(uri.toString()));
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                super.onLoadCleared(placeholder);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                Log.e("onloadfailed", "onLoadFailed  --" + uri.toString());
                EventBus.getDefault().post(new ErrorEvent(uri.toString()));
            }

            /**
             * 如果资源已经在内存中，则onLoadStarted就不会被调用
             * @param placeholder
             */
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                super.onLoadStarted(placeholder);
            }
        });
    }

    @Override
    public View showThumbnail(BigImageView parent, Uri thumbnail, int scaleType) {
        ImageView thumbnailView = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ui_glide_thumbnail, parent, false);
        switch (scaleType) {
            case BigImageView.INIT_SCALE_TYPE_CENTER_CROP:
                thumbnailView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case BigImageView.INIT_SCALE_TYPE_CENTER_INSIDE:
                thumbnailView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            default:
                break;
        }
        mRequestManager
                .load(thumbnail)
                .into(thumbnailView);
        return thumbnailView;
    }

    @Override
    public void prefetch(Uri uri) {
        RequestBuilder<File> builder = mRequestManager.downloadOnly();
        builder.load(uri).into(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, Transition<? super File> transition) {

            }
        });
    }
}
