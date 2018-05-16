package com.xptschool.parent.ui.watch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.watch.chat.ServerManager;
import com.xptschool.parent.util.ToastUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class BindWatchInputActivity extends BaseActivity {

    @BindView(R.id.imgTop)
    ImageView imgTop;
    @BindView(R.id.edtNickName)
    EditText edtNickName;
    @BindView(R.id.edtPhone)
    EditText edtPhone;

    private String currentIMEI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_watch_input);
        setTitle("绑定设备");
        setBtnRight("跳过");

        ViewGroup.LayoutParams params = imgTop.getLayoutParams();
        params.height = XPTApplication.getInstance().getWindowWidth() / 2;
        imgTop.setLayoutParams(params);

        Intent getIntent = getIntent();
        currentIMEI = getIntent.getStringExtra("mScan");

        setBtnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDevice(currentIMEI, "", "");
            }
        });
    }

    @OnClick({R.id.ok})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                String nickName = edtNickName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                if (currentIMEI.isEmpty()) {
                    ToastUtils.showToast(this, R.string.msg_imei_error);
                    return;
                }
                if (nickName.isEmpty()) {
                    ToastUtils.showToast(this, "您还没有输入设备昵称");
                    return;
                }
                if (phone.isEmpty()) {
                    ToastUtils.showToast(this, "您还没有输入设备电话号码");
                    return;
                }
                if (!CommonUtil.isPhone(phone)) {
                    ToastUtils.showToast(this, "您输入的设备手机号不正确，请重新输入");
                    return;
                }

                addDevice(currentIMEI, nickName, phone);
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

                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            try {
                                JSONObject object = new JSONObject(volleyHttpResult.getData().toString());
                                String stu_id = object.getString("stu_id");
                                BeanStudent student = new BeanStudent();
                                student.init();
                                student.setStu_id(stu_id);
                                student.setDevice_type("2");
                                student.setImei_id(imei);
                                student.setStu_name(nickName);
                                student.setCard_phone(phone);
                                //存入数据库
                                GreenDaoHelper.getInstance().insertStudent(student);
                                ToastUtils.showToast(BindWatchInputActivity.this, "绑定成功");
                                //启动服务
                                ServerManager.getInstance().startService();

                                Intent intent = new Intent(BindWatchInputActivity.this,
                                        ScanActivity.class);
                                setResult(4, intent);
                                finish();
                            } catch (Exception ex) {
                                ToastUtils.showToast(BindWatchInputActivity.this, "数据处理错误");
                            }
                        } else {
                            ToastUtils.showToast(BindWatchInputActivity.this, "绑定失败");
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
