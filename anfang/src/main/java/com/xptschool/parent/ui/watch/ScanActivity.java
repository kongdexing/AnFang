package com.xptschool.parent.ui.watch;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
public class ScanActivity extends BaseActivity {

    private CaptureFragment captureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setTitle("扫描二维码");
        setBtnRight("手动绑定");

        setBtnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, BindWatchInput2Activity.class);
                startActivityForResult(intent, 1);
            }
        });

        //申请定位权限
        ScanActivityPermissionsDispatcher.onStartCameraWithCheck(this);
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
        ScanActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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

            Intent intent = new Intent(ScanActivity.this, BindWatchInputActivity.class);
            intent.putExtra("mScan", stmp);
            startActivityForResult(intent, 0);
        }

        @Override
        public void onAnalyzeFailed() {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 4) {
            finish();
        }
        if (requestCode == 1 && resultCode == 5) {
            finish();
        }
    }
}
