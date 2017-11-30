package com.shuhai.anfang.ui.mine;

import android.os.Bundle;

import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.common.UserType;
import com.shuhai.anfang.ui.main.BaseActivity;

public class MyInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断老师家长
        if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            PInfoView pInfoView = new PInfoView(this);
            setContentView(pInfoView);
        } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            TInfoView tInfoView = new TInfoView(this);
            setContentView(tInfoView);
        }
        setTitle("个人信息");
    }

}
