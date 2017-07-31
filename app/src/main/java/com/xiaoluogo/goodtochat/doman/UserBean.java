package com.xiaoluogo.goodtochat.doman;

import java.util.List;
import java.util.Objects;

import cn.bmob.v3.datatype.BmobFile;

import cn.bmob.v3.BmobUser;

/**
 * Created by xiaoluogo on 2017/7/20.
 * Email: angel-lwl@126.com
 */
public class UserBean extends BmobUser {

    private String nickName;

    private List<String> friends;

    private int genderNumber;

    private BmobFile headerFile;

    private String headerImage;

    public BmobFile getHeaderFile() {
        return headerFile;
    }

    public void setHeaderFile(BmobFile headerFile) {
        this.headerFile = headerFile;
    }

    public String getHeaderImage() {
//        headerImage = headerFile.getFileUrl();
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
//        headerFile.setUrl(headerImage);
    }

    public int getGenderNumber() {

        return genderNumber;
    }

    public void setGenderNumber(int genderNumber) {
        this.genderNumber = genderNumber;
    }


    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
