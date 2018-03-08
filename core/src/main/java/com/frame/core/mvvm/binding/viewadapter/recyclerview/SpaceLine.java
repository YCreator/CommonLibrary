package com.frame.core.mvvm.binding.viewadapter.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yzd on 2018/3/8 0008.
 */

public class SpaceLine extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceLine(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.top = space;
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
    }
}
