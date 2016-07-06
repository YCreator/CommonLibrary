package com.frame.core.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast的工具类
 */
public class ToastUtil {

    private static Toast toast;

    //Toast显示（短时间）
    public static void showShort(Context context ,String text){
        if (toast == null) {
            toast =  Toast.makeText(context.getApplicationContext(),text,Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //Toast显示（长时间）
    public static void showLong(Context context ,String text){
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(),text,Toast.LENGTH_LONG);
            toast.show();
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
