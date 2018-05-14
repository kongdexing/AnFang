package com.xptschool.parent.ui.watch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.ui.cardset.CardWhiteListActivity;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.mine.MyChildActivity;
import com.xptschool.parent.view.CustomDialog;

import butterknife.OnClick;

/**
 * 孩子设备管理（设备使用者信息，设备绑定，白名单设置）
 */
public class DevicesManageActivity extends BaseActivity {

    BeanStudent currentStudent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_manage);
        setTitle(R.string.mine_devices);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentStudent = XPTApplication.getInstance().getCurrentWatchStu();
    }

    @OnClick({R.id.rlUserInfo, R.id.rlBind, R.id.rlPhone})
    void viewClick(View view) {
        if (currentStudent == null) {
            CustomDialog dialog = new CustomDialog(this);
            dialog.setTitle(R.string.label_tip);
            dialog.setMessage(R.string.message_nowatch);
            dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                @Override
                public void onPositiveClick() {
                    //绑定手表
                    startActivity(new Intent(DevicesManageActivity.this, ScanActivity.class));
                }
            });
            return;
        }
        switch (view.getId()) {
            case R.id.rlUserInfo:
                //使用者信息
                startActivity(new Intent(DevicesManageActivity.this, MyChildActivity.class));
                break;
            case R.id.rlBind:
                //设备绑定|解绑
                Intent intent = new Intent(DevicesManageActivity.this, UnbindActivity.class);
                DevicesManageActivity.this.startActivity(intent);
                break;
            case R.id.rlPhone:
                //
                intent = new Intent(DevicesManageActivity.this, CardWhiteListActivity.class);
                //stu_id
                intent.putExtra(ExtraKey.STUDENT_ID, currentStudent.getStu_id());
                DevicesManageActivity.this.startActivity(intent);
                break;
        }
    }

}
