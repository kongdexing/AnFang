package com.xptschool.parent.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanParent;
import com.xptschool.parent.model.BeanTeacher;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import butterknife.ButterKnife;

/**
 * Created by dexing on 2017/6/5.
 * No1
 */
public class BaseLoginActivity extends BaseActivity implements LoginListener{
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

    @Override
    public void onLoginStart() {

    }

    @Override
    public void onLoginSuccess() {
        //登录环信
        String easeLoginName = "";
        EMClient.getInstance().login(easeLoginName, CommonUtil.md5("SHUHAIXINXI" + easeLoginName), new EMCallBack() {

            @Override
            public void onSuccess() {
                EMLoginSuccess();
                Log.i(TAG, "登录聊天服务器成功！");
//                ToastUtils.showToast(LoginActivity.this, "登录聊天服务器成功");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, final String error) {
                EMLoginSuccess();

                if (code == 200) {
                    //USER_ALREADY_LOGIN

                    Log.i(TAG, "USER_ALREADY_LOGIN！");
//                    ToastUtils.showToast(LoginActivity.this, "USER_ALREADY_LOGIN");
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.i(TAG, "EMUI onError: " + code + " error:" + error);
//                            if (progress != null)
//                                progress.setVisibility(View.INVISIBLE);
//                            btnLogin.setEnabled(true);
//                            //清除数据
//                            SharedPreferencesUtil.clearUserInfo(LoginActivity.this);
//
//                            GreenDaoHelper.getInstance().clearData();
//                            ToastUtils.showToast(getApplicationContext(), "login failed");
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onLoginFail(String msg) {

    }

    private void EMLoginSuccess() {
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();

        String nickName = "";
        try {
            if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
                if (parent != null) {
                    nickName = parent.getParent_name();
                }
            } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
                if (teacher != null) {
                    nickName = teacher.getName();
                }
            }
        } catch (Exception ex) {
            Log.i(TAG, "onLoginSuccess: login ease error " + ex.getMessage());
        }

        EMClient.getInstance().updateCurrentUserNick(nickName);

//        EMClient.getInstance().chatManager().getAllConversations();

        runOnUiThread(new Runnable() {
            public void run() {
//                finishActivity();
            }
        });
    }

}
