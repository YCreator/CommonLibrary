package com.lib.tpl;

import android.annotation.SuppressLint;
import android.content.Context;

import com.ali.auth.third.ui.context.CallbackContext;

/**
 * Created by yzd on 2018/4/10 0010.
 */

public final class TpHelper {

    private static Options mOptions;
    @SuppressLint("StaticFieldLeak")
    static Context mContext;

    public static void init(Context context, Options options) {
        mOptions = options;
        mContext = context;
        if (options.isAuth || options.isShare) {
            share().init(mContext, options.umengId);
            share().setAlipay(options.alipayId)
                    .setQQ(options.qqId, options.qqSecret)
                    .setWeixin(options.wxId, options.wxSecret)
                    .setSina(options.sinaId, options.sinaSecret);
        }
        if (options.isAuth) {
            auth().initAuth(mContext);
        }
        if (options.isPay) {
            pay().init(options.wxId);
        }
    }

    public static ShareHelper share() {
        if (!mOptions.isShare) {
            throw new IllegalArgumentException("初始化配置中屏蔽分享功能");
        }
        return ShareHelper.getInstance();
    }

    public static PayHelper pay() {
        if (!mOptions.isPay) {
            throw new IllegalArgumentException("初始化配置中屏蔽支付功能");
        }
        return PayHelper.getInstance(mOptions.wxId);
    }

    public static AuthHelper auth() {
        CallbackContext callbackContext;
        if (!mOptions.isAuth) {
            throw new IllegalArgumentException("初始化配置中屏蔽第三方授权功能");
        }
        return AuthHelper.getInstance();
    }

    public static class Options {
        private String wxId;
        private String wxSecret;
        private String qqId;
        private String qqSecret;
        private String sinaId;
        private String sinaSecret;
        private String alipayId;
        private String umengId;
        private boolean isShare;
        private boolean isPay;
        private boolean isAuth;
        private boolean isDebug;

        public Options setWeixin(String wxId, String wxSecret) {
            this.wxId = wxId;
            this.wxSecret = wxSecret;
            return this;
        }

        public Options setQQ(String qqId, String qqSecret) {
            this.qqId = qqId;
            this.qqSecret = qqSecret;
            return this;
        }

        public Options setSina(String sinaId, String sinaSecret) {
            this.sinaId = sinaId;
            this.sinaSecret = sinaSecret;
            return this;
        }

        public Options setAlipay(String alipayId) {
            this.alipayId = alipayId;
            return this;
        }

        public Options setUmeng(String umengId) {
            this.umengId = umengId;
            return this;
        }

        public Options canShare(boolean isShare) {
            this.isShare = isShare;
            return this;
        }

        public Options canPay(boolean isPay) {
            this.isPay = isPay;
            return this;
        }

        public Options canAuth(boolean isAuth) {
            this.isAuth = isAuth;
            return this;
        }

        public Options setDebug(boolean isDebug) {
            this.isDebug = isDebug;
            return this;
        }
    }
}
