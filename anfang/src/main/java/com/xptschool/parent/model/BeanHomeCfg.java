package com.xptschool.parent.model;

import com.xptschool.parent.BuildConfig;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by dexing on 2017-11-14 0014.
 */
@Entity
public class BeanHomeCfg {

    @Id
    private String id;
    private String title;
    private String image;
    private String url;
    private String mark;
    private String type;

    @Generated(hash = 1082987507)
    public BeanHomeCfg(String id, String title, String image, String url,
            String mark, String type) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.url = url;
        this.mark = mark;
        this.type = type;
    }

    @Generated(hash = 1285855885)
    public BeanHomeCfg() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
