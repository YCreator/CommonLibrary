package com.frame.core.widget.popupwindow;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by san on 2017/8/22
 */

@IntDef({
        HorizontalGravity.CENTER,
        HorizontalGravity.LEFT,
        HorizontalGravity.RIGHT,
        HorizontalGravity.ALIGN_LEFT,
        HorizontalGravity.ALIGN_RIGHT,
})
@Retention(RetentionPolicy.SOURCE)
public @interface HorizontalGravity {
    int CENTER = 0;
    int LEFT = 1;
    int RIGHT = 2;
    int ALIGN_LEFT = 3;
    int ALIGN_RIGHT = 4;
}