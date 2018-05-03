package com.frame.core.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;

/**
 * 文字自动滚动TextView
 * Created by yzd on 2016/10/8.
 */

public class AutoScrollTextView extends AppCompatTextView {

    public final static String TAG = AutoScrollTextView.class.getSimpleName();

    private float textLength = 0f;//文本长度
    private float viewWidth = 0f;
    private float step = 0f;//文字的横坐标
    private float y = 0f;//文字的纵坐标
    private float speed = 0.5f;//文字滚动速度
    private float temp_view_plus_text_length = 0.0f;//用于计算的临时变量
    private float temp_view_plus_two_text_length = 0.0f;//用于计算的临时变量
    public boolean isStarting = false;//是否开始滚动
    private Paint paint = null;//绘图样式
    private String text = "";//文本内容
    private int color = 0xff000000;
    private boolean isFirstFinished = false;

    public AutoScrollTextView(Context context) {
        super(context);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(WindowManager windowManager) {
        paint = getPaint();
        paint.setColor(color);
        text = getText().toString();
        textLength = paint.measureText(text);
        viewWidth = getWidth();
        if (viewWidth == 0) {
            if (windowManager != null) {
                Display display = windowManager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                viewWidth = point.x;
            }
        }
        step = textLength;
        //temp_view_plus_text_length = viewWidth + textLength;
        temp_view_plus_text_length = textLength;
        //temp_view_plus_two_text_length = viewWidth + textLength * 2;
        temp_view_plus_two_text_length = textLength * 2;
        y = getTextSize() + getPaddingTop();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.step = step;
        ss.isStarting = isStarting;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        step = ss.step;
        isStarting = ss.isStarting;
    }

    private static class SavedState extends BaseSavedState {
        boolean isStarting = false;
        float step = 0.0f;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(new boolean[]{isStarting});
            out.writeFloat(step);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
        };

        private SavedState(Parcel in) {
            super(in);
            boolean[] b = new boolean[1];
            in.readBooleanArray(b);
            isStarting = b[0];
            step = in.readFloat();
        }
    }

    public void startScroll() {
        if (textLength >= viewWidth) {
            isStarting = true;
            invalidate();
        }
    }

    public void startScrollDelayed(long delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startScroll();
            }
        }, delayMillis);
    }

    public void stopScroll() {
        isStarting = false;
        invalidate();
    }

    public void setTextScroolSpeed(float speed) {
        this.speed = speed;
    }

    public void setScrollTextColor(@ColorInt int color) {
        this.color = color;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
        if (!isStarting) {
            return;
        }
        step += speed;//speed为文字滚动速度。
        if (step > temp_view_plus_two_text_length) {
            if (!isFirstFinished) {
                temp_view_plus_text_length += viewWidth;
                temp_view_plus_two_text_length += viewWidth;
                isFirstFinished = true;
            }
            step = textLength;
        }
        invalidate();
    }

    public boolean isStartingScroll() {
        return isStarting;
    }

}
