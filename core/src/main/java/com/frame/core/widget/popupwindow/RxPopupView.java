package com.frame.core.widget.popupwindow;

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Vondear
 */
public class RxPopupView {

    public static final int POSITION_ABOVE = 0;
    public static final int POSITION_BELOW = 1;
    public static final int POSITION_LEFT_TO = 3;
    public static final int POSITION_RIGHT_TO = 4;
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int GRAVITY_CENTER = 0;
    public static final int GRAVITY_LEFT = 1;
    public static final int GRAVITY_RIGHT = 2;
    private Context mContext;
    private View mAnchorView;
    private ViewGroup mRootViewGroup;
    private View mCustomView;
    private String mMessage;
    private @Position int mPosition;
    private @Align int mAlign;
    private int mOffsetX;
    private int mOffsetY;
    private boolean mArrow;
    private int mBackgroundColor;
    private float mElevation;
    public RxPopupView(Builder builder){
        mContext = builder.mContext;
        mAnchorView = builder.mAnchorView;
        mRootViewGroup = builder.mRootViewGroup;
        mPosition = builder.mPosition;
        mAlign = builder.mAlign;
        mOffsetX = builder.mOffsetX;
        mOffsetX = builder.mOffsetX;
        mOffsetY = builder.mOffsetY;
        mArrow = builder.mArrow;
        mCustomView = builder.mCustomView;
        mBackgroundColor = builder.mBackgroundColor;
        mElevation = builder.mElevation;
    }

    public Context getContext() {
        return mContext;
    }

    public View getAnchorView() {
        return mAnchorView;
    }

    public ViewGroup getRootView() {
        return mRootViewGroup;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(@Position int position) {
        mPosition = position;
    }

    public int getAlign() {
        return mAlign;
    }

    public int getOffsetX() {
        return mOffsetX;
    }

    public int getOffsetY() {
        return mOffsetY;
    }

    public boolean hideArrow() {
        return !mArrow;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public boolean positionedLeftTo(){
        return POSITION_LEFT_TO == mPosition;
    }

    public boolean positionedRightTo(){
        return POSITION_RIGHT_TO == mPosition;
    }

    public boolean positionedAbove(){
        return POSITION_ABOVE == mPosition;
    }

    public boolean positionedBelow(){
        return POSITION_BELOW == mPosition;
    }

    public boolean alignedCenter(){
        return ALIGN_CENTER == mAlign;
    }

    public boolean alignedLeft(){
        return ALIGN_LEFT == mAlign;
    }

    public boolean alignedRight(){
        return ALIGN_RIGHT == mAlign;
    }

    public float getElevation() {
        return mElevation;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POSITION_ABOVE, POSITION_BELOW, POSITION_LEFT_TO, POSITION_RIGHT_TO})
    public @interface Position {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ALIGN_CENTER, ALIGN_LEFT, ALIGN_RIGHT})
    public @interface Align {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GRAVITY_CENTER, GRAVITY_LEFT, GRAVITY_RIGHT})
    public @interface Gravity {
    }

    public static class Builder {
        private Context mContext;
        private View mAnchorView;
        private ViewGroup mRootViewGroup;
        private View mCustomView;
        private @Position int mPosition;
        private @Align int mAlign;
        private int mOffsetX;
        private int mOffsetY;
        private boolean mArrow;
        private int mBackgroundColor;
        private float mElevation;

        /**
         * @param context    context
         * @param anchorView the view which near it we want to put the tip
         * @param root       a class extends ViewGroup which the created tip view will be added to
         * @param position   put the tip above / below / left to / right to
         */
        public Builder(Context context, View anchorView, ViewGroup root, View view, @Position int position) {
            mContext = context;
            mAnchorView = anchorView;
            mRootViewGroup = root;
            mPosition = position;
            mCustomView = view;
            mAlign = ALIGN_CENTER;
            mOffsetX = 0;
            mOffsetY = 0;
            mArrow = true;
        }

        public Builder setView(View view) {
            mCustomView = view;
            return this;
        }

        public Builder setPosition(@Position int position){
            mPosition = position;
            return this;
        }

        public Builder setAlign(@Align int align){
            mAlign = align;
            return this;
        }

        /**
         * @param offset offset to move the tip on x axis after tip was positioned
         * @return offset
         */
        public Builder setOffsetX(int offset){
            mOffsetX = offset;
            return this;
        }

        /**
         * @param offset offset to move the tip on y axis after tip was positioned
         * @return offset
         */
        public Builder setOffsetY(int offset){
            mOffsetY = offset;
            return this;
        }

        public Builder withArrow(boolean value){
            mArrow = value;
            return this;
        }

        public Builder setBackgroundColor(int color){
            mBackgroundColor = color;
            return this;
        }

        public Builder setElevation(float elevation){
            mElevation = elevation;
            return this;
        }

        public RxPopupView build(){
            return new RxPopupView(this);
        }

    }
}
