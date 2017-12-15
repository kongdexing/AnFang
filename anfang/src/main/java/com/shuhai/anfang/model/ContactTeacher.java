package com.shuhai.anfang.model;

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by dexing on 2016/12/8.
 * No1
 */
@Entity
public class ContactTeacher implements Serializable {

    private String t_id;
    private String u_id;
    private String s_id;
    private String a_id;
    private String name;
    private String phone;
    private String sex;
    private String email;
    private String charge;
    private String d_id;
    private String education;
    private String school_name;
    private String area_name;
    private String d_name;

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getD_name() {
        return d_name;
    }

    public void setD_name(String d_name) {
        this.d_name = d_name;
    }

    public ContactTeacher() {
    }

    protected ContactTeacher(Parcel in) {
        this.t_id = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.s_id = in.readString();
        this.school_name = in.readString();
        this.a_id = in.readString();
        this.area_name = in.readString();
        this.d_name = in.readString();
        this.education = in.readString();
        this.sex = in.readString();
        this.email = in.readString();
        this.charge = in.readString();
    }

    @Generated(hash = 1882569368)
    public ContactTeacher(String t_id, String u_id, String s_id, String a_id,
            String name, String phone, String sex, String email, String charge,
            String d_id, String education, String school_name, String area_name,
            String d_name) {
        this.t_id = t_id;
        this.u_id = u_id;
        this.s_id = s_id;
        this.a_id = a_id;
        this.name = name;
        this.phone = phone;
        this.sex = sex;
        this.email = email;
        this.charge = charge;
        this.d_id = d_id;
        this.education = education;
        this.school_name = school_name;
        this.area_name = area_name;
        this.d_name = d_name;
    }

}
