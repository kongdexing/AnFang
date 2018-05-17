package com.xptschool.parent.ui.watch;

import android.content.Intent;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.UserHelper;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.watch.chat.ServerManager;
import com.xptschool.parent.util.ToastUtils;

import org.json.JSONObject;

public abstract class BaseBindWatchActivity extends BaseActivity{

    public void addDevice(final String imei) {

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_WATCH_Bind,
                new VolleyHttpParamsEntity().addParam("imei_id", imei)
                        .addParam("devicetype", "2")
                        .addParam("status", "1")
                        .addParam("user_id", XPTApplication.getInstance().getCurrentUserId()),
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
                                //存入数据库
                                GreenDaoHelper.getInstance().insertStudent(student);
                                ToastUtils.showToast(BaseBindWatchActivity.this, "绑定成功");
                                //启动服务
                                ServerManager.getInstance().startService();

                                onBindSuccess();
                                finish();
                            } catch (Exception ex) {
                                ToastUtils.showToast(BaseBindWatchActivity.this, "数据处理错误");
                            }
                        } else {
                            ToastUtils.showToast(BaseBindWatchActivity.this, "绑定失败");
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                    }
                });
    }

    public void onBindSuccess(){
        UserHelper.getInstance().userLoginSuccess();
    };
    public void onBindFailed(){

    };

}
