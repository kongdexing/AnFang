package com.xptschool.parent.ui.mine;

import android.os.Bundle;

import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.mine.role.PInfoView;
import com.xptschool.parent.ui.mine.role.TInfoView;
import com.xptschool.parent.ui.mine.role.VisitorInfoView;

public class MyInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断老师家长
        UserType type = XPTApplication.getInstance().getCurrent_user_type();

        if (UserType.PARENT.equals(type)) {
            PInfoView pInfoView = new PInfoView(this);
            setContentView(pInfoView);
        } else if (UserType.TEACHER.equals(type)) {
            TInfoView tInfoView = new TInfoView(this);
            setContentView(tInfoView);
        } else {
            VisitorInfoView vInfoView = new VisitorInfoView(this);
            setContentView(vInfoView);
        }

        setTitle("个人信息");
    }

}
