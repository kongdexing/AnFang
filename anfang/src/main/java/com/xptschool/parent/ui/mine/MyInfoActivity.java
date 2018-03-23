package com.xptschool.parent.ui.mine;

import android.os.Bundle;
import android.util.Log;

import com.jph.takephoto.model.TResult;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.ui.album.TakePhotoActivity;
import com.xptschool.parent.ui.mine.role.BaseUserView;
import com.xptschool.parent.ui.mine.role.PInfoView;
import com.xptschool.parent.ui.mine.role.TInfoView;
import com.xptschool.parent.ui.mine.role.VisitorInfoView;

public class MyInfoActivity extends TakePhotoActivity {

    private BaseUserView baseUserView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断老师家长
        UserType type = XPTApplication.getInstance().getCurrent_user_type();

        if (UserType.PARENT.equals(type)) {
            baseUserView = new PInfoView(this);
        } else if (UserType.TEACHER.equals(type)) {
            baseUserView = new TInfoView(this);
        } else {
            baseUserView = new VisitorInfoView(this);
        }
        setContentView(baseUserView);
        setTitle("个人信息");
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Log.i(TAG, "takeFail: " + msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
//        showImg(result.getImages());

        baseUserView.setHeadImage("file://" + result.getImage().getCompressPath(), XPTApplication.getInstance().getCurrentUserId());

    }

}
