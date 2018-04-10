package com.lib.tpl.share;

import com.umeng.socialize.media.UMWeb;

/**
 * Created by yzd on 2018/4/9 0009.
 */

public class ShareWeb {
    UMWeb web;

    public ShareWeb(String url) {
        web = new UMWeb(url);
    }

    public void setTitle(String title) {
        web.setTitle(title);
    }

    public void setDescription(String description) {
        web.setDescription(description);
    }

    public void setThumb(ShareImage img) {
        web.setThumb(img.image);
    }
}
