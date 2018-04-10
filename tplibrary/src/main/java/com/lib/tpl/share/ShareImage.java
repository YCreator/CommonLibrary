package com.lib.tpl.share;

import android.content.Context;
import android.support.annotation.DrawableRes;

import com.umeng.socialize.media.UMImage;

/**
 * Created by yzd on 2018/4/9 0009.
 */

public class ShareImage {
    UMImage image;

    public ShareImage(Context context, @DrawableRes int id) {
        image = new UMImage(context, id);
    }

    public ShareImage(Context context,  String url) {
        image = new UMImage(context, url);
    }

    public void setThumb(ShareImage thumb) {
        image.setThumb(thumb.image);
    }

}
