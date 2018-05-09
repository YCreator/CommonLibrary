package com.frame.core.autoscreen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.frame.core.autoscreen.conversion.IConversion;
import com.frame.core.autoscreen.conversion.SimpleConversion;

/**
 * Created by yzd on 2018/5/8 0008.
 */

public abstract class AbsScreenHelper {
    protected float actualDensity;
    protected float actualDensityDpi;
    protected float actualWidth;
    protected float actualHeight;

    protected int designWidth;
    protected int designDpi;
    protected float fontSize;
    protected String unit;

    AbsScreenHelper() {

    }

    public AbsScreenHelper(Context context, int designWidth, int designDpi, float fontSize, String unit) {
        this.designWidth = designWidth;
        this.designDpi = designDpi;
        this.fontSize = fontSize;
        this.unit = unit;
        setActualParams(context);
    }

    public void reset(Context context) {
        setActualParams(context);
    }

    private void setActualParams(Context context) {
        float[] actualScreenInfo = ActualScreen.screenInfo(context);
        if (actualScreenInfo.length == 4) {
            actualWidth = actualScreenInfo[0];
            actualHeight = actualScreenInfo[1];
            actualDensity = actualScreenInfo[2];
            actualDensityDpi = actualScreenInfo[3];
        }
    }

    public abstract void loadWidthHeightFont(View view);

    public abstract void loadPadding(View view);

    public abstract void loadLayoutMargin(View view);

    public abstract void loadMaxWidthAndHeight(View view);

    public abstract void loadMinWidthAndHeight(View view);

    // if subclass has owner conversion，you need override this method and provide your conversion
    public void loadView(ViewGroup viewGroup) {
        loadView(viewGroup, new SimpleConversion());
    }

    public final void loadView(ViewGroup viewGroup, IConversion conversion) {
        conversion.transform(viewGroup, this);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                loadView((ViewGroup) viewGroup.getChildAt(i), conversion);
            } else {
                conversion.transform(viewGroup.getChildAt(i), this);
            }
        }
    }
}
