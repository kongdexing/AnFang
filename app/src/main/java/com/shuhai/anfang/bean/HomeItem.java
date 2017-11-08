package com.shuhai.anfang.bean;

import android.app.Activity;

/**
 * Created by dexing on 2017-11-6 0006.
 * 首页模型类
 */
public class HomeItem {

    //图标
    private int iconId;
    //标题
    private String title;
    //跳转至某个Activity
    private Class<Activity> activity;
    //家长是否可见
    private boolean showForParent = true;
    //老师是否可见
    private boolean showForTeacher = true;

    public int getIconId() {
        return iconId;
    }

    public HomeItem setIconId(int iconId) {
        this.iconId = iconId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public HomeItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public Class<Activity> getActivity() {
        return activity;
    }

    public HomeItem setActivity(Class<Activity> activity) {
        this.activity = activity;
        return this;
    }

    public boolean isShowForParent() {
        return showForParent;
    }

    public HomeItem setShowForParent(boolean showForParent) {
        this.showForParent = showForParent;
        return this;
    }

    public boolean isShowForTeacher() {
        return showForTeacher;
    }

    public HomeItem setShowForTeacher(boolean showForTeacher) {
        this.showForTeacher = showForTeacher;
        return this;
    }
}
