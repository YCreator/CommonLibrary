package com.frame.core.autoscreen.conversion;

import android.view.View;

import com.frame.core.autoscreen.AbsScreenHelper;

/**
 * Created by yzd on 2018/5/8 0008.
 */

public class SimpleConversion implements IConversion {

    @Override
    public void transform(View view, AbsScreenHelper loadViewHelper) {
        if (view.getLayoutParams() != null) {
            loadViewHelper.loadWidthHeightFont(view);
            loadViewHelper.loadPadding(view);
            loadViewHelper.loadLayoutMargin(view);
        }
    }

}
