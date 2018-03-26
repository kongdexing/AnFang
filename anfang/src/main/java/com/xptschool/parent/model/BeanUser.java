package com.xptschool.parent.model;

import com.xptschool.parent.BuildConfig;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by shuhaixinxi on 2018/3/26.
 */
//普通用户，代理商，第三方公司，区域代理商
@Entity
public class BeanUser {

    private String name;
    private String username;
    private String user_id;
    private String sex;
    private String phone;
    private String email;
    private String head_portrait;
    private String ref_id;
    private String type;

    @Generated(hash = 1748734693)
    public BeanUser(String name, String username, String user_id, String sex,
                    String phone, String email, String head_portrait, String ref_id,
                    String type) {
        this.name = name;
        this.username = username;
        this.user_id = user_id;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
        this.head_portrait = head_portrait;
        this.ref_id = ref_id;
        this.type = type;
    }

    @Generated(hash = 340745516)
    public BeanUser() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
