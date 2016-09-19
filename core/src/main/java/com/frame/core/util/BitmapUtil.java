package com.frame.core.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * bitmap工具类
 * Created by Administrator on 2016/1/9.
 */
public class BitmapUtil {

    /**
     * drawable 转 bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap 转 bytes
     *
     * @param bm
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm, Bitmap.CompressFormat format) {
        byte[] bytes = null;
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(format, 100, baos);
            bytes = baos.toByteArray();
        }
        return bytes;
    }

    /**
     * 重新调整 bitmap 大小
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    /**
     * 保存图片
     * @param context
     * @param bitmap
     * @param format
     * @param appDir
     * @param imageName
     * @return
     */
    public static boolean saveImage(Context context, Bitmap bitmap, Bitmap.CompressFormat format
            , File appDir, String imageName) {
        if (bitmap == null) return false;
        File file = new File(appDir, imageName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(format, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                , Uri.parse("file://"+file.getAbsolutePath())));
        return true;
    }

    /**
     * 根据一个网络连接(String)获取bitmap图像
     * @param imageUri
     */
    public static Bitmap getbitmap(String imageUri) {
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 图片压缩
     * @param bitmap 图片
     * @param wantSize 期望的图片大小
     * @return
     */
    public static byte[] zipBitmap(Bitmap bitmap, Bitmap.CompressFormat format, int wantSize) {
        byte[] imgBytes;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(format, 100, out);
            int option = 100;
            while (out.toByteArray().length / 1024 > wantSize) {
                option -= 10;
                if (option <= 0) {
                    break;
                }
                out.reset();
                bitmap.compress(format, option, out);
            }
            imgBytes = out.toByteArray();
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
            imgBytes = new byte[]{};
        }
        return imgBytes;
    }

    public static Bitmap decodeBitmapFromResource(Bitmap.Config config, Resources mResources) {
        return null;
    }

    /**
     * 将文本变为bitmap
     *
     * @param message
     * @param width
     * @param textSize
     * @param lines
     * @return
     */
    private static Bitmap drawTextToBitmap(String message, int width, float textSize, int lines) {
        int height = (int) textSize * lines + 4 * (lines - 1);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.create("System", Typeface.BOLD));
        StaticLayout sl = new StaticLayout(message
                , textPaint, bitmap.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.translate(0, 0);
        sl.draw(canvas);
        return bitmap;
    }

    /**
     * 计算，缩放bitmap
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options
            , int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
     *
     * A.网络路径: url=&quot;http://blog.foreverlove.us/girl2.png&quot; ;
     *
     * B.本地路径:url=&quot;file://mnt/sdcard/photo/image.png&quot;;
     *
     * C.支持的图片格式 ,png, jpg,bmp,gif等等
     *
     * @param url
     * @return
     */
    /*public static Bitmap GetLocalOrNetBitmap(String url)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), Constant.IO_BUFFER_SIZE);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, Constant.IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }*/
}
