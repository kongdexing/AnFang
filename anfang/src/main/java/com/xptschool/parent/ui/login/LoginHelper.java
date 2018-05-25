package com.xptschool.parent.ui.login;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.HttpErrorMsg;
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
                                try {
                                    CommonUtil.analyseLoginData(httpResult);
                                } catch (Exception ex) {
                                    if (listener != null) {
                                        listener.onLoginFail(HttpErrorMsg.ERROR_JSON);
                                    }
                                    break;
                                }

                                if (listener != null) {
                                    listener.onLoginSuccess();
                                }
                                break;
                            default:
                                if (listener != null) {
                                    listener.onLoginFail(httpResult.getInfo());
                                }
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (listener != null) {
                            listener.onLoginFail(error.getMessage());
                        }
                    }
                });
    }

}
