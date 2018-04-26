package com.xptschool.parent.ui.watch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class BindWatchInputActivity extends BaseActivity {

    @BindView(R.id.edtImei)
    EditText edtImei;
    @BindView(R.id.edtNickName)
    EditText edtNickName;
    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_watch_input);

    }

    @OnClick({R.id.ok})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                String imei = edtImei.getText().toString().trim();
                String nickName = edtNickName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();

                if (imei.isEmpty() || imei.length() != 15) {
                    ToastUtils.showToast(this, R.string.msg_imei_error);
                    return;
                }

                addDevice(imei, nickName, phone);
                break;
        }
    }

    private void addDevice(final String imei, final String nickName, final String phone) {

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_WATCH_Bind,
                new VolleyHttpParamsEntity().addParam("imei_id", imei)
                        .addParam("devicetype", "2")
                        .addParam("status", "1")
                        .addParam("user_id", XPTApplication.getInstance().getCurrentUserId())
                        .addParam("stu_phone", phone)
                        .addParam("stu_name", nickName),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在添加设备");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        ToastUtils.showToast(BindWatchInputActivity.this, volleyHttpResult.getInfo());
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            try {
                                JSONObject object = new JSONObject(volleyHttpResult.getData().toString());
                                String stu_id = object.getString("stu_id");
                                BeanStudent student = new BeanStudent();
                                student.setStu_id(stu_id);
                                student.setDevice_type("2");
                                student.setImei_id(imei);
                                student.setStu_name(nickName);
                                student.setCard_phone(phone);
                                //存入数据库
                                GreenDaoHelper.getInstance().insertStudent(student);
                                setResult(1);
                                finish();
                            } catch (Exception ex) {
                                ToastUtils.showToast(BindWatchInputActivity.this, "数据处理错误");
                            }
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
