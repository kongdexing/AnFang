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

    public void login(final String account, final String password, final String type, DefaultRetryPolicy retryPolicy) {
        //login
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.LOGIN,
                new MyVolleyHttpParamsEntity()
                        .addParam("username", account)
                        .addParam("password", password)
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
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: exception " + ex.getMessage());
                                    onLoginFailed("登录失败");
                                    return;
                                }
                                onLoginSuccess(account);
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

    protected void onLoginSuccess(String newAccount) {
        CommonUtil.changeUserStatus(newAccount);
    }

    protected void onLoginFailed(String msg) {
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_UID, "");
    }
}
