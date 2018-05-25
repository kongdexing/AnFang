package com.xptschool.parent.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.meizu.cloud.pushsdk.PushManager;
import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanParent;
import com.xptschool.parent.model.BeanTeacher;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.push.DeviceHelper;
import com.xptschool.parent.ui.main.MainActivity;
import com.xptschool.parent.util.ToastUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseLoginActivity implements HuaweiApiClient.ConnectionCallbacks, HuaweiApiClient.OnConnectionFailedListener {

    @BindView(R.id.llParent)
    LinearLayout llParent;
    @BindView(R.id.edtAccount)
    EditText edtAccount;
    @BindView(R.id.edtCode)
    EditText edtCode;
    @BindView(R.id.btnLogin)
    TextView btnLogin;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.imgCompany)
    ImageView imgCompany;
    @BindView(R.id.btnSend)
    Button btnSend;

    HuaweiApiClient client;
    String origin = null;

    private int lastTime = 0;
    private final int START_TIMER_DELAY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            origin = bundle.getString(ExtraKey.LOGIN_ORIGIN);
            if (origin != null && origin.equals("0")) {
                showImgBack(false);
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
            } else if (origin != null && origin.equals("1")) {
//                String userName = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_NAME, "");
//                edtAccount.setText(userName);
//                String userPwd = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_PWD, "");
//                edtCode.setText(userPwd);
//                login(userName, CommonUtil.md5(userPwd), null);
            }
        }

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

    @OnClick({R.id.imgDel, R.id.btnSend, R.id.btnLogin, R.id.imgQQ, R.id.imgWeChat, R.id.imgPwd})
    void buttonOnclick(View view) {
        switch (view.getId()) {
            case R.id.imgDel:
                edtAccount.setText("");
                edtAccount.requestFocus();
                break;
            case R.id.btnSend:
                //发送验证码
                String phone = edtAccount.getText().toString();
                if (phone.isEmpty() || !CommonUtil.isPhone(phone)) {
                    ToastUtils.showToast(this, R.string.input_error_phone);
                    break;
                }
                getVerifyCode(phone);
                break;
            case R.id.btnLogin:
                String account = edtAccount.getText().toString();
                String verifyCode = edtCode.getText().toString();

                if (!TextUtils.isEmpty(verifyCode)) {
                    btnLogin.setEnabled(false);
                    CommonUtil.hideInputWindow(LoginActivity.this, btnLogin);
                    LoginHelper.getInstance().login(new MyVolleyHttpParamsEntity()
                            .addParam("phone", account)
                            .addParam("verifyCode", verifyCode), this);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.hint_code, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgQQ:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.imgWeChat:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.imgPwd:
                startActivityForResult(new Intent(this, LoginByPwdActivity.class), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            //使用登录密码登录成功
            finish();
        }


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_TIMER_DELAY:
                    try {
                        lastTime--;
                        if (lastTime == 0) {
                            btnSend.setEnabled(true);
                            btnSend.setText(R.string.btn_send_code);
                        } else {
                            btnSend.setText(lastTime + "秒后重发");
                            btnSend.setEnabled(false);
                            handler.sendEmptyMessageDelayed(START_TIMER_DELAY, 1000);
                        }
                    } catch (Exception ex) {
                        Log.i(TAG, "handleMessage: " + ex.getMessage());
                    }
                    break;
            }
        }
    };

    private void getVerifyCode(final String phone) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.LOGIN_GETCODE,
                new MyVolleyHttpParamsEntity()
                        .addParam("phone", phone), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在获取验证码");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            SharedPreferencesUtil.saveData(LoginActivity.this, SharedPreferencesUtil.KEY_LAST_REGISTER, System.currentTimeMillis());
                            //开始60秒倒计时
                            lastTime = 60;
                            handler.sendEmptyMessageDelayed(START_TIMER_DELAY, 1000);
                        }
                        ToastUtils.showToast(LoginActivity.this, volleyHttpResult.getInfo());
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                    }
                });
    }

    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.i(TAG, "onStart: " + share_media.getName());
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Log.i(TAG, "onComplete: successed");
            ToastUtils.showToast(LoginActivity.this, "onComplete: successed");

            for (String key : map.keySet()) {
                Log.i(TAG, "onComplete: key-" + key + "  val-" + map.get(key));
            }
            //授权成功，登录
//            LoginHelper.getInstance().login(new MyVolleyHttpParamsEntity()
//                    .addParam("openId", map.get("openid"))
//                    .addParam("platform_name", verifyCode), this);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Log.i(TAG, "onError: " + throwable.getMessage());
            ToastUtils.showToast(LoginActivity.this, "fail" + throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Log.i(TAG, "onCancel: cancel");
        }
    };

    @Override
    public void onLoginStart() {
        super.onLoginStart();
        enableView(false);
    }

    @Override
    public void onLoginSuccess() {
        super.onLoginSuccess();
        enableView(true);
        finish();
    }

    @Override
    public void onLoginFail(String msg) {
        super.onLoginFail(msg);
        ToastUtils.showToast(this, msg);
        enableView(true);
    }

    private void enableView(boolean enable) {
        if (progress != null)
            progress.setVisibility(enable ? View.INVISIBLE : View.VISIBLE);
        if (btnLogin != null) {
            btnLogin.setEnabled(enable);
        }
    }

    private void finishActivity() {
        if (origin != null && origin.equals("1")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            finish();
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
