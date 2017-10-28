package com.shuhai.anfang.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.shuhai.anfang.R;
import com.shuhai.anfang.ui.main.MainActivity;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;

/**
 * Created by dexing on 2017/5/19.
 * No1
 */

public class MyPushIntentService extends UmengMessageService {

    private static final String TAG = MyPushIntentService.class.getName();
    private static int notifyId = 0;

    @Override
    public void onMessage(Context context, Intent intent) {
        String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        UMessage msg = null;
        Intent mainIntent = new Intent(this, MainActivity.class);
        boolean notify = true;
        try {

        } catch (Exception ex) {
            Log.i(TAG, "onMessage: " + ex.getMessage());
        } finally {
            if (notify) {
                PendingIntent mainPendingIntent = PendingIntent.getActivity(this, notifyId++, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                //消息提醒
                NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(msg == null ? "新消息提醒" : msg.title)
                        .setContentText(msg == null ? "" : msg.text)
                        .setTicker(msg == null ? "" : msg.ticker)
                        .setContentIntent(mainPendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true);
                mNotifyManager.notify(notifyId, builder.build());
            }
        }
    }
}
