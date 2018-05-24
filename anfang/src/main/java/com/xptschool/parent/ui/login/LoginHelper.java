package com.xptschool.parent.ui.login;

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

public class LoginHelper {

    private static LoginHelper instance;

    public static LoginHelper getInstance() {
        if (instance == null) {
            instance = new LoginHelper();
        }
        return instance;
    }

    /**
     * 账号密码登录
     *
     * @param entity
     * @param listener
     */
    public void login(final MyVolleyHttpParamsEntity entity, final LoginListener listener) {
        //login
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.LOGIN,
                entity, new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        if (listener != null) {
                            listener.onLoginStart();
                        }
                    }

                    @Override
                    public void onResponse(VolleyHttpResult httpResult) {
                        super.onResponse(httpResult);

                        switch (httpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                if (listener != null) {
                                    listener.onLoginSuccess();
                                }
//                                SharedPreferencesUtil.saveData(BaseLoginActivity.this, SharedPreferencesUtil.KEY_PWD, password);
//                                try {
//                                    CommonUtil.analyseLoginData(httpResult, account);
//                                } catch (Exception ex) {
//                                    Log.i(TAG, "onResponse: exception " + ex.getMessage());
//                                    onLoginFailed("登录失败");
//                                    return;
//                                }
//                                onLoginSuccess(account);
                                break;
                            default:
                                if (listener != null) {
                                    listener.onLoginFail(httpResult.getInfo());
                                }
//                                onLoginFailed(httpResult.getInfo());
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        onLoginFailed("登录失败");
                        if (listener != null) {
                            listener.onLoginFail(error.getMessage());
                        }
                    }
                });
    }

}
