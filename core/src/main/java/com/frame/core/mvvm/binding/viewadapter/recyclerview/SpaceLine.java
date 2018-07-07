package com.frame.core.mvvm.binding.viewadapter.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yzd on 2018/3/8 0008.
 */

public class SpaceLine extends RecyclerView.ItemDecoration {
    private int space;

    private LineSpaceMode mMode = null;

    public enum LineSpaceMode {
        TOP, RIGHT, BOTTOM, LEFT, ALL
    }

    public SpaceLine(int space) {
        this.space = space;
        this.mMode = LineSpaceMode.ALL;
    }

    public SpaceLine(int space, LineSpaceMode mode) {
        this.space = space;
        this.mMode = mode;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        switch (mMode) {
            case TOP:
                outRect.top = space;
                break;
            case RIGHT:
                outRect.right = space;
                break;
            case BOTTOM:
                outRect.bottom = space;
                break;
            case LEFT:
                outRect.left = space;
                break;
            default:
                outRect.top = space;
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
                break;
        }

    }
}
