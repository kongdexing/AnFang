package com.shuhai.anfang.model;

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dexing on 2016/12/8.
 * No1
 */
@Entity
public class ContactTeacherForParent extends ContactTeacher {

    private String u_id;
    private String s_name;
    private String a_name;
    private String d_name;
    private String education;
    private String email;
    private String charge;
    private String g_id;
    private String c_id;

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getA_name() {
        return a_name;
    }

    public void setA_name(String a_name) {
        this.a_name = a_name;
    }

    public String getD_name() {
        return d_name;
    }

    public void setD_name(String d_name) {
        this.d_name = d_name;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
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

    public ContactTeacherForParent() {
    }

    protected ContactTeacherForParent(Parcel in) {
        this.t_id = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.s_id = in.readString();
        this.s_name = in.readString();
        this.a_id = in.readString();
        this.a_name = in.readString();
        this.d_name = in.readString();
        this.education = in.readString();
        this.sex = in.readString();
        this.email = in.readString();
        this.charge = in.readString();
        this.g_id = in.readString();
        this.c_id = in.readString();
    }

    @Generated(hash = 392585045)
    public ContactTeacherForParent(String u_id, String s_name, String a_name, String d_name,
                                   String education, String email, String charge, String g_id, String c_id) {
        this.u_id = u_id;
        this.s_name = s_name;
        this.a_name = a_name;
        this.d_name = d_name;
        this.education = education;
        this.email = email;
        this.charge = charge;
        this.g_id = g_id;
        this.c_id = c_id;
    }

    @Override
    public String toString() {
        return "ContactTeacherForParent{" +
                "t_id='" + t_id + '\'' +
                ", u_id='" + u_id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", s_id='" + s_id + '\'' +
                ", s_name='" + s_name + '\'' +
                ", a_id='" + a_id + '\'' +
                ", a_name='" + a_name + '\'' +
                ", d_name='" + d_name + '\'' +
                ", education='" + education + '\'' +
                ", sex='" + sex + '\'' +
                ", email='" + email + '\'' +
                ", charge='" + charge + '\'' +
                ", g_id='" + g_id + '\'' +
                ", c_id='" + c_id + '\'' +
                '}';
    }
}
