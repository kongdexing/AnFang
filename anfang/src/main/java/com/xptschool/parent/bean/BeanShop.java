package com.xptschool.parent.bean;

import com.xptschool.parent.BuildConfig;

import java.io.Serializable;

/**
 * Created by gn on 2018/2/7.
 */
public class BeanShop implements Serializable{

    private String id;
    private String brand;
    private String address;
    private String describe;
    private String price;
    private String shop_name;
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getImage() {
        if (!image.contains(BuildConfig.SERVICE_URL)) {
            image = BuildConfig.SERVICE_URL + image;
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
