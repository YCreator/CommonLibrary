package com.lib.paylib.alipay;

/**
 * Created by yzd on 2017/3/13 0013.
 */

public interface AlipayCallback {

    void success();

    void waitting();

    void fault();
}
