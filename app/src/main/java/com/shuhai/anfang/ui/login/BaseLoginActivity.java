package com.shuhai.anfang.ui.login;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.http.HttpAction;
import com.shuhai.anfang.http.MyVolleyRequestListener;
import com.shuhai.anfang.imsdroid.ImsSipHelper;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.push.UpushTokenHelper;
import com.shuhai.anfang.server.ServerManager;
import com.shuhai.anfang.ui.main.BaseActivity;

import org.json.JSONObject;

/**
 * Created by dexing on 2017/6/5.
 * No1
 */
public class BaseLoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void login(final String account, final String password, final String type, DefaultRetryPolicy retryPolicy) {
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
                                if (!SharedPreferencesUtil.getData(BaseLoginActivity.this, SharedPreferencesUtil.KEY_USER_NAME, "").equals(account)) {
                                    SharedPreferencesUtil.saveData(BaseLoginActivity.this, SharedPreferencesUtil.KEY_USER_NAME, account);
                                    ImsSipHelper.getInstance().stopSipServer();
                                    //切换账号
                                    UpushTokenHelper.exitAccount(GreenDaoHelper.getInstance().getCurrentParent());
                                }
                                SharedPreferencesUtil.saveData(BaseLoginActivity.this, SharedPreferencesUtil.KEY_PWD, password);

                                try {
                                    JSONObject jsonData = new JSONObject(httpResult.getData().toString());
                                    CommonUtil.initBeanStudentByHttpResult(jsonData.getJSONArray("stuData").toString());
                                    CommonUtil.initParentInfoByHttpResult(jsonData.getJSONObject("login").toString(), account);
                                    //删除联系人
                                    GreenDaoHelper.getInstance().deleteContact();
                                    XPTApplication.getInstance().setCurrent_user_type(type);
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

    }

    protected void onLoginSuccess() {
        ServerManager.getInstance().startService();
    }

    protected void onLoginFailed(String msg) {
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_UID, "");
    }
}
