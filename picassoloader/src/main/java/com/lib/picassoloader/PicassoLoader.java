package com.lib.picassoloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.lib.imagelib.big.BigImageViewer;
import com.lib.imagelib.config.GlobalConfig;
import com.lib.imagelib.config.PriorityMode;
import com.lib.imagelib.config.ScaleMode;
import com.lib.imagelib.config.ShapeMode;
import com.lib.imagelib.config.SingleConfig;
import com.lib.imagelib.loader.FileGetter;
import com.lib.imagelib.loader.ILoader;
import com.lib.imagelib.utils.DownLoadImageService;
import com.lib.imagelib.utils.ImageUtil;
import com.lib.picassoloader.big.PicassoBigLoader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import okhttp3.OkHttpClient;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

/**
 * Created by yzd on 2018/3/19 0019.
 */

public class PicassoLoader implements ILoader {

    private static final String TAG_PICASSO = "picasso";
    private List<String> paths = new ArrayList<>();
    private OkHttpClient client;

    @Override
    public void init(Context context, int cacheSizeInM, int memoryMode, boolean isInternalCD) {
        client = ImageUtil.getClient(GlobalConfig.ignoreCertificateVerify);
        Picasso.Builder builder = new Picasso.Builder(context)
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .downloader(new OkHttp3Downloader(client))
                .memoryCache(new LruCache(2 * 1024 * 1024));
        Picasso.setSingletonInstance(builder.build());
        BigImageViewer.initialize(new PicassoBigLoader(client));
    }

    @Override
    public void request(final SingleConfig config) {
        RequestCreator request = getDrawableTypeRequest(config);
        if (request == null) return;
        request.tag(TAG_PICASSO).config(Bitmap.Config.RGB_565);
        if (config.getPlaceHolderResId() != 0) {
            request.placeholder(config.getPlaceHolderResId());
        } else {
            request.noPlaceholder();
        }
        request.error(config.getErrorResId());

        boolean canFit = true;
        boolean canCenterCrop = false;
        if (config.getWidth() > 0 && config.getHeight() > 0) {
            request.resize(config.getWidth(), config.getHeight());
            canFit = false;
            canCenterCrop = true;
        }

        int scaleMode = config.getScaleMode();

        switch (scaleMode) {
            case ScaleMode.CENTER_CROP:
                if (canCenterCrop)
                    request.centerCrop();
                break;
            case ScaleMode.FIT_CENTER:
                if (canFit)
                    request.fit();
                break;
            case ScaleMode.CENTER_INSIDE:
                request.centerInside();
                break;
            default:
                if (canFit)
                    request.fit();
                break;
        }

        if (config.getWidth() > 1000 || config.getHeight() > 1000) {
            request.memoryPolicy(NO_CACHE, NO_STORE);
        }

        setShapeModeAndBlur(config, request);

        setPriority(config, request);
        if (config.isAsBitmap()) {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if (config.getBitmapListener() != null)
                        config.getBitmapListener().onSuccess(bitmap);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    if (config.getBitmapListener() != null)
                        config.getBitmapListener().onFail();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            request.into(target);
        } else if (config.getTarget() instanceof ImageView) {
            request.into((ImageView) config.getTarget());
        }

    }

    @Override
    public void pause() {
        Picasso.get().pauseTag(TAG_PICASSO);
    }

    @Override
    public void resume() {
        Picasso.get().resumeTag(TAG_PICASSO);
    }

    @Override
    public void clearDiskCache() {
        File dir = new File(GlobalConfig.context.getCacheDir(), "picasso-cache");
        if (dir.exists()) {
            ImageUtil.deleteFolderFile(dir.getAbsolutePath(), false);
        }
        //PicassoBigLoader.clearCache();
    }

    @Override
    public void clearCacheByUrl(String url) {
        Picasso.get().invalidate(url);
    }

    @Override
    public long getCacheSize() {
        File dir = new File(GlobalConfig.context.getCacheDir(), "picasso-cache");
        if (dir.exists()) {
            return ImageUtil.getFolderSize(dir);
        } else {
            return 0;
        }
    }

    @Override
    public File getFileFromDiskCache(String url) {
        return null;
    }

    @Override
    public void getFileFromDiskCache(String url, final FileGetter getter) {

    }

    @Override
    public void clearMomoryCache(View view) {

    }

    @Override
    public void clearMomoryCache(String url) {
        Picasso.get().invalidate(url);
    }

    @Override
    public void clearMomory() {
        for (String path : paths) {
            Picasso.get().invalidate(path);
        }
    }

    @Override
    public boolean isCached(String url) {
        return false;
    }

    @Override
    public void trimMemory(int level) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void clearAllMemoryCaches() {
        clearMomory();
    }

    @Override
    public Bitmap loadBitmap(Context context, String url) {
        try {
            return Picasso.get().load(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void download(String url, FileGetter getter) {

    }

    @Override
    public void saveImageIntoGallery(DownLoadImageService downLoadImageService) {
        new Thread(downLoadImageService).start();
    }

    @Nullable
    private RequestCreator getDrawableTypeRequest(SingleConfig config) {
        RequestCreator request = null;
        if (!TextUtils.isEmpty(config.getUrl())) {
            request = Picasso.get().load(ImageUtil.appendUrl(config.getUrl()));
            paths.add(config.getUrl());
        } else if (!TextUtils.isEmpty(config.getFilePath())) {
            request = Picasso.get().load(new File(config.getFilePath()));
            paths.add(config.getFilePath());
        } else if (!TextUtils.isEmpty(config.getContentProvider())) {
            request = Picasso.get().load(Uri.parse(config.getContentProvider()));
            paths.add(config.getContentProvider());
        } else if (config.getResId() > 0) {
            request = Picasso.get().load(config.getResId());
            paths.add(config.getResId() + "");
        }
        return request;
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

    private void setShapeModeAndBlur(SingleConfig config, RequestCreator request) {
        int shapeMode = config.getShapeMode();
        List<Transformation> transformations = new ArrayList<>();


        if (config.isNeedBlur()) {
            transformations.add(new BlurTransformation(GlobalConfig.context, config.getBlurRadius()));
        }


        switch (shapeMode) {
            case ShapeMode.RECT:

                if (config.getBorderWidth() > 0) {

                }
                break;
            case ShapeMode.RECT_ROUND:
                transformations.add(new RoundedCornersTransformation(config.getRectRoundRadius(), 0, RoundedCornersTransformation.CornerType.ALL));

                if (config.getBorderWidth() > 0) {

                }
                if (config.isGif() && config.getRoundOverlayColor() > 0) {

                }
                break;
            case ShapeMode.OVAL:
                transformations.add(new CropCircleTransformation());
                if (config.getBorderWidth() > 0) {

                }
                if (config.isGif() && config.getRoundOverlayColor() > 0) {

                }
                break;
        }
        if (transformations.size() > 0) {
            request.transform(transformations);
        }


    }
}
