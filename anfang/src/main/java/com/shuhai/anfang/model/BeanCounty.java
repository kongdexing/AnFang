package com.shuhai.anfang.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by shuhaixinxi on 2017/12/27.
 */
@Entity
public class BeanCounty {

    private String region_id;
    private String region_code;
    private String region_name;
    private String parent_id;
    private String region_level;

    @Generated(hash = 1957042131)
    public BeanCounty(String region_id, String region_code, String region_name,
            String parent_id, String region_level) {
        this.region_id = region_id;
        this.region_code = region_code;
        this.region_name = region_name;
        this.parent_id = parent_id;
        this.region_level = region_level;
    }

    @Generated(hash = 1309208381)
    public BeanCounty() {
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getRegion_level() {
        return region_level;
    }

    public void setRegion_level(String region_level) {
        this.region_level = region_level;
    }


}
