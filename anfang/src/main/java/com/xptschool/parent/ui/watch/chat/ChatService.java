package com.xptschool.parent.ui.watch.chat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.volley.common.VolleyRequestListener;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.BroadcastAction;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanWChat;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.util.ChatUtil;

import org.json.JSONObject;

public class ChatService extends Service {
    public ChatService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getUnReadMessage();
        return super.onStartCommand(intent, flags, startId);
    }

    private void getUnReadMessage() {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_UNREAD_MESSAGE,
                new VolleyHttpParamsEntity().addParam("imei", XPTApplication.getInstance().getCurrentWatchIMEI()),
                new VolleyRequestListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    JSONObject object = new JSONObject(volleyHttpResult.getData().toString());
                                    BeanWChat chat = new BeanWChat();
                                    chat.setChatId(object.getString("id"));
                                    chat.setDevice_id(object.getString("imei"));
                                    chat.setUser_id(object.getString("user_id"));
                                    chat.setType(object.getString("type"));
                                    chat.setTime(object.getString("create_time"));
                                    if (ChatUtil.TYPE_TEXT.equals(chat.getType())) {
                                        chat.setText(object.getString("contents"));
                                    } else if (ChatUtil.TYPE_AMR.equals(chat.getType())) {
                                        chat.setFileName(object.getString("contents"));
                                    }
                                    chat.setIsSend(false);
                                    //收到消息后，立即存入数据库。
                                    GreenDaoHelper.getInstance().insertChat(chat);
                                    //发送广播，通知有消息到达
                                    Intent intent = new Intent(BroadcastAction.WCHAT_MESSAGE_RECEIVED);
                                    intent.putExtra("chat", chat);
                                    XPTApplication.getInstance().sendBroadcast(intent);
                                } catch (Exception ex) {

                                }
                                break;

                        }

                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

    }

}
