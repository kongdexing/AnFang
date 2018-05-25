package com.xptschool.parent.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.util.ToastUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 第三方账号 openId 绑定手机号
 */
public class BindPhoneActivity extends BaseLoginActivity {

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

    String authJsonMap = "";
    private int lastTime = 0;
    private final int START_TIMER_DELAY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_bind_phone);
        setTitle("绑定手机号");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            authJsonMap = bundle.getString("auth_data");
        }

        initView();
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

    @OnClick({R.id.imgDel, R.id.btnLogin, R.id.btnSend})
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
                if (account.isEmpty() || !CommonUtil.isPhone(account)) {
                    ToastUtils.showToast(this, R.string.input_error_phone);
                    return;
                }
                String code = edtCode.getText().toString();
                if (code.isEmpty()) {
                    ToastUtils.showToast(this, R.string.hint_code);
                    return;
                }

                CommonUtil.hideInputWindow(BindPhoneActivity.this, btnLogin);

                if (!authJsonMap.isEmpty()) {
                    try {
                        JSONObject object = new JSONObject(authJsonMap);
                        //openId 绑定手机号
                        LoginHelper.getInstance().bindPhone(new MyVolleyHttpParamsEntity()
                                .addParam("phone", account)
                                .addParam("verifyCode", code)
                                .addParam("openId", object.getString("openid"))
                                .addParam("platform_name", object.getString("platform"))
                                .addParam("name", object.getString("name"))
                                .addParam("sex", object.getString("gender").equals("男") ? "1" : "0")
                                .addParam("head_portrait", object.getString("profile_image_url")), this);

                    } catch (Exception ex) {
                        ToastUtils.showToast(BindPhoneActivity.this, "获取授权信息失败");
                    }
                } else {
                    ToastUtils.showToast(BindPhoneActivity.this, "获取授权信息失败");
                }
                break;
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
                            //开始60秒倒计时
                            lastTime = 60;
                            handler.sendEmptyMessageDelayed(START_TIMER_DELAY, 1000);
                        }
                        ToastUtils.showToast(BindPhoneActivity.this, volleyHttpResult.getInfo());
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                    }
                });
    }

    @Override
    public void onLoginStart() {
        super.onLoginStart();
        showProgress("正在绑定手机号");
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
        ToastUtils.showToast(this, msg);
        hideProgress();
        enableView(true);
    }

    private void enableView(boolean enable) {
        if (progress != null)
            progress.setVisibility(enable ? View.INVISIBLE : View.VISIBLE);
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
