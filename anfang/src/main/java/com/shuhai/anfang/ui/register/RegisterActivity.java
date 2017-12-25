package com.shuhai.anfang.ui.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.shuhai.anfang.R;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.http.HttpAction;
import com.shuhai.anfang.http.MyVolleyRequestListener;
import com.shuhai.anfang.ui.login.CheckSMSCodeActivity;
import com.shuhai.anfang.ui.main.BaseActivity;
import com.shuhai.anfang.util.ToastUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle(R.string.title_register);


    }


    @OnClick({R.id.btnSend, R.id.btnRegister})
    void viewOnClick(View view) {
        String phone = edtPhone.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btnSend:
                if (phone.isEmpty()) {
                    ToastUtils.showToast(this, R.string.hint_phone);
                    return;
                }
                getVerifyCode(phone);
                break;
            case R.id.btnNext:
                String code = edtCode.getText().toString().trim();
                if (code.isEmpty()) {
                    ToastUtils.showToast(RegisterActivity.this, R.string.hint_code);
                    return;
                }
//                checkVerifyCode(code);
                break;
        }
    }

    private void getVerifyCode(String phone) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.FORGOT_PWD_STEP2,
                new VolleyHttpParamsEntity()
                        .addParam("phone", phone), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
//                            SharedPreferencesUtil.saveData(RegisterActivity.this, spSMSKey, System.currentTimeMillis());
//                            //开始60秒倒计时
//                            lastTime = 60;
//                            handler.sendEmptyMessageDelayed(START_TIMER_DELAY, 1000);
                        }
                        ToastUtils.showToast(RegisterActivity.this, volleyHttpResult.getInfo());
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                    }
                });
    }


}
