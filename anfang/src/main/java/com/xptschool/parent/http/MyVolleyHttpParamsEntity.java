package com.xptschool.parent.http;

import android.content.pm.PackageInfo;

import com.android.volley.common.VolleyHttpParamsEntity;
import com.xptschool.parent.XPTApplication;

import java.util.HashMap;

/**
 * Created by shuhaixinxi on 2018/3/19.
 */

public class MyVolleyHttpParamsEntity extends VolleyHttpParamsEntity {

    private HashMap<String, String> map = null;

    public MyVolleyHttpParamsEntity() {
        this.map = new HashMap();
        int localVersionCode = 26;
        String localVersionName = "";
        try {
            PackageInfo packageInfo = XPTApplication.getInstance().getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(XPTApplication.getInstance().getPackageName(), 0);
            localVersionCode = packageInfo.versionCode;
            localVersionName = packageInfo.versionName;
        } catch (Exception ex) {

        }
        this.map.put("system_model", "1");
        this.map.put("version_code", localVersionCode + "");
        this.map.put("version_name", localVersionName);
    }

    @Override
    public MyVolleyHttpParamsEntity addParam(String key, String val) {
        this.map.put(key, val);
        return this;
    }

    public HashMap<String, String> getMap() {
        return this.map;
    }
}
