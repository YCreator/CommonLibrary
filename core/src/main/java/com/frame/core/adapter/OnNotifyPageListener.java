package com.frame.core.adapter;

/**
 * Created by yzd on 2017/7/26 0026.
 */

public interface OnNotifyPageListener {

    void emptyDataListListener(boolean isEmpty);

    void requestNextPageData();

    void showUpToTopFunction(boolean canUpPosition);
}
