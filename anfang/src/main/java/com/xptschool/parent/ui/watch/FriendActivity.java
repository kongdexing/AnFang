package com.xptschool.parent.ui.watch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.xptschool.parent.R;
import com.xptschool.parent.ui.login.LoginActivity;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.CustomDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class FriendActivity extends BaseActivity {

    @BindView(R.id.rlFriend1)
    RelativeLayout rlFriend1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        setTitle(R.string.home_friend);

    }

    @OnClick({R.id.btnDelete})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDelete:
                //弹出登录对话框
                CustomDialog dialog = new CustomDialog(this);
                dialog.setTitle(R.string.label_tip);
                dialog.setMessage("确定要删除该好友吗？");
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        rlFriend1.setVisibility(View.GONE);
                        ToastUtils.showToast(FriendActivity.this, "删除成功");
                    }
                });
                break;
        }
    }


}
