package com.xptschool.parent.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.meizu.cloud.pushsdk.PushManager;
import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.push.DeviceHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户名密码登录
 */
public class LoginByPwdActivity extends BaseLoginActivity implements HuaweiApiClient.ConnectionCallbacks, HuaweiApiClient.OnConnectionFailedListener {

    boolean showPassword = false;
    @BindView(R.id.llParent)
    LinearLayout llParent;
    @BindView(R.id.edtAccount)
    EditText edtAccount;
    @BindView(R.id.edtPwd)
    EditText edtPwd;
    @BindView(R.id.imgToggle)
    ImageView imgToggle;
    @BindView(R.id.btnLogin)
    TextView btnLogin;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.imgCompany)
    ImageView imgCompany;

    HuaweiApiClient client;
    String origin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pwd);
        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            origin = bundle.getString(ExtraKey.LOGIN_ORIGIN);
            if (origin != null && origin.equals("1")) {
                //修改密码后重新进入登录界面
                String userName = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_NAME, "");
                edtAccount.setText(userName);
                String userPwd = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_PWD, "");
                edtPwd.setText(userPwd);

                LoginHelper.getInstance().login(new MyVolleyHttpParamsEntity()
                        .addParam("phone", userName)
                        .addParam("password", CommonUtil.md5(userPwd)), this);
            }
        }

        setTxtRight(R.string.forgetpwd);
        setTextRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginByPwdActivity.this, CheckSMSCodeActivity.class);
                //获取已保存的用户找回密码的手机号
                startActivityForResult(intent, 2);
            }
        });
        //显示上次登录成功的手机号
        String userName = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_NAME, "");
        edtAccount.setText(userName);
        edtAccount.setSelection(edtAccount.getText().length());
    }

    @Override
    public void onConnected() {
        //华为移动服务client连接成功，在这边处理业务自己的事件
        Log.i(TAG, "HuaweiApiClient 连接成功");
        new Thread() {
            public void run() {
                HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(client, false);
                HuaweiPush.HuaweiPushApi.enableReceiveNormalMsg(client, false);
            }
        }.start();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        //HuaweiApiClient断开连接的时候，业务可以处理自己的事件
        Log.i(TAG, "HuaweiApiClient 连接断开");
        //HuaweiApiClient异常断开连接, if 括号里的条件可以根据需要修改
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "HuaweiApiClient连接失败，错误码：" + result.getErrorCode());
    }

    private void initView() {
        llParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = llParent.getRootView().getHeight();
                int height = llParent.getHeight();
                int diff = heightDiff - height;
                Log.i(TAG, "onGlobalLayout  rootH " + heightDiff + "  height:" + height + " diff:" + diff);
                if (diff > 400) {
                    //键盘弹起
                    imgCompany.setVisibility(View.GONE);
                } else {
                    imgCompany.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.imgDel, R.id.imgToggle, R.id.btnLogin})
    void buttonOnclick(View view) {
        switch (view.getId()) {
            case R.id.imgDel:
                edtAccount.setText("");
                edtAccount.requestFocus();
                break;
            case R.id.imgToggle:
                showPassword(!showPassword);
                break;
            case R.id.btnLogin:
                String account = edtAccount.getText().toString();
                String pwd = edtPwd.getText().toString();

                if (!TextUtils.isEmpty(account) || !TextUtils.isEmpty(pwd)) {
                    btnLogin.setEnabled(false);
                    CommonUtil.hideInputWindow(LoginByPwdActivity.this, btnLogin);
                    LoginHelper.getInstance().login(new MyVolleyHttpParamsEntity()
                            .addParam("phone", account)
                            .addParam("password", CommonUtil.md5(pwd)), this);
                } else {
                    Toast.makeText(LoginByPwdActivity.this, R.string.error_empty_login, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showPassword(boolean show) {
        if (show) {
            // 显示密码
            edtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imgToggle.setImageResource(R.drawable.login_reg_showpass);
        } else {
            // 隐藏密码
            edtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgToggle.setImageResource(R.drawable.login_reg_hidepass);
        }
        edtPwd.setSelection(edtPwd.getText().length());
        showPassword = show;
    }

    @Override
    public void onLoginStart() {
        super.onLoginStart();
        showProgress("正在登录");
        enableView(false);
    }

    @Override
    public void onLoginSuccess() {
        super.onLoginSuccess();
        hideProgress();
        enableView(true);
        setResult(1);
        finish();
    }

    @Override
    public void onLoginFail(String msg) {
        super.onLoginFail(msg);
        hideProgress();
        enableView(true);
    }

    private void enableView(boolean enable) {
//        if (progress != null)
//            progress.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
        if (btnLogin != null) {
            btnLogin.setEnabled(enable);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            CommonUtil.hideInputWindow(this, edtAccount);
        } catch (Exception ex) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
