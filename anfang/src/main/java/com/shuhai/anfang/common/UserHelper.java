package com.shuhai.anfang.common;

import android.util.Log;

import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.model.BeanParent;
import com.shuhai.anfang.model.BeanTeacher;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.push.UpushTokenHelper;

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
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * 切换账号
     */
    public void changeAccount() {
        if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
            if (parent != null) {
                UpushTokenHelper.exitAccount(parent.getLogin_name(), parent.getU_id());
            }
        } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
            if (teacher != null) {
                UpushTokenHelper.exitAccount(teacher.getLogin_name(), teacher.getU_id());
            }
        }
        Log.i(TAG, "changeAccount: " + listeners.size());
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onUserLoginSuccess();
        }
    }

    public interface UserChangeListener {
        void onUserLoginSuccess();
    }

}
