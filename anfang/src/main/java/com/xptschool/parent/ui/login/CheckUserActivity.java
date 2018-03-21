package com.xptschool.parent.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/*
    忘记密码-输入账号
 */
public class CheckUserActivity extends BaseActivity {

    @BindView(R.id.edtUserName)
    EditText edtUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_username);
        setTitle(R.string.title_forgot_password);
    }

    @OnClick({R.id.btnNext})
    void buttonOnclick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                String username = edtUserName.getText().toString().trim();
                if (username.isEmpty()) {
                    Toast.makeText(CheckUserActivity.this, R.string.hint_username, Toast.LENGTH_SHORT).show();
                    return;
                }
                checkUserName(username);
                break;
        }
    }

    private void checkUserName(final String username) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.FORGOT_PWD_STEP1,
                new MyVolleyHttpParamsEntity()
                        .addParam("username", username), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            Intent intent = new Intent(CheckUserActivity.this, CheckSMSCodeActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        } else {
                            ToastUtils.showToast(CheckUserActivity.this, volleyHttpResult.getInfo());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                    }
                });
    }

}
