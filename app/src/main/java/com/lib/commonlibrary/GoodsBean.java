package com.genye.domain;

import java.io.Serializable;

public class GoodsBeanEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4167798167311585091L;
    private Integer id;
    private String title;
    private String price;
    private String shopName;
    private String picUrl;
    private String category;
    private Boolean service1 = false; // 代
    private Boolean service2 = false; // 退
    private Boolean service3 = false; // 实
    private Boolean service4 = false; // 换
    private boolean isSupplyOfGoods;
    private int goodsStatus;
    private String address;
    private String mobile;

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public boolean isSupplyOfGoods() {
        return isSupplyOfGoods;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getService1() {
        return service1;
    }

    public void setService1(Boolean service1) {
        this.service1 = service1;
    }

    public Boolean getService2() {
        return service2;
    }

    public void setService2(Boolean service2) {
        this.service2 = service2;
    }

    public Boolean getService3() {
        return service3;
    }

    public void setService3(Boolean service3) {
        this.service3 = service3;
    }

    public Boolean getService4() {
        return service4;
    }

    public void setService4(Boolean service4) {
        this.service4 = service4;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setSupplyOfGoods(boolean supplyOfGoods) {
        isSupplyOfGoods = supplyOfGoods;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }
}
