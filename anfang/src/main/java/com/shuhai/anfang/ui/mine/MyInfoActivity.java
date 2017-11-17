package com.shuhai.anfang.ui.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.widget.view.CircularImageView;
import com.shuhai.anfang.R;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.common.UserHelper;
import com.shuhai.anfang.model.BeanParent;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.server.ServerManager;
import com.shuhai.anfang.ui.main.BaseActivity;
import com.shuhai.anfang.view.CustomDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class MyInfoActivity extends BaseActivity {

    @BindView(R.id.imgHead)
    CircularImageView imgHead;

    @BindView(R.id.txtMineName)
    TextView txtMineName;

    @BindView(R.id.txtPhone)
    TextView txtPhone;

    @BindView(R.id.txtEmail)
    TextView txtEmail;

    @BindView(R.id.txtHomeAdd)
    TextView txtHomeAdd;

    @BindView(R.id.txtHomeTel)
    TextView txtHomeTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        setTitle("个人信息");
        initData();
    }

    private void initData() {
        BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
        if (parent != null) {
            if (parent.getSex().equals("1")) {
                imgHead.setImageResource(R.drawable.parent_father);
            } else {
                imgHead.setImageResource(R.drawable.parent_mother);
            }

            txtMineName.setText(parent.getParent_name());
            txtPhone.setText(parent.getParent_phone());
            txtEmail.setText(parent.getEmail());
            txtHomeAdd.setText(parent.getAddress());
            txtHomeTel.setText(parent.getFamily_tel());
        }
    }

    @OnClick({R.id.rlMinePhoto, R.id.rlMinePhone, R.id.rlAddressPhone, R.id.rlExit})
    void viewClick(View view) {
        BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
        if (parent == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.rlMinePhoto:

                break;
            case R.id.rlMinePhone:
                if (parent.getParent_phone().isEmpty()) {
                    Toast.makeText(this, R.string.toast_phone_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + parent.getParent_phone()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception ex) {
                    Log.i(TAG, "toCallPhone: " + ex.getMessage());
                    Toast.makeText(this, R.string.toast_startcall_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rlAddressPhone:
                if (parent.getFamily_tel().isEmpty()) {
                    Toast.makeText(this, R.string.toast_phone_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + parent.getFamily_tel()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception ex) {
                    Log.i(TAG, "toCallPhone: " + ex.getMessage());
                    Toast.makeText(this, R.string.toast_startcall_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rlExit:
                CustomDialog dialog = new CustomDialog(MyInfoActivity.this);
                dialog.setTitle(R.string.label_tip);
                dialog.setMessage(R.string.msg_exit);
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        //清除upush信息
                        UserHelper.getInstance().changeAccount();

                        //清除数据
                        SharedPreferencesUtil.clearUserInfo(MyInfoActivity.this);

                        GreenDaoHelper.getInstance().clearData();

                        ServerManager.getInstance().stopService(MyInfoActivity.this);

                        finish();
                    }
                });
                break;
        }
    }


}
