package com.xptschool.parent.ui.login;

import android.os.Bundle;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.xptschool.parent.model.BeanParent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by dexing on 2017/6/5.
 * No1
 */
public class BaseLoginActivity extends BaseActivity implements LoginListener {
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
//        String easeLoginName = "";
//        EMClient.getInstance().login(easeLoginName, CommonUtil.md5("SHUHAIXINXI" + easeLoginName), new EMCallBack() {
//
//            @Override
//            public void onSuccess() {
//                EMLoginSuccess();
//                Log.i(TAG, "登录聊天服务器成功！");
////                ToastUtils.showToast(LoginActivity.this, "登录聊天服务器成功");
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//
//            }
//
//            @Override
//            public void onError(final int code, final String error) {
//                EMLoginSuccess();
//
//                if (code == 200) {
//                    //USER_ALREADY_LOGIN
//                    Log.i(TAG, "USER_ALREADY_LOGIN！");
////                    ToastUtils.showToast(LoginActivity.this, "USER_ALREADY_LOGIN");
//                } else {
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            Log.i(TAG, "EMUI onError: " + code + " error:" + error);
//                        }
//                    });
//                }
//            }
//        });

    }

    @Override
    public void onLoginFail(String msg) {

    }

    private void EMLoginSuccess() {
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();

        String nickName = "";
        try {
            BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
            if (parent != null) {
                nickName = parent.getName();
            }
        } catch (Exception ex) {
            Log.i(TAG, "onLoginSuccess: login ease error " + ex.getMessage());
        }

        EMClient.getInstance().updateCurrentUserNick(nickName);
        runOnUiThread(new Runnable() {
            public void run() {
//                finishActivity();
            }
        });
    }

}
