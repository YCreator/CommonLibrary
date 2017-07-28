package com.frame.core.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Tools for handler picture
 */
public final class ImageTools {

    /**
     * Transfer drawable to bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                : Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Bitmap to drawable
     *
     * @param bitmap
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    /**
     * Input stream to bitmap
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static Bitmap inputStreamToBitmap(InputStream inputStream)
            throws Exception {
        return BitmapFactory.decodeStream(inputStream);
    }

    /**
     * Byte transfer to bitmap
     *
     * @param byteArray
     * @return
     */
    public static Bitmap byteToBitmap(byte[] byteArray) {
        if (byteArray.length != 0) {
            return BitmapFactory
                    .decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            return null;
        }
    }

    /**
     * Byte transfer to drawable
     *
     * @param byteArray
     * @return
     */
    public static Drawable byteToDrawable(byte[] byteArray) {
        ByteArrayInputStream ins = null;
        if (byteArray != null) {
            ins = new ByteArrayInputStream(byteArray);
        }
        return Drawable.createFromStream(ins, null);
    }

    /**
     * Bitmap transfer to bytes
     *
     * @param bm
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        byte[] bytes = null;
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bytes = baos.toByteArray();
        }
        return bytes;
    }

    /**
     * Drawable transfer to bytes
     *
     * @param drawable
     * @return
     */
    public static byte[] drawableToBytes(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        byte[] bytes = bitmapToBytes(bitmap);
        return bytes;
    }

    /**
     * Base64 to byte[]
     //	 */
//	public static byte[] base64ToBytes(String base64) throws IOException {
//		byte[] bytes = Base64.decode(base64);
//		return bytes;
//	}
//
//	/**
//	 * Byte[] to base64
//	 */
//	public static String bytesTobase64(byte[] bytes) {
//		String base64 = Base64.encode(bytes);
//		return base64;
//	}

    /**
     * Create reflection images
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * Get rounded corner images
     *
     * @param bitmap
     * @param roundPx 5 10
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * Resize the bitmap
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
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * Resize the drawable
     *
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        matrix.postScale(sx, sy);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(newbmp);
    }

    /**
     * Get images from SD card by path and the name of image
     *
     * @param photoName
     * @return
     */
    public static Bitmap getPhotoFromSDCard(String path, String photoName) {
        Bitmap photoBitmap = BitmapFactory.decodeFile(path + "/" + photoName + ".png");
        if (photoBitmap == null) {
            return null;
        } else {
            return photoBitmap;
        }
    }

    /**
     * Check the SD card
     *
     * @return
     */
    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * Get image from SD card by path and the name of image
     *
     * @param path
     * @param photoName
     * @return
     */
    public static boolean findPhotoFromSDCard(String path, String photoName) {
        boolean flag = false;

        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (dir.exists()) {
                File folders = new File(path);
                File photoFile[] = folders.listFiles();
                for (int i = 0; i < photoFile.length; i++) {
                    String fileName = photoFile[i].getName().split("\\.")[0];
                    if (fileName.equals(photoName)) {
                        flag = true;
                    }
                }
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static void savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File photoFile = new File(path, photoName + ".png");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
//						fileOutputStream.close();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Delete the image from SD card
     *
     * @param path file:///sdcard/temp.jpg
     */
    public static void deleteAllPhoto(String path) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }

    public static void deletePhotoAtPathAndName(String path, String fileName) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i].getName());
                if (files[i].getName().equals(fileName)) {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
    * 旋转图片
    * @param angle
    * @param bitmap
    * @return Bitmap
    */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle, bitmap.getWidth(), bitmap.getHeight());
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap b1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return b1;
    }

    /**
     * 旋转Bitmap
     *
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
    }

    /**
     * 保存图片到指定路径
     *
     * @param context
     * @param bitmap
     * @param path
     */
    public synchronized static boolean saveImageToMe(Context context, Bitmap bitmap, String path) {
        String filename = System.currentTimeMillis() + ".jpg";
        return saveImageToMe(context, bitmap, path, filename);
    }

    public static boolean saveImage(Context context, Bitmap bitmap, File appDir, String imageName) {
        if (bitmap == null) return false;
        //File appDir = FileTools.instance().getChileFile(path);
        File file = new File(appDir, imageName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        /*context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
				, Uri.parse("file://"+file.getAbsolutePath())));*/
        return true;
    }

    public static boolean saveImage(Context context, Bitmap bitmap, String picPath) {
        if (bitmap == null) return false;
        File file = new File(picPath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
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

    public static boolean saveImage(Context context, Bitmap bitmap, String path, String imageName) {
        if (bitmap == null) return false;
        File file = new File(new File(path), imageName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
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
     * 保存图片到指定路径并保存到相册
     *
     * @param context
     * @param bitmap
     * @param path
     */
    public synchronized static boolean saveImageToMe(Context context, Bitmap bitmap, String path, String imageName) {
        if (bitmap == null) return false;
        File file = new File(new File(path), imageName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver()
                    , file.getAbsolutePath(), imageName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                , Uri.parse("file://" + file.getAbsolutePath())));
        return true;
    }

    public static void intentImgLib(Activity activity) {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(intent, 2);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            activity.startActivityForResult(intent, 3);
        }
    }

    // 截取图片 兼容大小图片裁剪,大图片不返回bitmap,而是返回uri来获取截取的图片,配置如下
    @SuppressLint("SdCardPath")
    public static void cropImage(Uri uri, int outputX, int outputY, int requestCode, Activity activity) {
	/*	File temp = new File(FileTools.instance().getChileFile(Constants.TEMP),"temp.jpg");
		if (!temp.exists()) {
			temp.mkdir();
		}*/
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("scale", false);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
    }

    // 计算，缩放bitmap
    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

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
     * @param path     图片路径
     * @param wantSize 期望的图片大小
     * @return
     */
    public static byte[] zipImage(String path, int wantSize) {
        byte[] imgBytes;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap == null) {
                TLog.i("bitmap========null");
                return new byte[]{};
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            int option = 100;
            while (out.toByteArray().length / 1024 > wantSize) {
                option -= 10;
                if (option <= 0) {
                    break;
                }
                out.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, option, out);
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

    public static byte[] zipBitmap(Bitmap bitmap, int wantSize) {
        byte[] imgBytes;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            int option = 100;
            while (out.toByteArray().length / 1024 > wantSize) {
                option -= 10;
                if (option <= 0) {
                    break;
                }
                out.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, option, out);
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

    public static Bitmap getBitmapFromOther(Context context, String imgName) {
        InputStream is = null;
        try {
            is = context.getAssets().open("banks/" + imgName);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap decodeBitmap(Bitmap bitmap, int displayWidth, int displayHeight) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true; //表示我们只读取Bitmap的宽高等信息，不读取像素。
        return Bitmap.createScaledBitmap(bitmap, displayWidth, displayHeight, true);
    }

}
