package com.frame.core.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Random;

/**
 * Created by yzd on 2016/6/22.
 */
public class CommonUtils {



    /**
     * 在1秒内
     * 防止按钮被连续点击
     * 是否为快速重复点击
     */
    public static boolean isFastDoubleClick(View view) {
        if (view == null) return true;
        long lastClickTime = view.getTag(-1) == null
                || !StringUtils.isNumber(String.valueOf(view.getTag(-1)))
                ? 0 : (long) view.getTag(-1);
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= 1000) {
            return true;
        } else {
            view.setTag(-1, time);
            return false;
        }
    }

    public static boolean isFastDoubleClick(int timeMillis, View view) {
        if (view == null) return true;
        long lastClickTime = view.getTag(-1) == null
                || !StringUtils.isNumber(String.valueOf(view.getTag(-1)))
                ? 0 : (long) view.getTag(-1);
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= timeMillis) {
            return true;
        } else {
            view.setTag(-1, time);
            return false;
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param view
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
