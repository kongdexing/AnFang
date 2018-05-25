package com.xptschool.parent.model;

import com.xptschool.parent.BuildConfig;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dexing on 2016/12/20.
 * No1
 */
@Entity
public class BeanParent {

    private String name;
    private String username;
    private String password;
    private String user_id;
    private String sex;
    private String phone;
    private String qq_openid;
    private String wx_openid;
    private String head_portrait;
    private String api_id;
    private String security_key;
    private String token;

    @Generated(hash = 2039025773)
    public BeanParent(String name, String username, String password,
            String user_id, String sex, String phone, String qq_openid,
            String wx_openid, String head_portrait, String api_id,
            String security_key, String token) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.user_id = user_id;
        this.sex = sex;
        this.phone = phone;
        this.qq_openid = qq_openid;
        this.wx_openid = wx_openid;
        this.head_portrait = head_portrait;
        this.api_id = api_id;
        this.security_key = security_key;
        this.token = token;
    }

    @Generated(hash = 111404833)
    public BeanParent() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq_openid() {
        return qq_openid;
    }

    public void setQq_openid(String qq_openid) {
        this.qq_openid = qq_openid;
    }

    public String getWx_openid() {
        return wx_openid;
    }

    public void setWx_openid(String wx_openid) {
        this.wx_openid = wx_openid;
    }

    public String getApi_id() {
        return api_id;
    }

    public void setApi_id(String api_id) {
        this.api_id = api_id;
    }

    public String getSecurity_key() {
        return security_key;
    }

    public void setSecurity_key(String security_key) {
        this.security_key = security_key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHead_portrait() {
        if (!head_portrait.contains(BuildConfig.SERVICE_URL)) {
            head_portrait = BuildConfig.SERVICE_URL + head_portrait;
        }
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }
}
