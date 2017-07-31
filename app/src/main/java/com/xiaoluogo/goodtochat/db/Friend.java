package com.xiaoluogo.goodtochat.db;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by xiaoluogo on 2017/7/25.
 * Email: angel-lwl@126.com
 */
public class Friend extends DataSupport implements Serializable {
    private int id;
    private String objectId;
    private String username;
    private String mobilePhoneNumber;
    private String nickName;
    private int genderNumber;
    private String headerImage;
    private String email;

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

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getGenderNumber() {
        return genderNumber;
    }

    public void setGenderNumber(int genderNumber) {
        this.genderNumber = genderNumber;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
