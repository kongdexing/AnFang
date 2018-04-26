package com.xptschool.parent.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BeanWChat {

    @Id
    private String chatId;
    private String type;    //1文字，2语音
    private String text;
    private String fileName;
    private String seconds;  //语音时长
    private String time;  //发送时间

    @Generated(hash = 625865190)
    public BeanWChat(String chatId, String type, String text, String fileName,
            String seconds, String time) {
        this.chatId = chatId;
        this.type = type;
        this.text = text;
        this.fileName = fileName;
        this.seconds = seconds;
        this.time = time;
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
        return seconds;
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
}
