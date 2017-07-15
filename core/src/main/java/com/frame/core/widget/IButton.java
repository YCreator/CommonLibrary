package com.frame.core.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * 防快速点击的按钮
 * Created by yzd on 2017/2/7 0007.
 */
public class IButton extends AppCompatButton {

    private long lastClickTime;
    private int delay = 1000;

    public IButton(Context context) {
        super(context);
    }

    public IButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(View.OnClickListener l) {
        super.setOnClickListener(v -> {
            if (!isFastDoubleClick()) {
                l.onClick(v);
            }
        });
    }

    /**
     * 设置延迟时间
     *
     * @param delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= delay) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }

}
