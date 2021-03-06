package com.frame.core.util.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * <pre>
 *     author: admin
 *     blog  : http://core.frame.com
 *     time  : 2016/10/09
 *     desc  : utils about close(关闭工具)
 *     menu
 *          closeIO       : 1.关闭 IO
            closeIOQuietly: 2.安静关闭 IO
 * </pre>
 */
public final class CloseUtils {

    private CloseUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Close the io stream.
     *
     * @param closeables closeables
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Close the io stream quietly.
     *
     * @param closeables closeables
     */
    public static void closeIOQuietly(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
