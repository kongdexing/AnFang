package com.shuhai.anfang.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.widget.view.SmoothCheckBox;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.meizu.cloud.pushsdk.PushManager;
import com.shuhai.anfang.R;
import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.common.ExtraKey;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.common.UserType;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.push.DeviceHelper;
import com.shuhai.anfang.util.ToastUtils;
import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseLoginActivity implements HuaweiApiClient.ConnectionCallbacks, HuaweiApiClient.OnConnectionFailedListener {

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

    @BindView(R.id.cbx_parent)
    SmoothCheckBox cbx_parent;
    @BindView(R.id.cbx_teacher)
    SmoothCheckBox cbx_teacher;

    HuaweiApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String origin = bundle.getString(ExtraKey.LOGIN_ORIGIN);
            if (origin != null && origin.equals("0")) {
//                showImgBack(false);
                //推送不可用
                //拒收通知
                String model = android.os.Build.MODEL;
                String carrier = android.os.Build.MANUFACTURER;
                Log.i(TAG, "onCreate: " + model + "  " + carrier);

                if (carrier.toUpperCase().equals(DeviceHelper.M_HUAWEI)) {
                    client = new HuaweiApiClient.Builder(this)
                            .addApi(HuaweiPush.PUSH_API)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .build();
                    client.connect();
                    Log.i(TAG, "HUAWEI disable ");
                } else if (carrier.toUpperCase().equals(DeviceHelper.M_MEIZU)) {
                    PushManager.unRegister(this, XPTApplication.MZ_APP_ID, XPTApplication.MZ_APP_KEY);
                } else {
                    PushAgent mPushAgent = PushAgent.getInstance(this);
                    mPushAgent.disable(new IUmengCallback() {
                        @Override
                        public void onSuccess() {
                            Log.i(TAG, "PushAgent disable onSuccess: ");
                        }

                        @Override
                        public void onFailure(String s, String s1) {
                            Log.i(TAG, "PushAgent disable onFailure: " + s + " s1 " + s1);
                        }
                    });
                }

            }
        }

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
        cbx_parent.setChecked(true);
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

    @OnClick({R.id.imgDel, R.id.imgToggle, R.id.btnLogin, R.id.ll_parent, R.id.cbx_parent,
            R.id.ll_teacher, R.id.cbx_teacher, R.id.txtForgetPWD, R.id.txtRegister})
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
                String password = edtPwd.getText().toString();

                if ((!TextUtils.isEmpty(account)) && (!TextUtils.isEmpty(password))) {
                    btnLogin.setEnabled(false);
                    CommonUtil.hideInputWindow(LoginActivity.this, btnLogin);
                    login(account, password, cbx_parent.isChecked() ? UserType.PARENT.toString() : UserType.TEACHER.toString(), null);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.error_empty_login, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_parent:
            case R.id.cbx_parent:
                cbx_parent.setChecked(true);
                cbx_teacher.setChecked(false);
                break;
            case R.id.ll_teacher:
            case R.id.cbx_teacher:
                cbx_parent.setChecked(false);
                cbx_teacher.setChecked(true);
                break;
            case R.id.txtForgetPWD:
                startActivity(new Intent(this, CheckUserActivity.class));
                break;
            case R.id.txtRegister:

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
    protected void onStartLogin() {
        super.onStartLogin();
        if (progress != null)
            progress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onLoginSuccess() {
        super.onLoginSuccess();

        String account = SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_NAME, "").toString();
        Log.i(TAG, "onLoginSuccess: login huanxin " + account);
        EMClient.getInstance().login(account, "111111", new EMCallBack() {

            @Override
            public void onSuccess() {
                EMLoginSuccess();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, final String error) {
                if (code == 200) {
                    //USER_ALREADY_LOGIN
                    EMLoginSuccess();
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.i(TAG, "EMUI onError: " + code + " error:" + error);
                            if (progress != null)
                                progress.setVisibility(View.INVISIBLE);
                            btnLogin.setEnabled(true);
                            //清除数据
                            SharedPreferencesUtil.clearUserInfo(LoginActivity.this);

                            GreenDaoHelper.getInstance().clearData();
                            ToastUtils.showToast(getApplicationContext(), "login failed");
                        }
                    });
                }
            }
        });
    }

    private void EMLoginSuccess() {
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();

        runOnUiThread(new Runnable() {
            public void run() {
                if (progress != null)
                    progress.setVisibility(View.INVISIBLE);
                btnLogin.setEnabled(true);
                Log.d("main", "登录聊天服务器成功！");
                ToastUtils.showToast(LoginActivity.this, "登录聊天服务器成功");
                finish();
            }
        });
    }

    @Override
    protected void onLoginFailed(String msg) {
        super.onLoginFailed(msg);
        ToastUtils.showToast(this, msg);
        if (progress != null)
            progress.setVisibility(View.INVISIBLE);
        btnLogin.setEnabled(true);
    }

}
