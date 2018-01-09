package com.xptschool.parent.push;

import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.volley.common.VolleyRequestListener;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.model.BeanParent;
import com.xptschool.parent.model.BeanTeacher;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.util.ParentUtil;

/**
 * Created by dexing on 2017/2/4.
 * No1
 */

public class UpushTokenHelper {

    private static String TAG = UpushTokenHelper.class.getSimpleName();

    /**
     * @param device_token
     */
    public static void uploadDevicesToken(final String device_token, final String push) {
        Log.i(TAG, "uploadDevicesToken: " + device_token + " " + push);
        if (device_token == null || device_token.isEmpty()) {
            return;
        }
        String user_name = "";
        String user_id = "";
        //家长登录
        if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
            if (parent != null) {
                user_name = parent.getLoginName();
                user_id = parent.getU_id();
            }
        } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            //老师登录
            BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
            if (teacher != null) {
                user_name = teacher.getLoginName();
                user_id = teacher.getU_id();
            }
        } else {
            return;
        }

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.HOOK_PUSH_TOKEN,
                new VolleyHttpParamsEntity()
                        .addParam("status", "1")    //1登录 2其他(切换\退出)
                        .addParam("user_name", user_name)
                        .addParam("system_model", "1") //1Android 0ios
                        .addParam("user_id", user_id)
                        .addParam("device_token", device_token)
                        .addParam("mobile_model", android.os.Build.MODEL)
                        .addParam("push_name", push)
                        .addParam("user_type", XPTApplication.getInstance().getCurrent_user_type().toString())
                , new VolleyRequestListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
//                        if (volleyHttpResult.getStatus() == HttpAction.FAILED) {
//                            uploadDevicesToken(device_token, push);
//                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
    }

    /**
     * 退出账号
     */
    public static void exitAccount(String user_name, String user_id) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.HOOK_PUSH_TOKEN,
                new VolleyHttpParamsEntity()
                        .addParam("status", "2")    //1登录 2其他(切换\退出)
                        .addParam("user_name", user_name)
                        .addParam("system_model", "1") //1Android 0ios
                        .addParam("user_id", user_id)
                        .addParam("user_type", "4")
                , new VolleyRequestListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
//                        if (volleyHttpResult.getStatus() == HttpAction.FAILED) {
//                            exitAccount(parent);
//                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

    }

}
