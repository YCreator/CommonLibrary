package com.lib.sharelib;

import com.umeng.socialize.media.UMImage;

import java.io.Serializable;

/**
 * Created by yzd on 2017/2/10 0010.
 */

public class ShareEntity implements Serializable {

    private UMImage image;
    private String title;
    private String targetUrl;
    private String text;

    public ShareEntity() {}

    public ShareEntity(String title, String targetUrl, String text, UMImage image) {
        this.image = image;
        this.title = title;
        this.targetUrl = targetUrl;
        this.text = text;
    }

    public UMImage getImage() {
        return image;
    }

    public void setImage(UMImage image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
