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
import com.shuhai.anfang.model.BeanParent;
import com.shuhai.anfang.model.BeanTeacher;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.push.DeviceHelper;
import com.shuhai.anfang.ui.main.MainActivity;
import com.shuhai.anfang.ui.register.RegisterActivity;
import com.shuhai.anfang.ui.register.SelSchoolActivity;
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
    @BindView(R.id.cbx_visitor)
    SmoothCheckBox cbx_visitor;

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
        cbx_visitor.setChecked(true);
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

    @OnClick({R.id.imgDel, R.id.imgToggle, R.id.btnLogin, R.id.ll_visitor, R.id.cbx_visitor, R.id.ll_parent, R.id.cbx_parent,
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
                    String type = "0";
                    if (cbx_parent.isChecked()) {
                        type = UserType.PARENT.toString();
                    } else if (cbx_teacher.isChecked()) {
                        type = UserType.TEACHER.toString();
                    }
                    login(account, password, type, null);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.error_empty_login, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_visitor:
            case R.id.cbx_visitor:
                cbx_visitor.setChecked(true);
                cbx_parent.setChecked(false);
                cbx_teacher.setChecked(false);
                break;
            case R.id.ll_parent:
            case R.id.cbx_parent:
                cbx_visitor.setChecked(false);
                cbx_parent.setChecked(true);
                cbx_teacher.setChecked(false);
                break;
            case R.id.ll_teacher:
            case R.id.cbx_teacher:
                cbx_visitor.setChecked(false);
                cbx_parent.setChecked(false);
                cbx_teacher.setChecked(true);
                break;
            case R.id.txtForgetPWD:
                startActivity(new Intent(this, CheckUserActivity.class));
                break;
            case R.id.txtRegister:
                startActivity(new Intent(this, RegisterActivity.class));
//                startActivity(new Intent(this, SelSchoolActivity.class));
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

        String easeLoginName = "";

        try {
            if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
                if (parent == null) {
                    return;
                }
                easeLoginName = parent.getU_id();
            } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
                if (teacher == null) {
                    return;
                }
                easeLoginName = teacher.getU_id();
            }
        } catch (Exception ex) {
            Log.i(TAG, "onLoginSuccess: login ease error " + ex.getMessage());
            return;
        }

        Log.i(TAG, "login ease easeLoginName : ");

        EMClient.getInstance().login(easeLoginName, CommonUtil.md5("SHUHAIXINXI" + easeLoginName), new EMCallBack() {

            @Override
            public void onSuccess() {
                EMLoginSuccess();
                Log.d("main", "登录聊天服务器成功！");
//                ToastUtils.showToast(LoginActivity.this, "登录聊天服务器成功");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, final String error) {
                if (code == 200) {
                    //USER_ALREADY_LOGIN
                    EMLoginSuccess();
                    Log.d("main", "USER_ALREADY_LOGIN！");
//                    ToastUtils.showToast(LoginActivity.this, "USER_ALREADY_LOGIN");
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
                if (progress != null)
                    progress.setVisibility(View.INVISIBLE);
                btnLogin.setEnabled(true);
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
