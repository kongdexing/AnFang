package com.xptschool.parent.ui.login;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by dexing on 2017/6/5.
 * No1
 */
public class BaseLoginActivity extends BaseActivity {
    public String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    protected void onStartLogin() {
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_PWD, "");
    }

    protected void onLoginSuccess(String newAccount) {
        CommonUtil.changeUserStatus(newAccount);
    }

    protected void onLoginFailed(String msg) {
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_UID, "");
    }
}
