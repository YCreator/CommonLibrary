package com.frame.core.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.frame.core.util.TLog;

/**
 * Created by yzd on 2016/7/29.
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {

    /*
    * RecyclerView的布局方向，默认先赋值
    * 为纵向布局
    * RecyclerView 布局可横向，也可纵向
    * 横向和纵向对应的分割想画法不一样
    * */
    private int mOrientation = LinearLayoutManager.VERTICAL;

    /**
     * item之间分割线的size，默认为1
     */
    private int mItemSize = 1;

    private boolean drawFootDividerable = false;

    private int spanCount;

    /**
     * 绘制item分割线的画笔，和设置其属性
     * 来绘制个性分割线
     */
    private Paint mPaint;


    public GridDividerItemDecoration(Context context, int mOrientation, int spanCount, float dp) {
        this(context, mOrientation, spanCount, dp, Color.GRAY);
    }

    public GridDividerItemDecoration(Context context, int mOrientation, int spanCount, float dp, @ColorInt int color) {
        this.mOrientation = mOrientation;
        this.spanCount = spanCount;
        if (mOrientation != LinearLayoutManager.VERTICAL && mOrientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请传入正确的参数");
        }
        mItemSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp
                , context.getResources().getDisplayMetrics()) / 2);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public GridDividerItemDecoration(Context context, int mOrientation, float dp, @ColorInt int color, boolean drawFootDividerable) {
        this.mOrientation = mOrientation;
        this.drawFootDividerable = drawFootDividerable;
        if (mOrientation != LinearLayoutManager.VERTICAL && mOrientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请传入正确的参数");
        }
        mItemSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp
                , context.getResources().getDisplayMetrics());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) manager).getSpanCount();
        } else {
            spanCount = 1;
        }
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    /**
     * 设置item分割线的size
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int span = 1;
        if (manager instanceof GridLayoutManager) {
            span = ((GridLayoutManager) manager).getSpanCount();
        }
        int childSize = parent.getChildCount();
        int size = parent.getChildCount() / span + ((parent.getChildCount() % span) > 0 ? 1 : 0);
        int position = parent.getChildLayoutPosition(view);
        if (position + 1 <= spanCount) {
            if (position == 0) {
                outRect.set(0, 0, mItemSize, mItemSize);
            } else if (position == spanCount - 1) {
                outRect.set(mItemSize, 0, 0, mItemSize);
            } else {
                outRect.set(mItemSize, 0, mItemSize, mItemSize);
            }
        }
        outRect.top = 0;
        outRect.left = 0;
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            if (!drawFootDividerable && (position + 1) / span + (((position + 1) % span) > 0 ? 1 : 0) == size) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, 0, mItemSize);
            }
        } else {
            if (!drawFootDividerable && (position + 1) % span == 0) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, mItemSize, 0);
            }
        }
    }

    /**
     * 绘制纵向 item 分割线
     *
     * @param canvas
     * @param parent
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        final int size = childSize / spanCount + (childSize % spanCount > 0 ? 1 : 0);
        TLog.i("drawsVertical", childSize, drawFootDividerable, mPaint.getColor());
        for (int i = 0; i < size; i++) {
            if (!drawFootDividerable && i == size - 1) {
                continue;
            }
            TLog.i("drawsVertical", i, mPaint.getColor());
            final View child = parent.getChildAt(i * spanCount);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mItemSize;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    /**
     * 绘制横向 item 分割线
     *
     * @param canvas
     * @param parent
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = spanCount == 1 ? parent.getChildCount() : spanCount;
        TLog.i("drawsHorizontal", childSize, drawFootDividerable, mPaint.getColor());
        for (int i = 0; i < childSize; i++) {
            if (!drawFootDividerable && (i == childSize - 1)) {
                continue;
            }
            TLog.i("drawsHorizontal", i, mPaint.getColor());
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mItemSize;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
