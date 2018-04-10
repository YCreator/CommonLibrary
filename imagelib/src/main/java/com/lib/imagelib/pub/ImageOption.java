package com.lib.imagelib;

import android.support.annotation.DrawableRes;

/**
 * Created by yzd on 2018/3/17 0017.
 */

public class ImageOption {

    private @DrawableRes int loadingShow;
    private @DrawableRes int errorShow;
    private boolean cacheInMemory;
    private boolean cacheOnDisk;

    public int getLoadingShow() {
        return loadingShow;
    }

    public void setLoadingShow(int loadingShow) {
        this.loadingShow = loadingShow;
    }

    public int getErrorShow() {
        return errorShow;
    }

    public void setErrorShow(int errorShow) {
        this.errorShow = errorShow;
    }

    public boolean isCacheInMemory() {
        return cacheInMemory;
    }

    public void setCacheInMemory(boolean cacheInMemory) {
        this.cacheInMemory = cacheInMemory;
    }

    public boolean isCacheOnDisk() {
        return cacheOnDisk;
    }

    public void setCacheOnDisk(boolean cacheOnDisk) {
        this.cacheOnDisk = cacheOnDisk;
    }
}
