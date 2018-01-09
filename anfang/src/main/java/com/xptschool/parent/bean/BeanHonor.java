package com.xptschool.parent.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shuhaixinxi on 2017/12/20.
 */

public class BeanHonor implements Parcelable {

    private String rw_id;
    private String g_id;
    private String g_name;
    private String c_id;
    private String c_name;
    private String stu_id;
    private String stu_name;
    private String reward_type;
    private String reward_details;
    private String create_time;
    private String modify_time;
    private String user_id;
    private String user_name;

    public String getRw_id() {
        return rw_id;
    }

    public void setRw_id(String rw_id) {
        this.rw_id = rw_id;
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getReward_type() {
        return reward_type;
    }

    public void setReward_type(String reward_type) {
        this.reward_type = reward_type;
    }

    public String getReward_details() {
        return reward_details;
    }

    public void setReward_details(String reward_details) {
        this.reward_details = reward_details;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getG_name() {
        return g_name;
    }

    public void setG_name(String g_name) {
        this.g_name = g_name;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.rw_id);
        dest.writeString(this.g_id);
        dest.writeString(this.g_name);
        dest.writeString(this.c_id);
        dest.writeString(this.c_name);
        dest.writeString(this.stu_id);
        dest.writeString(this.stu_name);
        dest.writeString(this.reward_type);
        dest.writeString(this.reward_details);
        dest.writeString(this.create_time);
        dest.writeString(this.modify_time);
        dest.writeString(this.user_id);
        dest.writeString(this.user_name);
    }

    public BeanHonor() {
    }

    protected BeanHonor(Parcel in) {
        this.rw_id = in.readString();
        this.g_id = in.readString();
        this.g_name = in.readString();
        this.c_id = in.readString();
        this.c_name = in.readString();
        this.stu_id = in.readString();
        this.stu_name = in.readString();
        this.reward_type = in.readString();
        this.reward_details = in.readString();
        this.create_time = in.readString();
        this.modify_time = in.readString();
        this.user_id = in.readString();
        this.user_name = in.readString();
    }

    public static final Creator<BeanHonor> CREATOR = new Creator<BeanHonor>() {
        @Override
        public BeanHonor createFromParcel(Parcel source) {
            return new BeanHonor(source);
        }

        @Override
        public BeanHonor[] newArray(int size) {
            return new BeanHonor[size];
        }
    };
}
