package com.frame.core.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by yzd on 2017/8/16 0016.
 * 兼容7.0popuwindow位置显示问题
 */

public class CommonPopuWindow extends PopupWindow {

    public CommonPopuWindow(Context context) {
        super(context);
    }

    public CommonPopuWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonPopuWindow(View contentView) {
        super(contentView);
    }

    public CommonPopuWindow(int width, int height) {
        super(width, height);
    }

    public CommonPopuWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CommonPopuWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CommonPopuWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public CommonPopuWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);

    }
}
