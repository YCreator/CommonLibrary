package com.lib.tpl.pay;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by yzd on 2017/3/13 0013.
 */

public class Wxpay {

    //微信支付
    public static void weichatPay(Context context, String wxKey, WpayEntity entity) {
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
