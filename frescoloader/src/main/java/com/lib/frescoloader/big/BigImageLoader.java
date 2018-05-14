/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Piasy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.lib.frescoloader.big;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.disk.FileCache;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.lib.frescoloader.R;
import com.lib.imagelib.big.event.CacheHitEvent;
import com.lib.imagelib.big.event.ErrorEvent;
import com.lib.imagelib.big.event.ProgressEvent;
import com.lib.imagelib.big.loader.BigLoader;
import com.lib.imagelib.big.view.BigImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by Piasy{github.com/Piasy} on 08/11/2016.
 */

public final class BigImageLoader implements BigLoader {

    private final Context mAppContext;
    private final DefaultExecutorSupplier mExecutorSupplier;
    private Handler handler;

    private BigImageLoader(Context appContext) {
        mAppContext = appContext;

        mExecutorSupplier = new DefaultExecutorSupplier(Runtime.getRuntime().availableProcessors());
        handler = new Handler(Looper.getMainLooper());
    }

    public static BigImageLoader with(Context appContext) {
        return with(appContext, null, null);
    }

    public static BigImageLoader with(Context appContext,
                                      ImagePipelineConfig imagePipelineConfig) {
        return with(appContext, imagePipelineConfig, null);
    }

    public static BigImageLoader with(Context appContext,
                                      ImagePipelineConfig imagePipelineConfig, DraweeConfig draweeConfig) {
        Fresco.initialize(appContext, imagePipelineConfig, draweeConfig);
        return new BigImageLoader(appContext);
    }

    @Override
    public void loadImage(final Uri uri) {
        ImageRequest request = ImageRequest.fromUri(uri);

        final File localCache = getCacheFile(request);
        if (localCache!=null && localCache.exists()) {
            Log.e("onResourceReady","cache onResourceReady  --"+ localCache.getAbsolutePath());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(localCache.length() >100){

                        EventBus.getDefault().postSticky(new CacheHitEvent(localCache,uri.toString()));
                    }else {
                        EventBus.getDefault().postSticky(new ErrorEvent(uri.toString()));
                    }
                }
            },300);


        } else {
            //EventBus.getDefault().post(new StartEvent(uri.toString()));
            //EventBus.getDefault().post(new ProgressEvent(0,false,uri.toString()));
           // callback.onStart(); // ensure `onStart` is called before `onProgress` and `onFinish`
           // callback.onProgress(0); // show 0 progress immediately

            ImagePipeline pipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<PooledByteBuffer>> source
                    = pipeline.fetchEncodedImage(request, true);
            source.subscribe(new ImageDownloadSubscriber(mAppContext) {
                @Override
                protected void onProgress(int progress) {
                    //callback.onProgress(progress);
                    EventBus.getDefault().post(new ProgressEvent(progress,progress==100,uri.toString()));
                }

                @Override
                protected void onSuccess(File image) {
                    //EventBus.getDefault().post(new ProgressEvent(100,true,uri.toString()));
                    Log.e("onResourceReady","download onResourceReady  --"+ image.getAbsolutePath());
                    if(image.length() >100){
                        EventBus.getDefault().postSticky(new CacheHitEvent(image,uri.toString()));
                    }else {
                        EventBus.getDefault().postSticky(new ErrorEvent(uri.toString()));
                    }
                    //callback.onFinish();
                    //callback.onCacheMiss(image);

                }

                @Override
                protected void onFail(Throwable t) {
                    // TODO: 12/11/2016 fail
                    t.printStackTrace();
                    EventBus.getDefault().post(new ErrorEvent(uri.toString()));
                }
            }, mExecutorSupplier.forBackgroundTasks());
        }
    }

    @Override
    public View showThumbnail(BigImageView parent, Uri thumbnail, int scaleType) {
        SimpleDraweeView thumbnailView = (SimpleDraweeView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ui_fresco_thumbnail, parent, false);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(thumbnail)
                .build();
        switch (scaleType) {
            case BigImageView.INIT_SCALE_TYPE_CENTER_CROP:
                thumbnailView.getHierarchy()
                        .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
                break;
            case BigImageView.INIT_SCALE_TYPE_CENTER_INSIDE:
                thumbnailView.getHierarchy()
                        .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            default:
                break;
        }
        thumbnailView.setController(controller);
        return thumbnailView;
    }

    @Override
    public void prefetch(Uri uri) {
        ImagePipeline pipeline = Fresco.getImagePipeline();
        pipeline.prefetchToDiskCache(ImageRequest.fromUri(uri),
                false); // we don't need context, but avoid null
    }

    private File getCacheFile(final ImageRequest request) {
        FileCache mainFileCache = ImagePipelineFactory
                .getInstance()
                .getMainFileCache();
        final CacheKey cacheKey = DefaultCacheKeyFactory
                .getInstance()
                .getEncodedCacheKey(request, false); // we don't need context, but avoid null
        File cacheFile = request.getSourceFile();
        // http://crashes.to/s/ee10638fb31
        if (mainFileCache.hasKey(cacheKey) && mainFileCache.getResource(cacheKey) != null) {
            cacheFile = ((FileBinaryResource) mainFileCache.getResource(cacheKey)).getFile();
        }
        return cacheFile;
    }
}
