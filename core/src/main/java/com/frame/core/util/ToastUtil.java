package com.frame.core.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.frame.core.R;

/**
 * Toast的工具类
 */
public class ToastUtil {

    private static Toast toast;

    private ToastUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    //Toast显示（短时间）
    public static void showShort(Context context, String text) {
        if (toast == null) {
            toast = new Toast(context);
            LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflate.inflate(R.layout.toast_layout, null);
            toast.setView(view);
        }
        toast.setText(text);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    //Toast显示（长时间）
    public static void showLong(Context context, String text) {
        if (toast == null) {
            toast = new Toast(context);
            LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflate.inflate(R.layout.toast_layout, null);
            toast.setView(view);
        }
        toast.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }

    public static void exitToast() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

    //Toast显示没网络
    public static void showNonet(Context context) {
        Toast.makeText(context, "当前网络不可用,请检查网络", Toast.LENGTH_SHORT).show();
    }

}
