package com.xptschool.parent.model;

import com.android.widget.spinner.SpinnerModel;
import com.xptschool.parent.BuildConfig;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2016/11/10.
 */
@Entity
public class BeanStudent extends SpinnerModel implements Serializable {

    @Id
    private String stu_id;
    private String stu_name;
    private String imei_id;
    private String watch_phone;
    //0 女 1 男
    private String sex;
    //设备类型 1学生卡，2手表
    private String sos;
    private String whitelist;
    private String photo;
    private String relation;

    @Generated(hash = 664981176)
    public BeanStudent(String stu_id, String stu_name, String imei_id,
            String watch_phone, String sex, String sos, String whitelist,
            String photo, String relation) {
        this.stu_id = stu_id;
        this.stu_name = stu_name;
        this.imei_id = imei_id;
        this.watch_phone = watch_phone;
        this.sex = sex;
        this.sos = sos;
        this.whitelist = whitelist;
        this.photo = photo;
        this.relation = relation;
    }

    @Generated(hash = 1456032229)
    public BeanStudent() {
    }

    public void init() {
        this.stu_id = "";
        this.stu_name = "";
        this.imei_id = "";
        this.sex = "1";
        this.sos = "";
        this.whitelist = "";
        this.photo = "";
        this.relation = "";
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public String getImei_id() {
        return imei_id;
    }

    public void setImei_id(String imei_id) {
        this.imei_id = imei_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSos() {
        return sos;
    }

    public void setSos(String sos) {
        this.sos = sos;
    }

    public String getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(String whitelist) {
        this.whitelist = whitelist;
    }

    @Override
    public String getName() {
        if (stu_name == null || stu_name.isEmpty()) {
            return "未添加昵称";
        }
        return stu_name;
    }

    public String getPhoto() {
        if (!photo.contains(BuildConfig.SERVICE_URL)) {
            photo = BuildConfig.SERVICE_URL + photo;
        }
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getWatch_phone() {
        return this.watch_phone;
    }

    public void setWatch_phone(String watch_phone) {
        this.watch_phone = watch_phone;
    }

}
