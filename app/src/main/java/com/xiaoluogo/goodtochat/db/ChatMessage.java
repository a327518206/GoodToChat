package com.xiaoluogo.goodtochat.db;

import org.litepal.crud.DataSupport;

/**
 * Created by xiaoluogo on 2017/7/25.
 * Email: angel-lwl@126.com
 */
public class ChatMessage extends DataSupport {
    private int id;
    private String objectId;
    private String username;
    private int sendOrReceive;//发的是1,收的是2
    private int messageType;
//    private String textMessage;//文本消息1
//    private String audioMessage;//语音消息2
//    private String imageMessage;//图片消息3
//    private String videoMessage;//视频消息4
//    private String mapMessage;//位置消息5
    private String message;
    private String messageTime;
    private String headerImage;
    private int privateOrGroup;


    private int isOnce;
    public int getIsOnce() {
        return isOnce;
    }

    public void setIsOnce(int isOnce) {
        this.isOnce = isOnce;
    }
    private String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public int getPrivateOrGroup() {
        return privateOrGroup;
    }

    public void setPrivateOrGroup(int privateOrGroup) {
        this.privateOrGroup = privateOrGroup;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSendOrReceive() {
        return sendOrReceive;
    }

    public void setSendOrReceive(int sendOrReceive) {
        this.sendOrReceive = sendOrReceive;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

}
