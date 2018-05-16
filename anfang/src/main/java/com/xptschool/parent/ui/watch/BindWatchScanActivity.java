package com.xptschool.parent.ui.watch;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.ui.main.BaseActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * 定制化显示扫描界面
 */
@RuntimePermissions
public class BindWatchScanActivity extends BaseBindWatchActivity {

    private CaptureFragment captureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setTitle("扫描二维码");

        setRightImage(R.drawable.ic_input_imei);

        setRightImageViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BindWatchScanActivity.this, BindWatchIMEIActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        //申请定位权限
        BindWatchScanActivityPermissionsDispatcher.onStartCameraWithCheck(this);
    }

    public static boolean isOpen = false;

    private void initView() {
//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear1);
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isOpen) {
//                    CodeUtils.isLightEnable(true);
//                    isOpen = true;
//                } else {
//                    CodeUtils.isLightEnable(false);
//                    isOpen = false;
//                }
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions != null && permissions.length > 0) {
            Log.i(TAG, "onRequestPermissionsResult: " + permissions[0]);
        }
        BindWatchScanActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.CAMERA})
    public void onStartCamera() {
        Log.i(TAG, "onStartCamera: ");
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        initView();
    }

    @OnPermissionDenied({Manifest.permission.CAMERA})
    void onStartCameraDenied() {
        Log.i(TAG, "onStartCameraDenied: ");
        Toast.makeText(this, R.string.permission_voice_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale({Manifest.permission.CAMERA})
    void showRationaleForStartCamera(PermissionRequest request) {
        Log.i(TAG, "showRationaleForStartCamera: ");
        request.proceed();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA})
    void onStartCameraNeverAskAgain() {
        Log.i(TAG, "onStartCameraNeverAskAgain: ");
        CommonUtil.goAppDetailSettingIntent(this);
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Log.i(TAG, "onAnalyzeSuccess: " + result);
            String stmp = result;
            if (stmp.length() > 4) {
                stmp = stmp.substring(4);
            }
            Log.i(TAG, "onAnalyzeSuccess: " + stmp);
            if (stmp.length() > 1) {
                stmp = stmp.substring(0, stmp.length() - 1);
            }
            Log.i(TAG, "onAnalyzeSuccess: " + stmp);
            addDevice(stmp);
        }

        @Override
        public void onAnalyzeFailed() {
        }
    };

    @Override
    public void onBindSuccess() {
        finish();
    }

    @Override
    public void onBindFailed() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 11) {
            //手动录入成功
            finish();
        }
    }


}
