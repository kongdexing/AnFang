package com.xptschool.parent.ui.watch.chat;

import android.content.Context;
import android.content.Intent;

import com.xptschool.parent.XPTApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dexing on 2017/5/8.
 * 管理聊天服务，视频通话服务
 * 1.启动服务
 * 2.停止服务
 * 3.发送聊天消息
 */

public class ServerManager {

    private static ServerManager mInstance = new ServerManager();
    private ChatService mSocketService;
    private Timer mTimer;
//    public static ExecutorService receiverThreadPool = Executors.newSingleThreadExecutor();
//    public static ExecutorService sendThreadPool = Executors.newFixedThreadPool(5);
    public static ServerManager getInstance() {
        return mInstance;
    }

    public void startService() {
        mTimer = new Timer();

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(XPTApplication.getInstance(), ChatService.class);
                XPTApplication.getInstance().startService(intent);
            }
        }, 1000, 8 * 1000);

    }

    public void stopService(Context context) {
        context.stopService(new Intent(context, ChatService.class));
        if (mTimer != null) {
            mTimer.cancel();
        }

//        ImsSipHelper.getInstance().stopSipServer();
//        NetWorkStatusChangeHelper.getInstance().disableNetWorkChange();
    }



}
