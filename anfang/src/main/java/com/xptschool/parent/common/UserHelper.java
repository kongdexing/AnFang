package com.xptschool.parent.common;

import android.util.Log;

import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.model.BeanParent;
import com.xptschool.parent.model.BeanTeacher;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.push.UpushTokenHelper;

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
        UserType type = XPTApplication.getInstance().getCurrent_user_type();
        if (UserType.PARENT.equals(type)) {
            BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
            if (parent != null) {
                UpushTokenHelper.exitAccount(parent.getLogin_name(), parent.getU_id());
            }
        } else if (UserType.TEACHER.equals(type)) {
            BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
            if (teacher != null) {
                UpushTokenHelper.exitAccount(teacher.getLogin_name(), teacher.getU_id());
            }
        }
        userLoginSuccess();
    }

    public void userLoginSuccess() {
        Log.i(TAG, "userLoginSuccess: listener size " + listeners.size());
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onUserLoginSuccess();
        }
    }

    public void userExit() {
        Log.i(TAG, "userExit: listener size " + listeners.size());
        XPTApplication.getInstance().setCurrent_user_type("");
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onUserExit();
        }
    }

    public interface UserChangeListener {
        void onUserLoginSuccess();

        void onUserExit();
    }

}
