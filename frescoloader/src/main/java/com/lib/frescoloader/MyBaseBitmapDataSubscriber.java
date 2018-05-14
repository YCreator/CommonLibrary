package com.lib.frescoloader;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.lib.frescoloader.gif.GifUtils;
import com.lib.imagelib.ImageEngine;
import com.lib.imagelib.utils.ImageUtil;

import java.io.File;

/**
 * Created by Administrator on 2017/5/1.
 */

public abstract class MyBaseBitmapDataSubscriber extends BaseDataSubscriber<CloseableReference<CloseableImage>> {

    String finalUrl; int width; int height;

    public MyBaseBitmapDataSubscriber(String finalUrl, int width, int height) {
        this.finalUrl = finalUrl;
        this.width = width;
        this.height = height;
    }

    @Override
    public void onNewResult(DataSource<CloseableReference<CloseableImage>> dataSource) {
        // isFinished() should be checked before calling onNewResultImpl(), otherwise
        // there would be a race condition: the final data source result might be ready before
        // we call isFinished() here, which would lead to the loss of the final result
        // (because of an early dataSource.close() call).
        final boolean shouldClose = dataSource.isFinished();
        try {
            onNewResultImpl(dataSource);
        } finally {
            if (shouldClose) {
               dataSource.close();
            }
        }
    }

    @Override
    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
        if(dataSource.getFailureCause()!=null){
            onFail(dataSource.getFailureCause());
        }else {
            onFail(new Throwable("unknown cause"));
        }


    }

    @Override
    public void onFailure(DataSource<CloseableReference<CloseableImage>> dataSource) {
        super.onFailure(dataSource);
    }

    @Override
    public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
        if (!dataSource.isFinished()) {
            return;
        }

        CloseableReference<CloseableImage> closeableImageRef = dataSource.getResult();
        Bitmap bitmap = null;
        if (closeableImageRef != null &&
                closeableImageRef.get() instanceof CloseableBitmap) {
            bitmap = ((CloseableBitmap) closeableImageRef.get()).getUnderlyingBitmap();
        }


        if(bitmap!=null ){
            if(bitmap.isRecycled()){
                onFail(new Throwable("bitmap.isRecycled"));
            }else {
                onNewResultImpl(bitmap,dataSource);
            }
            return;
        }

        //如果bitmap为空
        Log.e("onNewResultImpl","finalUrl :"+finalUrl);
        File cacheFile  = ImageEngine.getActualLoader().getFileFromDiskCache(finalUrl);
        if(cacheFile ==null){
            onFail(new Throwable("file cache is null:"+finalUrl));
            return;
        }
        //还要判断文件是不是gif格式的
        if (!"gif".equalsIgnoreCase(ImageUtil.getRealType(cacheFile))){
            onFail(new Throwable("file cache is not gif:"+finalUrl));
            return;
        }
        Bitmap bitmapGif = GifUtils.getBitmapFromGifFile(cacheFile);//拿到gif第一帧的bitmap
        if(width>0 && height >0) {
            bitmapGif = ImageUtil.compressBitmap(bitmapGif, false, width, height);//将bitmap压缩到指定宽高。
        }

        if (bitmapGif != null) {
            onNewResultImpl(bitmapGif,dataSource);
        } else {
            onFail(new Throwable("can not create bitmap from gif file:"+cacheFile.getAbsolutePath()));
        }




       /* try {
            onNewResultImpl(bitmap);
        } finally {
            //CloseableReference.closeSafely(closeableImageRef);
        }*/
    }

    /**
     * The bitmap provided to this method is only guaranteed to be around for the lifespan of the
     * method.
     *
     * <p>The framework will free the bitmap's memory after this method has completed.
     * @param bitmap

     */
    protected abstract void onNewResultImpl(@Nullable Bitmap bitmap,DataSource<CloseableReference<CloseableImage>> dataSource);

    protected abstract void onFail(Throwable e);
}
