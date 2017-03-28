package com.lib.paylib;

import java.io.Serializable;

/**
 * Created by yzd on 2017/2/22 0022.
 */

public class AlipayEntity implements Serializable {

    private String partner;             //商户号
    private String seller;              //买家
    private String alipayNotifyUrl;     //支付宝回调地址
    private String alipayPrivateKey;    //支付宝私钥
    private String alipayPublicKey;     //支付宝公钥

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getAlipayNotifyUrl() {
        return alipayNotifyUrl;
    }

    public void setAlipayNotifyUrl(String alipayNotifyUrl) {
        this.alipayNotifyUrl = alipayNotifyUrl;
    }

    public String getAlipayPrivateKey() {
        return alipayPrivateKey;
    }

    public void setAlipayPrivateKey(String alipayPrivateKey) {
        this.alipayPrivateKey = alipayPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    @Override
    public String toString() {
        return "AlipayEntity{" +
                "partner='" + partner + '\'' +
                ", seller='" + seller + '\'' +
                ", alipayNotifyUrl='" + alipayNotifyUrl + '\'' +
                ", alipayPrivateKey='" + alipayPrivateKey + '\'' +
                ", alipayPublicKey='" + alipayPublicKey + '\'' +
                '}';
    }
}
