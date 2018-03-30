package com.xptschool.parent.bean;

import com.xptschool.parent.BuildConfig;

public class BeanNews {

    private String m_id;
    private String title;
    private String describe;
    private String release_time;
    private String images_path;

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages_path() {
        if (images_path != null && !images_path.isEmpty() && !images_path.contains(BuildConfig.SERVICE_URL)) {
            images_path = BuildConfig.SERVICE_URL + images_path;
        }
        return images_path;
    }

    public void setImages_path(String images_path) {
        this.images_path = images_path;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }
}
