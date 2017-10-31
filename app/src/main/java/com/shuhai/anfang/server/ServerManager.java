package com.shuhai.anfang.server;

import android.content.Context;

import com.shuhai.anfang.imsdroid.ImsSipHelper;
import com.shuhai.anfang.imsdroid.NetWorkStatusChangeHelper;

/**
 * Created by dexing on 2017/5/8.
 * 管理聊天服务，视频通话服务
 * 1.启动服务
 * 2.停止服务
 * 3.发送聊天消息
 */

public class ServerManager {

    private static ServerManager mInstance = new ServerManager();
    public static ServerManager getInstance() {
        return mInstance;
    }

    public void startService() {
        ImsSipHelper.getInstance().startEngine();
    }

    public void stopService(Context context) {

        ImsSipHelper.getInstance().stopSipServer();
        NetWorkStatusChangeHelper.getInstance().disableNetWorkChange();
    }

}
