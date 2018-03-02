package com.frame.core.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
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
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.frame.core.BaseApplication;

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
public class BitmapUtils {

    /**
     * 压缩显示bitmap，防止图片过大引起内存溢出
     * @param is
     * @return
     */
    public static Bitmap zipToShowBitmap(InputStream is) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTempStorage = new byte[100 * 1024];
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inPurgeable = true;
        opts.inSampleSize = 4;
        opts.inInputShareable = true;
        return BitmapFactory.decodeStream(is, null, opts);
    }

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
     *
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
                , Uri.parse("file://" + file.getAbsolutePath())));
        return true;
    }

    /**
     * 根据一个网络连接(String)获取bitmap图像
     *
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
     *
     * @param bitmap   图片
     * @param wantSize 期望的图片大小  k
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
     * 模糊图片的具体方法
     *
     * @param context 上下文对象
     * @param image   需要模糊的图片
     * @return 模糊处理后的图片
     */
    public static Bitmap blurBitmap(Context context, Bitmap image, float blurRadius, float scale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // 计算图片缩小后的长宽
            int width = Math.round(image.getWidth() * scale);
            int height = Math.round(image.getHeight() * scale);
            // 将缩小后的图片做为预渲染的图片
            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
            // 创建一张渲染后的输出图片
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
            // 创建RenderScript内核对象
            RenderScript rs = RenderScript.create(context);

            // 创建一个模糊效果的RenderScript的工具对象
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
            // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

            // 设置渲染的模糊程度, 25f是最大模糊度
            blurScript.setRadius(blurRadius);
            // 设置blurScript对象的输入内存
            blurScript.setInput(tmpIn);
            // 将输出数据保存到输出内存中
            blurScript.forEach(tmpOut);

            // 将数据填充到Allocation中
            tmpOut.copyTo(outputBitmap);

            return outputBitmap;
        }

        return image;
    }

    /**
     * 从Assets中读取图片
     * @param fileName
     * @return
     */
    public static Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = BaseApplication.get_asset();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return image;

    }

    /**
     * 柔化效果(高斯模糊)(优化后比上面快三倍)
     * @param bmp
     * @return
     */
    public static Bitmap blurImageAmeliorate(Bitmap bmp)
    {
        long start = System.currentTimeMillis();
        // 高斯矩阵
        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int delta = 16; // 值越小图片会越亮，越大则越暗

        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++)
        {
            for (int k = 1, len = width - 1; k < len; k++)
            {
                idx = 0;
                for (int m = -1; m <= 1; m++)
                {
                    for (int n = -1; n <= 1; n++)
                    {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * gauss[idx]);
                        newG = newG + (int) (pixG * gauss[idx]);
                        newB = newB + (int) (pixB * gauss[idx]);
                        idx++;
                    }
                }

                newR /= delta;
                newG /= delta;
                newB /= delta;

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        Log.d("may", "used time="+(end - start));
        return bitmap;
    }
}
