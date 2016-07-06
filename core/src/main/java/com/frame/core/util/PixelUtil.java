package com.frame.core.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.frame.core.BaseApplication;


/**
 * 屏幕工具
 * 单位转换
 */
public class PixelUtil {
    /**
     * dip转px
     * param context
     * param dipValue
     * return
     */
    public static int dip2px(Context context, float dipValue) {

		/*return (int) TypedValue.applyDimension(1, dipValue
                , context.getApplicationContext().getResources().getDisplayMetrics());*/
        return (int) (dipValue * (context.getResources().getDisplayMetrics().densityDpi / 160) + 0.5f);
    }

    /**
     * px转dip
     * param context
     * param pxValue
     * return
     */
    public static int px2dip(Context context, float pxValue) {
		/*return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValue,
				context.getApplicationContext().getResources().getDisplayMetrics());*/
        return (int) (pxValue * 160 / context.getApplicationContext()
                .getResources().getDisplayMetrics().density + 0.5f);

    }

    /**
     * px转sp
     *
     * @param paramContext
     * @param paramFloat
     * @return
     */
    public static int px2sp(Context paramContext, float paramFloat) {
        return (int) (0.5F + paramFloat / paramContext.getApplicationContext()
                .getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * sp转px
     *
     * @param paramContext
     * @param paramFloat
     * @return
     */
    public static int sp2px(Context paramContext, float paramFloat) {
        return (int) (0.5F + paramFloat * paramContext.getApplicationContext()
                .getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     * param context
     * return
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);

    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenW() {
        return BaseApplication.get_resource().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenH() {
        return BaseApplication.get_resource().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕长宽比
     * param context
     * return
     */
    public static float getScreenRate(Context context) {
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H / W);
    }

    /**
     * 状态栏高度算法
     *
     * @param activity
     * @return
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
}
