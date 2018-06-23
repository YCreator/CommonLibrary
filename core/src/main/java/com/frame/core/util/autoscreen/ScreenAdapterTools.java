package com.frame.core.util.autoscreen;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by yzd on 2018/5/9 0009.
 */

public class ScreenAdapterTools {
    private static AbsScreenHelper sLoadViewHelper;

    public static AbsScreenHelper getInstance() {
        if (sLoadViewHelper == null) {
            sLoadViewHelper = new EmptyScreenHelper();
        }
        return sLoadViewHelper;
    }

    public static void init(Context context) {
        init(context, new IProvider() {
            @Override
            public AbsScreenHelper provide(Context context, int designWidth, int designDpi, float fontSize, String unit) {
                return new AutoScreenHelper(context, designWidth, designDpi, fontSize, unit);
            }
        });
    }

    public static void init(Context context, IProvider provider) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context
                    .getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert applicationInfo != null;
        int designwidth = applicationInfo.metaData.getInt("designwidth");
        int designdpi = applicationInfo.metaData.getInt("designdpi");
        float fontsize = applicationInfo.metaData.getFloat("fontsize");
        String unit = applicationInfo.metaData.getString("unit");
        sLoadViewHelper = provider.provide(context, designwidth, designdpi, fontsize, unit);
    }

    private static void init(Builder builder) {
        sLoadViewHelper = new IProvider() {
            @Override
            public AbsScreenHelper provide(Context context, int designWidth, int designDpi, float fontSize, String unit) {
                return new AutoScreenHelper(context, designWidth, designDpi, fontSize, unit);
            }
        }.provide(builder.mContext, builder.designwidth, builder.designdpi, builder.fontsize, builder.unit);
    }

    public static Builder newBuilder(Context mContext) {
        return new Builder(mContext);
    }

    public static class Builder {
        int designwidth;
        int designdpi;
        float fontsize;
        String unit;
        Context mContext;

        Builder(Context context) {
            mContext = context;
        }

        public Builder setDesignwidth(int designwidth) {
            this.designwidth = designwidth;
            return this;
        }

        public Builder setDesigndpi(int designdpi) {
            this.designdpi = designdpi;
            return this;
        }

        public Builder setFontsize(float fontsize) {
            this.fontsize = fontsize;
            return this;
        }

        public Builder setUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public void build() {
            init(this);
        }
    }

    public interface IProvider {
        AbsScreenHelper provide(Context context, int designWidth, int designDpi, float fontSize, String unit);
    }
}
