package com.lib.frescoloader;

import android.graphics.Bitmap;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.imagepipeline.bitmaps.SimpleBitmapReleaser;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.lib.imagelib.config.GlobalConfig;
import com.lib.imagelib.config.ScaleMode;

/**
 * Created by huangshuisheng on 2017/9/28.
 */

public class FrescoUtil {
    
    public static ScalingUtils.ScaleType getActualScaleType(int scaleMode){
        switch (scaleMode){
            case ScaleMode.CENTER_CROP:
                return ScalingUtils.ScaleType.CENTER_CROP;

            case ScaleMode.CENTER_INSIDE:
                return ScalingUtils.ScaleType.CENTER_INSIDE;

            case ScaleMode.FIT_CENTER:
                return ScalingUtils.ScaleType.FIT_CENTER;

            default:
                return ScalingUtils.ScaleType.CENTER_CROP;

        }
    }

    public static void putIntoPool(Bitmap bitmap,String uriString) {
        final ImageRequest requestBmp = ImageRequest.fromUri(uriString); // 赋值

// 获得 Key
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getBitmapCacheKey(requestBmp, GlobalConfig.context);

// 获得 closeableReference
        CloseableReference<CloseableImage> closeableReference = CloseableReference.<CloseableImage>of(
            new CloseableStaticBitmap(bitmap,
                SimpleBitmapReleaser.getInstance(),
                ImmutableQualityInfo.FULL_QUALITY, 0));
// 存入 Fresco
        Fresco.getImagePipelineFactory().getBitmapMemoryCache().cache(cacheKey, closeableReference);
    }
}
