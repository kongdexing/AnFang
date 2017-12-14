package com.shuhai.anfang.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dexing on 2016/12/8.
 * No1
 */
@Entity
public class ContactTeacherForTeacher extends ContactTeacher {

    private String school_name;
    private String area_name;

    @Generated(hash = 815279813)
    public ContactTeacherForTeacher(String school_name, String area_name) {
        this.school_name = school_name;
        this.area_name = area_name;
    }

    @Generated(hash = 477570612)
    public ContactTeacherForTeacher() {
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

}
