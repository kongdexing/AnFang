package com.xptschool.parent.ui.register;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtNickName)
    EditText edtNickName;
    @BindView(R.id.edtCode)
    EditText edtCode;
    @BindView(R.id.edtPwd)
    EditText edtPwd;
    @BindView(R.id.edtRePwd)
    EditText edtRePwd;

    @BindView(R.id.btnSend)
    Button btnSend;
    private int lastTime = 0;
    private final int START_TIMER_DELAY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle(R.string.title_register);

        //获取可继续发送短信的剩余时间
        long spVal = (long) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_LAST_REGISTER, 0l);

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

    @OnClick({R.id.btnSend, R.id.btnRegister})
    void viewOnClick(View view) {
        String phone = edtPhone.getText().toString().trim();
        if (phone.isEmpty() || !CommonUtil.isPhone(phone)) {
            ToastUtils.showToast(this, R.string.hint_phone);
            edtPhone.setError(getResources().getString(R.string.hint_phone));
            return;
        }

        switch (view.getId()) {
            case R.id.btnSend:
                getVerifyCode(phone);
                break;
            case R.id.btnRegister:
                String code = edtCode.getText().toString().trim();
                if (code.isEmpty()) {
                    ToastUtils.showToast(RegisterActivity.this, R.string.hint_code);
                    edtCode.setError(getResources().getString(R.string.hint_code));
                    return;
                }
//                String userName = edtNickName.getText().toString().trim();
//                if (userName.isEmpty()) {
//                    ToastUtils.showToast(RegisterActivity.this, R.string.hint_nickname);
//                    edtNickName.setError(getResources().getString(R.string.hint_nickname));
//                    return;
//                }
                String pwd = edtPwd.getText().toString().trim();
                if (pwd.isEmpty()) {
                    ToastUtils.showToast(RegisterActivity.this, R.string.hint_userpwd);
                    edtPwd.setError(getResources().getString(R.string.hint_userpwd));
                    return;
                }
//                String repwd = edtRePwd.getText().toString().trim();
//                if (repwd.isEmpty()) {
//                    ToastUtils.showToast(RegisterActivity.this, R.string.hint_new_pwd2);
//                    edtRePwd.setError(getResources().getString(R.string.hint_new_pwd2));
//                    return;
//                }
//                if (!pwd.equals(repwd)) {
//                    ToastUtils.showToast(RegisterActivity.this, R.string.toast_pwd_not_equal);
//                    edtRePwd.setSelection(repwd.length());
//                    return;
//                }

                register(phone, "", code, CommonUtil.md5(pwd));
//                checkVerifyCode(code);
                break;
        }
    }

    @OnClick({R.id.imgDel1, R.id.imgDel2, R.id.imgDel3, R.id.imgDel4})
    void onDelClick(View view) {
        switch (view.getId()) {
            case R.id.imgDel1:
                edtPhone.setText("");
                break;
            case R.id.imgDel2:
                edtNickName.setText("");
                break;
            case R.id.imgDel3:
                edtPwd.setText("");
                break;
            case R.id.imgDel4:
                edtRePwd.setText("");
                break;
        }
    }

    private void getVerifyCode(final String phone) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.REGISTER_GETCODE,
                new VolleyHttpParamsEntity()
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
                            SharedPreferencesUtil.saveData(RegisterActivity.this, SharedPreferencesUtil.KEY_LAST_REGISTER, System.currentTimeMillis());
                            //开始60秒倒计时
                            lastTime = 60;
                            handler.sendEmptyMessageDelayed(START_TIMER_DELAY, 1000);
                        }
                        ToastUtils.showToast(RegisterActivity.this, volleyHttpResult.getInfo());
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                    }
                });
    }

    private void register(String phone, String userName, String code, String pwd) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.REGISTER,
                new VolleyHttpParamsEntity()
                        .addParam("phone", phone)
                        .addParam("name", userName)
                        .addParam("code", code)
                        .addParam("pwd", pwd), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在注册...");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        ToastUtils.showToast(RegisterActivity.this, volleyHttpResult.getInfo());
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
//                            startActivity(new Intent(RegisterActivity.this, CheckRoleActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                    }
                });
    }

}
