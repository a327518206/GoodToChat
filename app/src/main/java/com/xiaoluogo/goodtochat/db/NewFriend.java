package com.xiaoluogo.goodtochat.db;


import org.litepal.crud.DataSupport;

/**
 * Entity mapped to table "newfriend".
 */
public class NewFriend extends DataSupport implements java.io.Serializable {

    private Long id;
    private String objectId;//这个就是objectId
    private String username;
    private String msg;
    private String nickName;
    private String headerImage;
    private int genderNumber;
    private String email;
    private int status;
    private Long time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String name) {
        this.nickName = name;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public int getGenderNumber() {
        return genderNumber;
    }

    public void setGenderNumber(int genderNumber) {
        this.genderNumber = genderNumber;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
