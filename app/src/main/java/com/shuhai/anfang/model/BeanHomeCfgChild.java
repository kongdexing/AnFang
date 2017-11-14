package com.shuhai.anfang.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by dexing on 2017-11-14 0014.
 */
@Entity
public class BeanHomeCfgChild {

    @Id
    private String id;
    private String title;
    private String img;
    private String url;
    private String pid;

    @Generated(hash = 389834924)
    public BeanHomeCfgChild(String id, String title, String img, String url,
            String pid) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.url = url;
        this.pid = pid;
    }

    @Generated(hash = 225517789)
    public BeanHomeCfgChild() {
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

    public String getImg() {
        if (!img.startsWith("http:")) {
            img = "http:" + img;
        }
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

}
