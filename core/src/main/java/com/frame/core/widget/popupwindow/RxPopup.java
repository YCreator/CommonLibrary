package com.frame.core.widget.popupwindow;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yzd on 2018/4/23 0023.
 */

public class RxPopup {

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

    private FrameLayout mView;
    private Context mContext;
    private @Align
    int mAlign;
    private @ColorInt
    int mColor;
    private @Position
    int mPosition;

    private EasyPopup easyPopup;

    public RxPopup(Context context) {
        this.mContext = context;
        this.mView = new FrameLayout(context);
        this.mView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public RxPopup setView(View customView) {
        mView.addView(customView);
        return this;
    }

    public RxPopup setBackgroundColor(@ColorInt int color) {
        this.mColor = color;
        return this;
    }

    public RxPopup setAlign(@Align int align) {
        this.mAlign = align;
        return this;
    }

    public RxPopup setPosition(@Position int position) {
        this.mPosition = position;
        return this;
    }

    public EasyPopup createPopup() {
        RxPopupBackgroundConstructor.setBackground(mView, this);
        easyPopup = new EasyPopup(mContext)
                .setContentView(mView);
        return easyPopup;
    }

    public void showAtAnchorView(View view, int offsetX, int offsetY) {
        switch (mPosition) {
            case RxPopup.POSITION_ABOVE:
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                easyPopup.getPopupWindow()
                        .showAtLocation(view, android.view.Gravity.NO_GRAVITY, location[0] + offsetX, location[1] - view.getHeight() - offsetY);
                break;
            case RxPopup.POSITION_BELOW:
                easyPopup.showAsDropDown(view, offsetX, offsetY);
                break;
            case RxPopup.POSITION_LEFT_TO:
                int[] location1 = new int[2];
                view.getLocationOnScreen(location1);
                easyPopup.getPopupWindow()
                        .showAtLocation(view, android.view.Gravity.NO_GRAVITY, location1[0] - easyPopup.getPopupWindow().getWidth(), location1[1] + offsetY);
                break;
            case RxPopup.POSITION_RIGHT_TO:
                int[] location2 = new int[2];
                view.getLocationOnScreen(location2);
                easyPopup.getPopupWindow()
                        .showAtLocation(view, android.view.Gravity.NO_GRAVITY, location2[0] + easyPopup.getPopupWindow().getWidth(), location2[1] + offsetY);
                break;
        }
    }

    public FrameLayout getView() {
        return mView;
    }

    public Context getContext() {
        return mContext;
    }

    public int getAlign() {
        return mAlign;
    }

    public int getBackgroundColor() {
        return mColor;
    }

    public int getPosition() {
        return mPosition;
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
}
