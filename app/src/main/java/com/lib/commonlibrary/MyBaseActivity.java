package com.lib.commonlibrary;

import android.databinding.ViewDataBinding;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.frame.core.mvvm.base.BaseViewModel;
import com.frame.core.mvvm.base.NewBaseActivity;
import com.frame.core.util.utils.ImageUtils;
import com.jaeger.library.StatusBarUtil;

/**
 * Created by yzd on 2018/5/24 0024.
 */

public abstract class MyBaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends NewBaseActivity<V, VM> {

    Toolbar toolbar;

    @Override
    public void initData() {
        initToolbar((ViewGroup) getWindow().getDecorView());
        setStatusBar(false);
        super.initData();
    }

    protected void setStatusBar(final boolean isTransparent) {
        if (toolbar != null) {
            Drawable drawble = toolbar.getBackground();
            if (drawble instanceof ColorDrawable) {
                if (isTransparent) {
                    StatusBarUtil.setColor(this, ((ColorDrawable) drawble).getColor(), 0);
                } else {
                    StatusBarUtil.setColor(this, ((ColorDrawable) drawble).getColor());
                }
            } else {
                Palette.from(ImageUtils.drawable2Bitmap(drawble)).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@NonNull Palette palette) {
                        Palette.Swatch swatch = palette.getMutedSwatch();
                        if (swatch != null && !isDestroyed()) {
                            if (isTransparent) {
                                StatusBarUtil.setColor(MyBaseActivity.this, swatch.getRgb(), 0);
                            } else {
                                StatusBarUtil.setColor(MyBaseActivity.this, swatch.getRgb());
                            }

                        }
                    }
                });
            }
        }

    }

    private void initToolbar(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View mView = viewGroup.getChildAt(i);
            if (mView instanceof Toolbar) {
                toolbar = (Toolbar) mView;
                this.setSupportActionBar(toolbar);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            }
            if (mView instanceof ViewGroup) {
                initToolbar((ViewGroup) mView);
            }
        }
    }
}
