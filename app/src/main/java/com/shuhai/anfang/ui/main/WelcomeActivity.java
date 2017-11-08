package com.shuhai.anfang.ui.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shuhai.anfang.R;
import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.common.ExtraKey;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.xiaomi.ad.SplashAdListener;
import com.xiaomi.ad.adView.SplashAd;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.rlAD_bottom)
    RelativeLayout rlAD_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(XPTApplication.TAG, "WelcomeActivity onCreate: ");

        setContentView(R.layout.activity_welcome);
        llContent.setBackgroundColor(Color.TRANSPARENT);
        showActionBar(false);

        new SplashAd(this, rlAD_bottom, R.drawable.company_logo, new SplashAdListener() {
            @Override
            public void onAdPresent() {

            }

            @Override
            public void onAdClick() {

            }

            @Override
            public void onAdDismissed() {

            }

            @Override
            public void onAdFailed(String s) {
                Log.i(TAG, "onAdFailed: "+s);
            }
        })
                .requestAd("9820dad84ce71c6015ef8d3a4bbcfcee");


        analyLogin();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions != null && permissions.length > 0) {
            Log.i(TAG, "onRequestPermissionsResult: " + permissions[0]);
        }
        WelcomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO})
    void canReadPhoneState() {
        Log.i(TAG, "canReadPhoneState: ");
        analyLogin();
    }

    private void analyLogin() {
        final Intent intent = new Intent();
        String userName = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_NAME, "");
        String password = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_PWD, "");
        String splash_init = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_SPLASH_INIT, "0");

        if (splash_init.equals("0")) {
            Intent[] intents = new Intent[2];
            intents[1] = new Intent(this, SplashActivity.class);
            intents[0] = new Intent(this, MainActivity.class);
            intents[0].putExtra(ExtraKey.LOGIN_ORIGIN, "0");
            startActivities(intents);
            finish();
            return;
        }


        if (password.isEmpty() || userName.isEmpty()) {

        } else {
            //login
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent.setClass(WelcomeActivity.this, MainActivity.class);
                intent.putExtra(ExtraKey.LOGIN_ORIGIN, "0");
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO})
    void onReadPhoneStateDenied() {
        Log.i(TAG, "onReadPhoneStateDenied: ");
        Toast.makeText(this, R.string.permission_readphonestate_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale({Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO})
    void showRationaleForReadPhoneState(PermissionRequest request) {
        Log.i(TAG, "showRationaleForReadPhoneState: ");
        request.proceed();
    }

    @OnNeverAskAgain({Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO})
    void onReadPhoneStateNeverAskAgain() {
        Log.i(TAG, "onReadPhoneStateNeverAskAgain: ");
        Toast.makeText(this, R.string.permission_never_askagain, Toast.LENGTH_SHORT).show();
        CommonUtil.goAppDetailSettingIntent(this);
    }

//    @Override
//    protected void onStartLogin() {
//        super.onStartLogin();
//    }
//
//    @Override
//    protected void onLoginSuccess() {
//        super.onLoginSuccess();
//        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
//        finish();
//    }
//
//    @Override
//    protected void onLoginFailed(String msg) {
//        super.onLoginFailed(msg);
//        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
//        finish();
//    }

}
