package com.xiaoluogo.goodtochat.db;

import org.litepal.crud.DataSupport;

/**
 * Created by xiaoluogo on 2017/7/29.
 * Email: angel-lwl@126.com
 */
public class ChatDialog extends DataSupport {
    private int id;
    private String objectId;
    private int dialogType;//0代表私聊   1代表群聊
    private String title;//私聊显示好有昵称 群聊显示群名
    private String msgContent;
    private String groupId;
    private int msgType;
    private String msgTime;

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public int getDialogType() {
        return dialogType;
    }

    public void setDialogType(int dialogType) {
        this.dialogType = dialogType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
