package com.xiaoluogo.goodtochat.doman;

import android.text.TextUtils;

import org.json.JSONObject;

import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;

import com.xiaoluogo.goodtochat.db.NewFriend;
import com.xiaoluogo.goodtochat.utils.Constants;
import com.xiaoluogo.goodtochat.utils.L;

/**
 * 添加好友请求
 */
public class AddFriendMessage extends BmobIMExtraMessage{

    public AddFriendMessage(){}

    /**
     * 将IMMessage转成NewFriend
     * @param msg 消息
     * @return
     */
    public static NewFriend convert(BmobIMMessage msg){
        NewFriend add =new NewFriend();
        String content = msg.getContent();
        add.setMsg(content);
        add.setTime(msg.getCreateTime());
        add.setStatus(Constants.STATUS_VERIFY_NONE);
        try {
            String extra = msg.getExtra();
            if(!TextUtils.isEmpty(extra)){
                JSONObject json =new JSONObject(extra);
                String username = json.optString("username");
                add.setUsername(username);
                String nickName = json.optString("nickName");
                add.setNickName(nickName);
                String headerImage = json.optString("headerImage");
                add.setHeaderImage(headerImage);
                add.setObjectId(json.optString("objectId"));
            }else{
                L.e("AddFriendMessage的extra为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return add;
    }


    @Override
    public String getMsgType() {
        return "add";
    }

    @Override
    public boolean isTransient() {
        //设置为true,表明为暂态消息，那么这条消息并不会保存到本地db中，SDK只负责发送出去
        //设置为false,则会保存到指定会话的数据库中
        return true;
    }

}
