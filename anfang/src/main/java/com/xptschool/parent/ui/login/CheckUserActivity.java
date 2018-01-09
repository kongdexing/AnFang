package com.xptschool.parent.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.view.SmoothCheckBox;
import com.xptschool.parent.R;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.http.HttpAction;
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

    @BindView(R.id.cbx_parent)
    SmoothCheckBox cbx_parent;
    @BindView(R.id.cbx_teacher)
    SmoothCheckBox cbx_teacher;
    @BindView(R.id.cbx_visitor)
    SmoothCheckBox cbx_visitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_username);
        setTitle(R.string.title_forgot_password);
        cbx_visitor.setChecked(true);

    }

    @OnClick({R.id.btnNext, R.id.ll_visitor, R.id.cbx_visitor, R.id.ll_parent, R.id.cbx_parent,
            R.id.ll_teacher, R.id.cbx_teacher})
    void buttonOnclick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                String username = edtUserName.getText().toString().trim();
                if (username.isEmpty()) {
                    Toast.makeText(CheckUserActivity.this, R.string.hint_username, Toast.LENGTH_SHORT).show();
                    return;
                }
                String type = "0";
                if (cbx_parent.isChecked()) {
                    type = UserType.PARENT.toString();
                } else if (cbx_teacher.isChecked()) {
                    type = UserType.TEACHER.toString();
                }
                checkUserName(username, type);
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
        }
    }

    private void checkUserName(final String username, String type) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.FORGOT_PWD_STEP1,
                new VolleyHttpParamsEntity()
                        .addParam("type", type)
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
