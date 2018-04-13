package com.lib.imagelib.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;
import com.lib.imagelib.config.AnimationMode;
import com.lib.imagelib.config.Contants;
import com.lib.imagelib.config.DiskCacheMode;
import com.lib.imagelib.config.GlobalConfig;
import com.lib.imagelib.config.MemoryMode;
import com.lib.imagelib.config.PriorityMode;
import com.lib.imagelib.config.ScaleMode;
import com.lib.imagelib.config.ShapeMode;
import com.lib.imagelib.config.SingleConfig;
import com.lib.imagelib.utils.DownLoadImageService;
import com.lib.imagelib.utils.ImageUtil;

import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

/**
 * Created by yzd on 2018/3/17 0017.
 */

public class GlideLoader implements ILoader {
    @Override
    public void init(Context context, int cacheSizeInM, int memoryMode, boolean isInternalCD) {
        MemoryCategory memoryCategory = memoryMode == MemoryMode.LOW ? MemoryCategory.LOW : memoryMode == MemoryMode.HIGH ? MemoryCategory.HIGH : MemoryCategory.NORMAL;
        Glide.get(context).setMemoryCategory(memoryCategory); //如果在应用当中想要调整内存缓存的大小，开发者可以通过如下方式：
        GlideBuilder builder = new GlideBuilder();
        if (isInternalCD) {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, Contants.DEFAULT_DISK_CACHE_DIR, cacheSizeInM * 1024 * 1024));
        } else {
            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, Contants.DEFAULT_DISK_CACHE_DIR, cacheSizeInM * 1024 * 1024));
        }
    }

    @Override
    public void request(final SingleConfig config) {
        RequestManager requestManager = Glide.with(config.getContext());
        RequestBuilder request;
        //设置图片加载动画
        if (config.isAsBitmap()) {
            request = getRequest(config, requestManager.asBitmap().transition(bitmapAnimator(config)));
        } else if (config.isGif()) {
            request = getRequest(config, requestManager.asGif().transition(drawableAnimator(config)));
        } else {
            request = getRequest(config, requestManager.asDrawable().transition(drawableAnimator(config)));
        }
        RequestOptions options = new RequestOptions();
        if (config.isAsBitmap()) {
            if (request == null) {
                return;
            }
            SimpleTarget target = new SimpleTarget<Bitmap>(config.getWidth(), config.getHeight()) {
                @Override
                public void onResourceReady(Bitmap resource, Transition transition) {
                    if (config.getBitmapListener() != null)
                        config.getBitmapListener().onSuccess(resource);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    if (config.getBitmapListener() != null)
                        config.getBitmapListener().onFail();
                }
            };
            setShapeModeAndBlur(config, options);

            if (config.getDiskCacheMode() != 0) {
                options.diskCacheStrategy(config.getDiskCacheMode() == DiskCacheMode.ALL
                        ? DiskCacheStrategy.ALL : config.getDiskCacheMode() == DiskCacheMode.AUTOMATIC
                        ? DiskCacheStrategy.AUTOMATIC : config.getDiskCacheMode() == DiskCacheMode.DATA
                        ? DiskCacheStrategy.DATA : config.getDiskCacheMode() == DiskCacheMode.RESOURCE
                        ? DiskCacheStrategy.RESOURCE : DiskCacheStrategy.NONE);
            }
            request.apply(options);
            request.into(target);
        } else {

            if (request == null) {
                return;
            }

            if (ImageUtil.shouldSetPlaceHolder(config)) {
                options.placeholder(config.getPlaceHolderResId());
            }

            int scaleMode = config.getScaleMode();

            switch (scaleMode) {
                case ScaleMode.CENTER_CROP:
                    options.centerCrop();
                    break;
                case ScaleMode.FIT_CENTER:
                    options.fitCenter();
                    break;
                case ScaleMode.CENTER_INSIDE:
                    options.centerInside();
                    break;
                default:
                    options.fitCenter();
                    break;
            }

            setShapeModeAndBlur(config, options);

            //设置缩略图
            if (config.getThumbnail() != 0) {
                request.thumbnail(config.getThumbnail());
            }

            //设置图片加载的分辨 sp
            if (config.getoWidth() != 0 && config.getoHeight() != 0) {
                options.override(config.getoWidth(), config.getoHeight());
            }

            //是否跳过磁盘存储
            if (config.getDiskCacheMode() != 0) {
                options.diskCacheStrategy(config.getDiskCacheMode() == DiskCacheMode.ALL
                        ? DiskCacheStrategy.ALL : config.getDiskCacheMode() == DiskCacheMode.AUTOMATIC
                        ? DiskCacheStrategy.AUTOMATIC : config.getDiskCacheMode() == DiskCacheMode.DATA
                        ? DiskCacheStrategy.DATA : config.getDiskCacheMode() == DiskCacheMode.RESOURCE
                        ? DiskCacheStrategy.RESOURCE : DiskCacheStrategy.NONE);
            }

            //设置图片加载优先级
            setPriority(config, options);

            if (config.getErrorResId() > 0) {
                options.error(config.getErrorResId());
            }
            request.apply(options);
            if (config.getTarget() instanceof ImageView) {
                request.into((ImageView) config.getTarget());
            }
        }
    }

    @Override
    public void pause() {
        Glide.with(GlobalConfig.context).pauseRequestsRecursive();
    }

    @Override
    public void resume() {
        Glide.with(GlobalConfig.context).resumeRequestsRecursive();
    }

    @Override
    public void clearDiskCache() {
        Glide.get(GlobalConfig.context).clearDiskCache();
    }

    @Override
    public void clearMomoryCache(View view) {
        Glide.with(view).clear(view);
    }

    @Override
    public void clearMomory() {
        Glide.get(GlobalConfig.context).clearMemory();
    }

    @Override
    public boolean isCached(String url) {
        return false;
    }

    @Override
    public void trimMemory(int level) {
        Glide.with(GlobalConfig.context).onTrimMemory(level);
    }

    @Override
    public void clearAllMemoryCaches() {
        Glide.with(GlobalConfig.context).onLowMemory();
    }

    @Override
    public Bitmap loadBitmap(Context context, String url) {
        try {
            return Glide
                    .with(context)
                    .asBitmap()
                    .load(url)
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveImageIntoGallery(DownLoadImageService downLoadImageService) {
        new Thread(downLoadImageService).start();
    }

    @Nullable
    private <T> RequestBuilder<T> getRequest(SingleConfig config, RequestBuilder<T> request) {
        if (!TextUtils.isEmpty(config.getUrl())) {
            request = request.load(ImageUtil.appendUrl(config.getUrl()));
            Log.e("TAG", "getUrl : " + config.getUrl());
        } else if (!TextUtils.isEmpty(config.getFilePath())) {
            request = request.load(ImageUtil.appendUrl(config.getFilePath()));
            Log.e("TAG", "getFilePath : " + config.getFilePath());
        } else if (!TextUtils.isEmpty(config.getContentProvider())) {
            request = request.load(Uri.parse(config.getContentProvider()));
            Log.e("TAG", "getContentProvider : " + config.getContentProvider());
        } else if (config.getResId() > 0) {
            request = request.load(config.getResId());
            Log.e("TAG", "getResId : " + config.getResId());
        } else if (config.getFile() != null) {
            request = request.load(config.getFile());
            Log.e("TAG", "getFile : " + config.getFile());
        } else if (!TextUtils.isEmpty(config.getAssertspath())) {
            request = request.load(config.getAssertspath());
            Log.e("TAG", "getAssertspath : " + config.getAssertspath());
        } else if (!TextUtils.isEmpty(config.getRawPath())) {
            request = request.load(config.getRawPath());
            Log.e("TAG", "getRawPath : " + config.getRawPath());
        }
        return request;
    }

    /**
     * 设置加载优先级
     *
     * @param config
     * @param options
     */
    private void setPriority(SingleConfig config, RequestOptions options) {
        switch (config.getPriority()) {
            case PriorityMode.PRIORITY_LOW:
                options.priority(Priority.LOW);
                break;
            case PriorityMode.PRIORITY_NORMAL:
                options.priority(Priority.NORMAL);
                break;
            case PriorityMode.PRIORITY_HIGH:
                options.priority(Priority.HIGH);
                break;
            case PriorityMode.PRIORITY_IMMEDIATE:
                options.priority(Priority.IMMEDIATE);
                break;
            default:
                options.priority(Priority.IMMEDIATE);
                break;
        }
    }

    /**
     * 设置加载进入动画
     *
     * @param config
     */
    private DrawableTransitionOptions drawableAnimator(SingleConfig config) {
        DrawableCrossFadeFactory.Builder builder = new DrawableCrossFadeFactory.Builder();
        if (config.getAnimationType() == AnimationMode.ANIMATIONID) {
            builder.setCrossFadeEnabled(true);
            //builder.setDefaultAnimationId(config.getAnimationId());
        } else if (config.getAnimationType() == AnimationMode.ANIMATOR) {
           // builder.setDefaultAnimationFactory(config.getAnimator());
        } else if (config.getAnimationType() == AnimationMode.ANIMATION) {
           // builder.setDefaultAnimation(config.getAnimation());
        }
        return DrawableTransitionOptions.withCrossFade(builder);
    }

    /**
     * 设置加载进入动画
     *
     * @param config
     */
    private BitmapTransitionOptions bitmapAnimator(SingleConfig config) {
        DrawableCrossFadeFactory.Builder builder = new DrawableCrossFadeFactory.Builder();
        if (config.getAnimationType() == AnimationMode.ANIMATIONID) {
            builder.setCrossFadeEnabled(true);
            //builder.setDefaultAnimationId(config.getAnimationId());
        } else if (config.getAnimationType() == AnimationMode.ANIMATOR) {
           // builder.setDefaultAnimationFactory(config.getAnimator());
        } else if (config.getAnimationType() == AnimationMode.ANIMATION) {
           // builder.setDefaultAnimation(config.getAnimation());
        }
        return BitmapTransitionOptions.withCrossFade(builder);
    }

    /**
     * 设置图片滤镜和形状
     *
     * @param config
     * @param options
     */
    private void setShapeModeAndBlur(SingleConfig config, RequestOptions options) {

        if (config.isNeedBlur()) {
            options.getTransformations().put(BlurTransformation.class, new BlurTransformation(config.getContext(), config.getBlurRadius()));
        }

        if (config.isNeedBrightness()) {
            options.getTransformations().put(BrightnessFilterTransformation.class, new BrightnessFilterTransformation(config.getContext(), config.getBrightnessLeve())); //亮度
        }

        if (config.isNeedGrayscale()) {
            options.getTransformations().put(GrayscaleTransformation.class, new GrayscaleTransformation(config.getContext())); //黑白效果
        }

        if (config.isNeedFilteColor()) {
            options.getTransformations().put(ColorFilterTransformation.class, new ColorFilterTransformation(config.getContext(), config.getFilteColor()));
        }

        if (config.isNeedSwirl()) {
            options.getTransformations().put(SwirlFilterTransformation.class, new SwirlFilterTransformation(config.getContext(), 0.5f, 1.0f, new PointF(0.5f, 0.5f))); //漩涡
        }

        if (config.isNeedToon()) {
            options.getTransformations().put(ToonFilterTransformation.class, new ToonFilterTransformation(config.getContext())); //油画
        }

        if (config.isNeedSepia()) {
            options.getTransformations().put(SepiaFilterTransformation.class, new SepiaFilterTransformation(config.getContext())); //墨画
        }

        if (config.isNeedContrast()) {
            options.getTransformations().put(ContrastFilterTransformation.class, new ContrastFilterTransformation(config.getContext(), config.getContrastLevel())); //锐化
        }

        if (config.isNeedInvert()) {
            options.getTransformations().put(InvertFilterTransformation.class, new InvertFilterTransformation(config.getContext())); //胶片
        }

        if (config.isNeedPixelation()) {
            options.getTransformations().put(PixelationFilterTransformation.class, new PixelationFilterTransformation(config.getContext(), config.getPixelationLevel())); //马赛克
        }

        if (config.isNeedSketch()) {
            options.getTransformations().put(SketchFilterTransformation.class, new SketchFilterTransformation(config.getContext())); //素描
        }

        if (config.isNeedVignette()) {
            options.getTransformations().put(VignetteFilterTransformation.class, new VignetteFilterTransformation(config.getContext(), new PointF(0.5f, 0.5f),
                    new float[]{0.0f, 0.0f, 0.0f}, 0f, 0.75f)); //晕映
        }

        switch (config.getShapeMode()) {
            case ShapeMode.RECT:

                break;
            case ShapeMode.RECT_ROUND:
                options.getTransformations().put(RoundedCornersTransformation.class, new RoundedCornersTransformation(config.getContext(), config.getRectRoundRadius(), 0, RoundedCornersTransformation.CornerType.ALL));
                break;
            case ShapeMode.OVAL:
                options.getTransformations().put(CropCircleTransformation.class, new CropCircleTransformation(config.getContext()));
                break;

            case ShapeMode.SQUARE:
                options.getTransformations().put(CropSquareTransformation.class, new CropSquareTransformation(config.getContext()));
                break;
        }
    }

}
