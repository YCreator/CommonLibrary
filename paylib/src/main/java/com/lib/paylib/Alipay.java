package com.lib.paylib;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.lib.paylib.alipay.AlipayCallback;
import com.lib.paylib.alipay.PayResult;

import java.util.Map;

import static com.lib.paylib.alipay.Keys.SDK_PAY_FLAG;

/**
 * Created by yzd on 2017/3/13 0013.
 */

public class Alipay {

    private static AlipayHandle mHandler = new AlipayHandle(Looper.getMainLooper());

    private static class AlipayHandle extends Handler {

        private AlipayCallback callback;

        private AlipayHandle(Looper looper) {
            super(looper);
        }

        private AlipayHandle(Looper looper, AlipayCallback callback) {
            super(looper);
            this.callback = callback;
        }

        private void setCallback(AlipayCallback callback) {
            this.callback = callback;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String statu = payResult.getResultStatus();
                    switch (statu) {
                        case "9000":    //判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                            if (callback != null) {
                                callback.success();
                            }
                            break;
                        case "8000":    //“8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (callback != null) {
                                callback.waitting();
                            }
                            break;
                        default:        //其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            if (callback != null) {
                                callback.fault();
                            }
                            break;
                    }
                }
            }
        }
    }

    public static void aliPay(final Activity activity, final String payInfo, AlipayCallback callback) {
        mHandler.setCallback(callback);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


}
