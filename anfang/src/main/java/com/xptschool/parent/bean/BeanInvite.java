package com.xptschool.parent.bean;

import com.xptschool.parent.BuildConfig;

/**
 * Created by shuhaixinxi on 2018/3/9.
 */

public class BeanInvite {

    private String user_id;
    private String username;
    private String name;
    private String head_portrait;
    private String type;
    private String create_time;
    private String status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead_portrait() {
        if (head_portrait != null && !head_portrait.isEmpty() && !head_portrait.contains(BuildConfig.SERVICE_URL)) {
            head_portrait = BuildConfig.SERVICE_URL + head_portrait;
        }
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
