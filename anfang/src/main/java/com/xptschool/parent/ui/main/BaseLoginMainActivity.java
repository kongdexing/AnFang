package com.xptschool.parent.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;

import butterknife.ButterKnife;

/**
 * Created by dexing on 2017/6/5.
 * No1
 */
public class BaseLoginMainActivity extends AppCompatActivity {
    public String TAG = "";
    private Dialog progressDialog;

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

    public void login(final String account, final String password, DefaultRetryPolicy retryPolicy) {
        Log.i(TAG, "start login: "+password);
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.LOGIN,
                new MyVolleyHttpParamsEntity()
                        .addParam("username", account)
                        .addParam("password", password), retryPolicy,
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
                                SharedPreferencesUtil.saveData(BaseLoginMainActivity.this, SharedPreferencesUtil.KEY_PWD, password);
                                Log.i(TAG, "login success onResponse: ");
                                try {
                                    CommonUtil.analyseLoginData(httpResult, account);
                                } catch (Exception ex) {
                                    Log.i(TAG, "BaseLoginMainActivity onResponse: exception " + ex.getMessage());
                                    return;
                                }
                                onLoginSuccess(account);
                                break;
                            default:
                                Log.i(TAG, "onResponse: " + httpResult.getInfo());
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "login fail onErrorResponse: " + error.getMessage());
                    }
                });
    }

    protected void onStartLogin() {

    }

    protected void onLoginSuccess(String newAccount) {
        CommonUtil.changeUserStatus(newAccount);

    }

    protected void onLoginFailed(String msg) {
        Log.i(TAG, "onLoginFailed: ");
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_UID, "");
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_REF_ID, "");
    }

    public void showProgress(String str) {
        if (progressDialog == null) {
            progressDialog = new Dialog(this, R.style.CustomDialog);
            progressDialog.setContentView(R.layout.layout_dialog);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        TextView msg = (TextView) progressDialog.findViewById(R.id.tv_load_dialog);
        msg.setText(str);
        try {
            progressDialog.show();
        } catch (Exception ex) {
            Log.e(TAG, "showProgress: " + ex.getMessage());
        }
    }

    public void showProgress(int strId) {
        showProgress(getResources().getString(strId));
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

}
