package com.lib.imagelib.config;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import com.lib.imagelib.loader.GlideLoader;
import com.lib.imagelib.loader.ILoader;
import com.lib.imagelib.loader.ImageLoader;
import com.lib.imagelib.loader.PicassoLoader;

/**
 * Created by yzd on 2018/3/17 0017.
 */

public class GlobalConfig {

    public static String baseUrl;
    public static Context context;

    /**
     * 屏幕高度
     */
    private static int winHeight;

    /**
     * 屏幕宽度
     */
    private static int winWidth;

    /**
     * lrucache 最大值
     */
    public static int cacheMaxSize;

    public static Engine engineType;

    /**
     * https是否忽略校验,默认不忽略
     */
    public static boolean ignoreCertificateVerify = false;

    public static void init(Context context, int cacheSizeInM, int memoryMode, boolean isInternalCD, Engine engineType) {
        GlobalConfig.context = context;
        GlobalConfig.cacheMaxSize = cacheSizeInM;
        GlobalConfig.engineType = engineType;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        assert wm != null;
        wm.getDefaultDisplay().getSize(point);
        GlobalConfig.winWidth = point.x;
        GlobalConfig.winHeight = point.y;
        getLoader().init(context, cacheSizeInM, memoryMode, isInternalCD);
    }

    private static Handler mainHandler;

    public static Handler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }

    private static ILoader loader;

    public static ILoader getLoader() {

        if (loader == null) {
            switch (engineType) {
                case GLIDELOADER:
                    loader = new GlideLoader();
                    break;
                case IMAGELOADER:
                    loader = new ImageLoader();
                    break;
                case PICASSOLOADER:
                    loader = new PicassoLoader();
                    break;
                default:
                    loader = new GlideLoader();
                    break;
            }
        }

        return loader;
    }

    public static int getWinHeight() {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return winHeight < winWidth ? winHeight : winWidth;
        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return winHeight > winWidth ? winHeight : winWidth;
        }
        return winHeight;
    }

    public static int getWinWidth() {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return winHeight > winWidth ? winHeight : winWidth;
        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return winHeight < winWidth ? winHeight : winWidth;
        }
        return winWidth;
    }

    public enum Engine{
        GLIDELOADER, IMAGELOADER, PICASSOLOADER
    }
}
