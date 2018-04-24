package com.xptschool.parent.ui.watch;

import android.Manifest;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.widget.audiorecorder.AudioRecorderButton;
import com.android.widget.audiorecorder.Recorder;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.ui.chat.ChatActivity;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ChatDetailActivity extends BaseActivity {

    @BindView(R.id.rlMeVoice)
    RelativeLayout rlMeVoice;

    @BindView(R.id.id_recorder_button)
    AudioRecorderButton mAudioRecorderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        setTitle("小明");

        mAudioRecorderButton.setAudioRecorderCallBack(new AudioRecorderButton.AudioRecorderCallBack() {

            @Override
            public void onStartRecord() {
                SoundPlayHelper.getInstance().stopPlay();
            }

            @Override
            public void onPermissionAsk() {
                Log.i(TAG, "onPermissionAsk: ");
                final int version = Build.VERSION.SDK_INT;
                if (version > 19) {
                    ChatDetailActivityPermissionsDispatcher.onStartRecordingWithCheck(ChatDetailActivity.this);
                } else {
                    ToastUtils.showToast(ChatDetailActivity.this, R.string.permission_voice_rationale);
                    CommonUtil.goAppDetailSettingIntent(ChatDetailActivity.this);
                }
            }

            @Override
            public void onPermissionDenied() {
                Log.i(TAG, "onPermissionDenied: ");
                final int version = Build.VERSION.SDK_INT;
                if (version > 19) {
                    ChatDetailActivityPermissionsDispatcher.onStartRecordingWithCheck(ChatDetailActivity.this);
                } else {
                    ToastUtils.showToast(ChatDetailActivity.this, R.string.permission_voice_never_askagain);
                    CommonUtil.goAppDetailSettingIntent(ChatDetailActivity.this);
                }
            }

            public void onFinish(float seconds, String filePath) {
                Recorder recorder = new Recorder(seconds, filePath);
                File file = new File(recorder.getFilePath());
                Log.i(TAG, "onFinish: " + recorder.getFilePath() + "  " + file.getName());
                rlMeVoice.setVisibility(View.VISIBLE);

                if (file.length() == 0) {
                    return;
                }
                try {
                    CopySdcardFile(recorder.getFilePath(), "/sdcard/" + file.getName());
                } catch (Exception ex) {
                    Log.i(TAG, "viewClick: " + ex.getMessage());
                }
            }

            @Override
            public void onMediaRecorderError(Exception ex) {
                if ("Permission deny!".equals(ex.getMessage())) {
                    ToastUtils.showToast(ChatDetailActivity.this, R.string.permission_voice_never_askagain);
                    CommonUtil.goAppDetailSettingIntent(ChatDetailActivity.this);
                } else {
                    ToastUtils.showToast(ChatDetailActivity.this, R.string.voice_recorder_error);
                }
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions != null && permissions.length > 0) {
            Log.i(TAG, "onRequestPermissionsResult: " + permissions[0]);
        }
        ChatDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.RECORD_AUDIO})
    void onStartRecording() {

    }

    @OnPermissionDenied({Manifest.permission.RECORD_AUDIO})
    void onStartRecordingDenied() {
        Log.i(TAG, "onStartRecordingDenied: ");
        Toast.makeText(this, R.string.permission_voice_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale({Manifest.permission.RECORD_AUDIO})
    void showRationaleForStartRecording(PermissionRequest request) {
        Log.i(TAG, "showRationaleForStartRecording: ");
        request.proceed();
    }

    @OnNeverAskAgain({Manifest.permission.RECORD_AUDIO})
    void onStartRecordingNeverAskAgain() {
        Toast.makeText(this, R.string.permission_voice_never_askagain, Toast.LENGTH_SHORT).show();
        CommonUtil.goAppDetailSettingIntent(this);
    }


    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public int CopySdcardFile(String fromFile, String toFile) {

        try {

            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex) {
            return -1;
        }
    }

}
