package com.xptschool.parent.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class CheckSMSCodeActivity extends BaseActivity {

    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtCode)
    EditText edtCode;
    @BindView(R.id.btnSend)
    Button btnSend;
    private String spSMSKey = "";
    private final int START_TIMER_DELAY = 0;
    private int lastTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_smscode);
        setTitle(R.string.title_forgot_password);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //1.获取上次获取验证码使用的手机号
            //2.已此手机号为键，获取该手机号可重新获取验证码的剩余时间
            spSMSKey = bundle.getString("phone");
        } else {
            spSMSKey = "";
        }
        initView();

    }

    private void initView() {
        //获取可继续发送短信的剩余时间
        long spVal = (long) SharedPreferencesUtil.getData(this, spSMSKey, 0l);

        long diff = (System.currentTimeMillis() - spVal) / 1000;
        int maxTime = 60;
        if (maxTime >= diff) {
            btnSend.setEnabled(false);
            lastTime = (int) (maxTime - diff);
            btnSend.setText(lastTime + "秒后重发");
            handler.sendEmptyMessageDelayed(START_TIMER_DELAY, 1000);
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

    @OnClick({R.id.btnSend, R.id.btnNext})
    void viewOnClick(View view) {
        String phone = edtPhone.getText().toString().trim();
        if (phone.isEmpty() || !CommonUtil.isPhone(phone)) {
            ToastUtils.showToast(this, R.string.input_error_phone);
            return;
        }
        switch (view.getId()) {
            case R.id.btnSend:

                getVerifyCode(phone);
                break;
            case R.id.btnNext:
                String code = edtCode.getText().toString().trim();
                if (code.isEmpty()) {
                    ToastUtils.showToast(CheckSMSCodeActivity.this, R.string.hint_code);
                    return;
                }
                checkVerifyCode(phone, code);
                break;
        }
    }

    private void getVerifyCode(String phone) {
        if (spSMSKey.isEmpty()) {
            spSMSKey = phone;
        }

        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_PHONE_FORGET_PWD, phone);

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.FORGOT_PWD_STEP2,
                new MyVolleyHttpParamsEntity()
                        .addParam("phone", phone)
                        .addParam("username", phone), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            edtPhone.setEnabled(false);
                            SharedPreferencesUtil.saveData(CheckSMSCodeActivity.this, spSMSKey, System.currentTimeMillis());
                            //开始60秒倒计时
                            lastTime = 60;
                            handler.sendEmptyMessageDelayed(START_TIMER_DELAY, 1000);
                        }
                        ToastUtils.showToast(CheckSMSCodeActivity.this, volleyHttpResult.getInfo());
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                    }
                });
    }

    private void checkVerifyCode(final String phone, String code) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.FORGOT_PWD_STEP3,
                new MyVolleyHttpParamsEntity()
                        .addParam("code", code)
                        .addParam("username", phone), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            //验证成功，关闭当前页面，进入设置密码界面
                            Intent intent = new Intent(CheckSMSCodeActivity.this, SetPasswordActivity.class);
                            intent.putExtra("username", phone);
                            startActivity(intent);
                            finish();
                        }
                        ToastUtils.showToast(CheckSMSCodeActivity.this, volleyHttpResult.getInfo());
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                    }
                });
    }

}
