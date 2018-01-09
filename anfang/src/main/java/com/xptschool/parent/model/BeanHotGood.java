package com.xptschool.parent.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dexing on 2017-11-13 0013.
 */
@Entity
public class BeanHotGood {

    private String id;
    private String url_pc_short;
    private String image;

    @Generated(hash = 113461661)
    public BeanHotGood(String id, String url_pc_short, String image) {
        this.id = id;
        this.url_pc_short = url_pc_short;
        this.image = image;
    }

    @Generated(hash = 427236984)
    public BeanHotGood() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        if (!image.startsWith("http:")) {
            image = "http:" + image;
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl_pc_short() {
        return url_pc_short;
    }

    public void setUrl_pc_short(String url_pc_short) {
        this.url_pc_short = url_pc_short;
    }
}
