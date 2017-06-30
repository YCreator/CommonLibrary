package com.frame.core.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * 机身存储与外置存储工具
 * Created by Administrator on 2016/1/5.
 */
public class StorageUtils {

    /**
     * 判断是否有外置存储
     *
     * @return
     */
    public static boolean isSDcardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取sd卡的绝对路劲
     *
     * @return
     */
    public static String getSDdir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取/data/data//cache目录
     *
     * @return
     */
    public static File getCacheDir(@NonNull Context context) {
        return context.getApplicationContext().getCacheDir();
    }

    /**
     * 获取/data/data//files目录
     *
     * @return
     */
    public static File getFilesDir(@NonNull Context context) {
        return context.getApplicationContext().getFilesDir();
    }

    public static boolean isExternalStorageDocument(@NonNull Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(@NonNull Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(@NonNull Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(@NonNull Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static File getOwnFilesDirectory(Context context, String cacheDir) {
        File appCacheDir;
        if (isSDcardExist()) {
            appCacheDir = context.getExternalFilesDir(cacheDir);
        } else {
            appCacheDir = new File(context.getFilesDir(),cacheDir);
        }
        if (appCacheDir == null) {
            if (isSDcardExist()) {
                appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
            } else {
                appCacheDir = new File(Environment.getRootDirectory(), cacheDir);
            }
        }
        if (!appCacheDir.exists()) {
            appCacheDir.mkdirs();
        }
        return appCacheDir;
    }

    public static File getOwnDataDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        if (isSDcardExist()) {
            appCacheDir = new File(context.getExternalCacheDir(),cacheDir);
            if (!appCacheDir.exists()) {
                appCacheDir.mkdirs();
            }
        } else {
            appCacheDir = new File(context.getCacheDir(),cacheDir);
            if (!appCacheDir.exists()) {
                appCacheDir.mkdirs();
            }
        }
        return appCacheDir;
    }

}
