package com.xptschool.parent.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

@Entity
public class BeanWChat implements Serializable {

    @Id
    private String chatId;      //聊天标识
    private String user_id;     //用户 id
    private String device_id;    //设备 id
    private String type;        //1文字，2语音
    private String text;        //文字聊天
    private String fileName;    //语音文件路径
    private String seconds;     //语音时长
    private String time;        //发送时间
    private boolean isSend = false; //发出|接收（发出消息显示在界面右侧，接收消息显示在界面左侧）

    @Generated(hash = 2120968242)
    public BeanWChat(String chatId, String user_id, String device_id, String type,
                     String text, String fileName, String seconds, String time,
                     boolean isSend) {
        this.chatId = chatId;
        this.user_id = user_id;
        this.device_id = device_id;
        this.type = type;
        this.text = text;
        this.fileName = fileName;
        this.seconds = seconds;
        this.time = time;
        this.isSend = isSend;
    }

    @Generated(hash = 1695636561)
    public BeanWChat() {
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSeconds() {
        return seconds == null ? "5" : "5";
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean getIsSend() {
        return this.isSend;
    }

    public void setIsSend(boolean isSend) {
        this.isSend = isSend;
    }
}
