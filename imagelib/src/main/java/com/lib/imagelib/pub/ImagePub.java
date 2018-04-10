package com.lib.imagelib;

import android.content.Context;

/**
 * Created by yzd on 2018/3/16 0016.
 */

public final class ImagePub {

    public static Context mContext;

    public static void init(Context context, ImageFactory.ImageLib lib) {
        mContext = context;
        ImageFactory.getInstance().init(lib);
    }

    public static ImageFactory start() {
        return ImageFactory.getInstance().initParam();
    }
}
