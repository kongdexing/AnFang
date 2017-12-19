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

    @Generated(hash = 930676312)
    public EaseLocalUser(String userId, String nickName) {
        this.userId = userId;
        this.nickName = nickName;
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
}
