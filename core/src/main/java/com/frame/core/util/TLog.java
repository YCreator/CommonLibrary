package com.frame.core.util;

import android.util.Log;

import com.frame.core.BaseApplication;

/**
 * 日志打印工具
 * Created by Administrator on 2015/12/2.
 */
public class TLog {
    public static boolean DEBUG = BaseApplication.DEBUG;
    private static final String LOG_TAG = "SIMICO";

    public static void analytics(String paramString) {
        if (DEBUG)
            Log.d(LOG_TAG, paramString);
    }

    public static void e(String paramString) {
        if (DEBUG)
            Log.e(LOG_TAG, paramString);
    }

    public static void i(String paramString) {
        if (DEBUG)
            Log.i(LOG_TAG, paramString);
    }

    public static void i(String paramString1, String paramString2) {
        if (DEBUG)
            Log.i(paramString1, paramString2);
    }

    public static void v(String paramString) {
        if (DEBUG)
            Log.v(LOG_TAG, paramString);
    }

    public static void w(String paramString) {
        if (DEBUG)
            Log.w(LOG_TAG, paramString);
    }
}
