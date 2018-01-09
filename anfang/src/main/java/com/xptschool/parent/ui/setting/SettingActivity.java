package com.xptschool.parent.ui.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.xptschool.parent.BuildConfig;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.common.UserHelper;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.main.WebViewActivity;
import com.xptschool.parent.view.CustomDialog;
import com.tencent.bugly.beta.Beta;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.txtVersion)
    TextView txtVersion;

    @BindView(R.id.rlChangePwd)
    RelativeLayout rlChangePwd;

    @BindView(R.id.rltutelage)
    RelativeLayout rltutelage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(R.string.mine_setting);

        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            txtVersion.setText(info.versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            rltutelage.setVisibility(View.VISIBLE);
        } else {
            rltutelage.setVisibility(View.GONE);
        }

        if (XPTApplication.getInstance().isLoggedIn()) {
            rlChangePwd.setVisibility(View.VISIBLE);
        } else {
            rlChangePwd.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.rlChangePwd, R.id.rlTel, R.id.rlUpdate, R.id.rltutelage, R.id.rlHelp, R.id.rlExit})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.rlTel:
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + getString(R.string.company_tel)));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(this, R.string.toast_startcall_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rlUpdate:
                Beta.checkUpgrade();
                break;
            case R.id.rlChangePwd:
                startActivity(new Intent(this, ChangePwdActivity.class));
                break;
            case R.id.rltutelage:
                List<BeanStudent> students = GreenDaoHelper.getInstance().getStudents();
                if (students.size() != 0) {
                    startActivity(new Intent(this, TutelageActivity.class));
                } else {
                    Toast.makeText(this, "暂无绑定的学生", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rlHelp:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(ExtraKey.WEB_URL, BuildConfig.SERVICE_URL + "/html/app-help/index.html");
                startActivity(intent);
                break;
            case R.id.rlExit:
                CustomDialog dialog = new CustomDialog(this);
                dialog.setTitle(R.string.label_tip);
                dialog.setMessage(R.string.msg_exit);
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        //清除数据
                        SharedPreferencesUtil.clearUserInfo(SettingActivity.this);
                        GreenDaoHelper.getInstance().clearData();
                        UserHelper.getInstance().userExit();

                        EMClient.getInstance().logout(true, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                SettingActivity.this.finish();
                                Log.i(TAG, "logout onSuccess: ");
                            }

                            @Override
                            public void onError(int i, String s) {
                                Log.i(TAG, "logout onError: " + s);
                            }

                            @Override
                            public void onProgress(int i, String s) {
                                Log.i(TAG, "logout onProgress: " + i);
                            }
                        });

                    }
                });
                break;
        }
    }

}
