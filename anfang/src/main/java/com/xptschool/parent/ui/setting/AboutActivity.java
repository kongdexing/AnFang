package com.xptschool.parent.ui.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.beta.Beta;
import com.xptschool.parent.BuildConfig;
import com.xptschool.parent.R;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.main.WebCommonActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.txtVersion)
    TextView txtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle(R.string.mine_about);

        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            txtVersion.setText(info.versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.rlInstruction, R.id.rlTel, R.id.rlUpdate})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.rlInstruction:
                Intent intent = new Intent(AboutActivity.this, WebCommonActivity.class);
                intent.putExtra(ExtraKey.WEB_URL, BuildConfig.APP_INSTRUCTION_URL);
                startActivity(intent);
                break;
            case R.id.rlTel:
                try {
                    intent = new Intent();
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
        }
    }
}
