package com.frame.core.util;

import android.util.Log;

import com.frame.core.BaseApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 日志打印工具
 * Created by Administrator on 2015/12/2.
 * please use com.frame.core.util.utils.LogUtils
 */
@Deprecated
public class TLog {
    public static boolean DEBUG = BaseApplication.isDebug();
    private static final String LOG_TAG = "SIMICO";

    public static void analytics(String paramString) {
        if (DEBUG)
            Log.d(LOG_TAG, StringUtils.isEmpty(paramString) ? "param is empty" : paramString);
    }

    public static void analytics(String paramString1, String paramString) {
        if (DEBUG)
            Log.d(paramString1, StringUtils.isEmpty(paramString) ? "param is empty" : paramString);
    }

    public static void e(String paramString) {
        if (DEBUG)
            Log.e(LOG_TAG, StringUtils.isEmpty(paramString) ? "param is empty" : paramString);
    }

    public static void i(String paramString) {
        if (DEBUG)
            Log.i(LOG_TAG, StringUtils.isEmpty(paramString) ? "param is empty" : paramString);
    }

    public static void i(String paramString1, String paramString2) {
        if (paramString1 == null) paramString1 = "";
        if (paramString2 == null) paramString2 = "";
        if (DEBUG)
            Log.i(paramString1, paramString2);
    }

    public static void i(String paramString1, Object... paramString2) {
        if (DEBUG) {
            StringBuilder builder = new StringBuilder();
            for (Object param : paramString2) {
                builder.append(param).append("_");
            }
            Log.i(paramString1, builder.toString());
        }
    }

    public static void v(String paramString) {
        if (DEBUG)
            Log.v(LOG_TAG, StringUtils.isEmpty(paramString) ? "param is empty" : paramString);
    }

    public static void w(String paramString) {
        if (DEBUG)
            Log.w(LOG_TAG, StringUtils.isEmpty(paramString) ? "param is empty" : paramString);
    }

    public static void saveLog(File path) {
        try {
            java.lang.Process p = Runtime.getRuntime().exec("logcat");
            final InputStream is = p.getInputStream();
            new Thread() {
                @Override
                public void run() {
                    FileOutputStream os = null;
                    try {
                        os = new FileOutputStream(path);
                        int len = 0;
                        byte[] buf = new byte[1024];
                        while (-1 != (len = is.read(buf))) {
                            os.write(buf, 0, len);
                            os.flush();
                        }
                    } catch (Exception e) {
                        Log.d("writelog", "read logcat process failed. message: " + e.getMessage());
                    } finally {
                        if (null != os) {
                            try {
                                os.close();
                                os = null;
                            } catch (IOException e) {
                                // Do nothing
                            }
                        }
                    }
                }
            }.start();
        } catch (Exception e) {
            Log.d("writelog", "open logcat process failed. message: " + e.getMessage());
        }
    }
}
