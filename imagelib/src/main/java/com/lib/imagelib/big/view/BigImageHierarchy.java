package com.lib.imagelib.big.view;

import java.io.File;

/**
 * Created by yzd on 2018/5/10 0010.
 */

public interface BigImageHierarchy {
    public void showContent(File image);
    public void showProgress(int progress);
    public void showError();
    public void showThumbnail();

    void onStart();
}
