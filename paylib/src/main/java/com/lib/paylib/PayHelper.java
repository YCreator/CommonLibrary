package com.lib.paylib;

import android.app.Activity;
import android.content.Context;

import com.lib.paylib.alipay.AlipayCallback;

/**
 * 支付工具
 * Created by yzd on 2017/2/10 0010.
 */

public class PayHelper {

    private static PayHelper helper;

    private String wxKey;

    private PayHelper() {
    }

    public static PayHelper getInstance() {
        if (helper == null) {
            helper = new PayHelper();
        }
        return helper;
    }

    /**
     * 初始化
     *
     * @param wxKey
     */
    public void init(String wxKey) {
        this.wxKey = wxKey;
    }

    //微信支付
    public void weichatPay(Context context, WpayEntity entity) throws Exception {
        if (wxKey == null || "".equals(wxKey)) {
            throw new Exception("微信appkey未初始化");
        }
        Wxpay.weichatPay(context, wxKey, entity);
    }

    public void aliPay(final Activity activity, final String payInfo, AlipayCallback callback) {
        Alipay.aliPay(activity, payInfo, callback);
    }

}
