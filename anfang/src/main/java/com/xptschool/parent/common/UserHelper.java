package com.xptschool.parent.common;

import android.util.Log;

import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.model.BeanParent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.push.UpushTokenHelper;
import com.xptschool.parent.ui.watch.chat.ServerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexing on 2017-11-16 0016.
 */

public class UserHelper {

    private String TAG = UserHelper.class.getSimpleName();
    private static UserHelper mInstance;
    private List<UserChangeListener> listeners = new ArrayList<>();

    private UserHelper() {

    }

    public static synchronized UserHelper getInstance() {

        if (mInstance == null) {
            mInstance = new UserHelper();
        }

        return mInstance;
    }

    public void addUserChangeListener(UserChangeListener listener) {
        if (listener == null) {
            return;
        }
        listeners.add(listener);
    }

    /**
     * 登录成功，切换账号信息
     */
    public void changeAccount() {
        BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
        if (parent != null) {
            UpushTokenHelper.exitAccount(parent.getUsername(), parent.getUser_id());
        }
        userLoginSuccess();
    }

    public void userLoginSuccess() {
        //判断有无手表
        if (XPTApplication.getInstance().hasWatch()) {
            ServerManager.getInstance().startService();
        }

        Log.i(TAG, "userLoginSuccess: listener size " + listeners.size());
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onUserLoginSuccess();
        }
    }

    public void userExit() {
        ServerManager.getInstance().stopService(XPTApplication.getInstance());
        Log.i(TAG, "userExit: listener size " + listeners.size());
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onUserExit();
        }
    }

    public interface UserChangeListener {
        void onUserLoginSuccess();

        void onUserExit();
    }

}
