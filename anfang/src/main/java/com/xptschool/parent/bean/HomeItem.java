package com.xptschool.parent.bean;

import android.content.Intent;

/**
 * Created by dexing on 2017-11-6 0006.
 * 首页模型类
 */
public class HomeItem {

    //图标
    private int iconId;
    //标题
    private String title;
    //跳转至某个Activity之Intent
    private Intent intent;

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

    public Intent getIntent() {
        return intent;
    }

    public HomeItem setIntent(Intent intent) {
        this.intent = intent;
        return this;
    }

}
