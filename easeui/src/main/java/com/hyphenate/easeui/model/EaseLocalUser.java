package com.hyphenate.easeui.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by shuhaixinxi on 2017/12/19.
 */
@Entity
public class EaseLocalUser {

    @Id
    private String userId;
    private String nickName;
    private String type;
    private String sex;

    @Generated(hash = 362595428)
    public EaseLocalUser(String userId, String nickName, String type, String sex) {
        this.userId = userId;
        this.nickName = nickName;
        this.type = type;
        this.sex = sex;
    }

    @Generated(hash = 1620892798)
    public EaseLocalUser() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
