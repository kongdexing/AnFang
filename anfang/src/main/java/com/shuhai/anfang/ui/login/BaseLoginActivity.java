package com.shuhai.anfang.ui.login;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.http.HttpAction;
import com.shuhai.anfang.http.MyVolleyRequestListener;
import com.shuhai.anfang.server.ServerManager;
import com.shuhai.anfang.ui.main.BaseActivity;

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

    public void login(final String account, final String password, final String type, DefaultRetryPolicy retryPolicy) {
        //login
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.LOGIN,
                new VolleyHttpParamsEntity()
                        .addParam("username", account)
                        .addParam("password", password)
                        .addParam("system_model", "1")
                        .addParam("type", type), retryPolicy,
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        onStartLogin();
                    }

                    @Override
                    public void onResponse(VolleyHttpResult httpResult) {
                        super.onResponse(httpResult);
                        switch (httpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                SharedPreferencesUtil.saveData(BaseLoginActivity.this, SharedPreferencesUtil.KEY_PWD, password);
                                try {
                                    CommonUtil.analyseLoginData(httpResult, type, account);
                                    onLoginSuccess();
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: exception " + ex.getMessage());
                                    onLoginFailed("登录失败");
                                }
                                break;
                            default:
                                onLoginFailed(httpResult.getInfo());
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLoginFailed("登录失败");
                    }
                });
    }

    protected void onStartLogin() {
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_PWD, "");
    }

    protected void onLoginSuccess() {
        ServerManager.getInstance().startService();
    }

    protected void onLoginFailed(String msg) {
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_UID, "");
    }
}
