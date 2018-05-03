package com.xptschool.parent.ui.watch;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.audiorecorder.AudioRecorderButton;
import com.android.widget.audiorecorder.Recorder;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.adapter.WrapContentLinearLayoutManager;
import com.xptschool.parent.common.BroadcastAction;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.BeanWChat;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.chat.ChatActivity;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.main.BaseListActivity;
import com.xptschool.parent.util.ChatUtil;
import com.xptschool.parent.util.KeyboardChangeListener;
import com.xptschool.parent.util.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ChatDetailActivity extends BaseListActivity {

    @BindView(R.id.RlParent)
    RelativeLayout RlParent;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    @BindView(R.id.imgVoiceOrText)
    ImageView imgVoiceOrText;

    @BindView(R.id.id_recorder_button)
    AudioRecorderButton mAudioRecorderButton;

    @BindView(R.id.edtContent)
    EmojiconEditText edtContent;

    @BindView(R.id.btnSend)
    Button btnSend;

    private ChatAdapter adapter = null;

    BeanStudent currentStudent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        setTitle(R.string.home_chat);

        //获取学生信息
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentStudent = (BeanStudent) bundle.getSerializable("student");
        }

        if (currentStudent == null) {
            ToastUtils.showToast(this, "未获取设备信息");
            return;
        } else {
            String nickName = currentStudent.getStu_name();
            String imei = currentStudent.getImei_id();
            if (nickName == null || nickName.isEmpty()) {
                setTitle(imei);
            } else {
                setTitle(nickName);
            }
        }
        initView();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.WCHAT_MESSAGE_RECEIVED);
        this.registerReceiver(messageReceiver, filter);

    }

    private void initView() {
        recycleView.setHasFixedSize(true);
        WrapContentLinearLayoutManager mLayoutManager = new WrapContentLinearLayoutManager(this);
        recycleView.setLayoutManager(mLayoutManager);

//        initRecyclerView(recycleView, swipeRefreshLayout);

        swipeRefreshLayout.setEnabled(false);

        LinearLayoutManager layoutManager = (LinearLayoutManager) recycleView.getLayoutManager();
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        adapter = new ChatAdapter(this);
        recycleView.setAdapter(adapter);

        KeyboardChangeListener mKeyboardChangeListener = new KeyboardChangeListener(this);
        mKeyboardChangeListener.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    smoothBottom();
                }
            }
        });

        getChatList();

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
//                rlMeVoice.setVisibility(View.VISIBLE);

                if (file.length() == 0) {
                    return;
                }

                BeanWChat chat = new BeanWChat();
                chat.setChatId(CommonUtil.getUUID());
                chat.setUser_id(XPTApplication.getInstance().getCurrentUserId());
                chat.setDevice_id(currentStudent.getImei_id());
                chat.setFileName(recorder.getFilePath());
                chat.setIsSend(true);
                chat.setText("");
                chat.setType(ChatUtil.TYPE_AMR);
                chat.setSeconds("");
                chat.setTime(CommonUtil.getCurrentDateHms());
                addSendingMsg(chat);

                sendMessage(recorder.getFilePath(), ChatUtil.TYPE_AMR);

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

    @OnClick({R.id.imgVoiceOrText, R.id.btnSend})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.imgVoiceOrText:
                if (edtContent.getVisibility() == View.GONE) {
                    edtContent.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.VISIBLE);
                    mAudioRecorderButton.setVisibility(View.GONE);
                } else {
                    mAudioRecorderButton.setVisibility(View.VISIBLE);
                    edtContent.setVisibility(View.GONE);
                    btnSend.setVisibility(View.GONE);
                }
                break;
            case R.id.btnSend:
                String content = edtContent.getText().toString().trim();
                if (content.isEmpty()) {
                    ToastUtils.showToast(this, "未填写发送信息");
                    return;
                }
                BeanWChat chat = new BeanWChat();
                chat.setChatId(CommonUtil.getUUID());
                chat.setUser_id(XPTApplication.getInstance().getCurrentUserId());
                chat.setDevice_id(currentStudent.getImei_id());
                chat.setFileName("");
                chat.setIsSend(true);
                chat.setText(content);
                chat.setType(ChatUtil.TYPE_TEXT);
                chat.setSeconds("");
                chat.setTime(CommonUtil.getCurrentDateHms());
                edtContent.setText("");
                addSendingMsg(chat);
                sendMessage(content, ChatUtil.TYPE_TEXT);
                break;
        }
    }

    private void getChatList() {
        List<BeanWChat> pageChatList = GreenDaoHelper.getInstance().getChatsByDeviceId(currentStudent.getImei_id());
        if (pageChatList.size() == 0) {
            return;
        }

        adapter.appendData(pageChatList);
//        currentOffset = adapter.getItemCount();
    }

    private void addSendingMsg(BeanWChat chat) {
        adapter.addData(chat);
        smoothBottom();
        GreenDaoHelper.getInstance().insertChat(chat);
    }

    private void sendMessage(final String message, String msgType) {
        VolleyHttpParamsEntity entity = new MyVolleyHttpParamsEntity()
                .addParam("imei", currentStudent.getImei_id())
                .addParam("type", msgType)
                .addParam("user_id", XPTApplication.getInstance().getCurrentUserId());

        if (ChatUtil.TYPE_TEXT.equals(msgType)) {
            entity.addParam("contents", message);

            VolleyHttpService.getInstance().sendPostRequest(HttpAction.POST_WCHAT_MESSAGE,
                    entity, myVolleyRequestListener);
        } else if (ChatUtil.TYPE_AMR.equals(msgType)) {
            List<String> uploadFiles = new ArrayList<>();
            uploadFiles.add(message);

            VolleyHttpService.getInstance().uploadFiles(HttpAction.POST_WCHAT_MESSAGE,
                    entity, uploadFiles, myVolleyRequestListener);

        }

    }

    MyVolleyRequestListener myVolleyRequestListener = new MyVolleyRequestListener() {
        @Override
        public void onStart() {
            super.onStart();
            showProgress("正在发送");
        }

        @Override
        public void onResponse(VolleyHttpResult volleyHttpResult) {
            super.onResponse(volleyHttpResult);
            hideProgress();
            ToastUtils.showToast(ChatDetailActivity.this, volleyHttpResult.getInfo());
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            super.onErrorResponse(volleyError);
            hideProgress();

        }
    };

    private void smoothBottom() {
        recycleView.smoothScrollToPosition(0);
    }

    public BroadcastReceiver messageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: " + action);
//            if (action.equals(BroadcastAction.WCHAT_MESSAGE_RECEIVED)) {
//                return;
//            }

            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                Log.i(TAG, "onReceive: bundle is null");
                return;
            }

            BeanWChat chat = (BeanWChat) bundle.getSerializable("chat");
            Log.i(TAG, "onReceive: " + chat.getFileName());
            adapter.addData(chat);
            smoothBottom();
        }
    };

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
