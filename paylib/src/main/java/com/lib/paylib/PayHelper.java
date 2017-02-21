package com.lib.paylib;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by yzd on 2017/2/10 0010.
 */

public class PayHelper {

    private static PayHelper helper;

    private String wxKey;

    private PayHelper() {}

    public static PayHelper getInstance() {
        if (helper == null) {
            helper = new PayHelper();
        }
        return helper;
    }

    /**
     * 初始化
     * @param wxKey
     */
    public void init(String wxKey) {
        this.wxKey = wxKey;
    }

    //微信支付
    public void weichatPay(Context context, WpayEntity entity) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, wxKey);
        PayReq request = new PayReq();
        request.appId = entity.getAppId();
        request.partnerId = entity.getPartnerId();
        request.prepayId = entity.getPrepayId();
        request.packageValue = entity.getPackageValue();
        request.nonceStr = entity.getNonceStr();
        request.timeStamp = entity.getTimeStamp();
        request.sign = entity.getSign();
        api.sendReq(request);
    }
}
