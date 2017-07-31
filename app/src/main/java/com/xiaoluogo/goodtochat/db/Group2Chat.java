package com.xiaoluogo.goodtochat.db;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 群聊id
 * Created by xiaoluogo on 2017/7/29.
 * Email: angel-lwl@126.com
 */
public class Group2Chat extends DataSupport {
    private int id;
    private String groupId;
    private List<String> memberId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getMemberId() {
        return memberId;
    }

    public void setMemberId(List<String> memberId) {
        this.memberId = memberId;
    }
}
