package com.xiaoluogo.goodtochat.utils;

import android.content.Context;

import com.xiaoluogo.goodtochat.db.ChatDialog;
import com.xiaoluogo.goodtochat.db.ChatMessage;
import com.xiaoluogo.goodtochat.db.Friend;
import com.xiaoluogo.goodtochat.db.NewFriend;
import com.xiaoluogo.goodtochat.db.NewFriendManager;
import com.xiaoluogo.goodtochat.doman.AgreeAddFriendMessage;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.utils.i.QueryUserListener;
import com.xiaoluogo.goodtochat.utils.i.UpdateCacheListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobDownloadManager;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.FileDownloadListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by xiaoluogo on 2017/7/23.
 * Email: angel-lwl@126.com
 */
public class BmobUtils extends BaseBmobUtils {

    private static BmobUtils ourInstance = new BmobUtils();
    public static boolean isFrist = false;

    public static BmobUtils getInstance() {
        return ourInstance;
    }

    private BmobUtils() {
    }

    /**
     * 查找好友
     *
     * @param username
     * @param listener
     */
    public static void queryUsers(String username, final FindListener<UserBean> listener) {
        BmobQuery<UserBean> query = new BmobQuery<UserBean>("_User");
        UserBean user = BmobUser.getCurrentUser(UserBean.class);
        query.addWhereNotEqualTo("username", user.getUsername());
        query.addWhereEqualTo("username", username);
//        query.getObjectByTable("5e14eafda1", new QueryListener<JSONObject>() {
//            @Override
//            public void done(JSONObject jsonObject, BmobException e) {
//                findUser = (UserBean) jsonObject.opt("_User");
//            }
//        });
        query.findObjects(new FindListener<UserBean>() {
            @Override
            public void done(List<UserBean> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        L.e("共" + list.size() + "条数据");
                        listener.done(list, e);
                    } else {
                        listener.done(list, new BmobException(CODE_NULL, "查无此人"));
                    }
                } else {
                    listener.done(list, e);
                }
            }
        });

    }

    /**
     * 更新用户资料和会话资料
     *
     * @param event
     * @param listener
     */
    public void updateUserInfo(MessageEvent event, final UpdateCacheListener listener) {
        final BmobIMConversation conversation = event.getConversation();
        final BmobIMUserInfo info = event.getFromUserInfo();
        final BmobIMMessage msg = event.getMessage();
        String username = info.getName();
        String title = conversation.getConversationTitle();
//        Logger.i("" + username + "," + title);
        //sdk内部，将新会话的会话标题用objectId表示，因此需要比对用户名和会话标题--单聊，后续会根据会话类型进行判断
        if (!username.equals(title)) {
            BmobUtils.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
                @Override
                public void done(UserBean s, BmobException e) {
                    if (e == null) {
                        String name = s.getUsername();
                        String headerImage = s.getHeaderImage();
//                        Logger.i("query success：" + name + "," + avatar);
                        conversation.setConversationIcon(headerImage);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(headerImage);
                        //更新用户资料
                        BmobIM.getInstance().updateUserInfo(info);
                        //更新会话资料-如果消息是暂态消息，则不更新会话资料
                        if (!msg.isTransient()) {
                            BmobIM.getInstance().updateConversation(conversation);
                        }
                    } else {
//                        Logger.e(e);
                        e.printStackTrace();
                    }
                    listener.done(null);
                }
            });
        } else {
            listener.done(null);
        }
    }

    /**
     * 同意添加好友：1、发送同意添加的请求，2、添加对方到自己的好友列表中
     */
    public void agreeAddFriend(UserBean friend, UpdateListener listener) {
        boolean isExist = false;
        UserBean user = BmobUser.getCurrentUser(UserBean.class);
        List<String> userFriends = user.getFriends();
        if (userFriends == null) {
            userFriends = new ArrayList<>();
        } else {
            for (int i = 0; i < userFriends.size(); i++) {
                if (friend.getObjectId().equals(userFriends.get(i))) {
                    isExist = true;
                }
            }
        }
        if (!isExist) {
            userFriends.add(friend.getObjectId());
            user.setFriends(userFriends);
            user.update(listener);
        }
    }

    /**
     * 将好友信息保存到本地数据库
     *
     * @param friend
     * @return
     */
    public void saveFriend2db(UserBean friend) {
        List<Friend> friends = DataSupport.findAll(Friend.class);
        boolean isExist = false;
        for (int i = 0; i < friends.size(); i++) {
            if (friend.getObjectId().equals(friends.get(i).getObjectId())) {
                isExist = true;
            }
        }
        if (!isExist) {
            Friend f = new Friend();
            f.setObjectId(friend.getObjectId());
            f.setUsername(friend.getUsername());
            f.setNickName(friend.getNickName());
            f.setEmail(friend.getEmail());
            f.setGenderNumber(friend.getGenderNumber());
            f.setMobilePhoneNumber(friend.getMobilePhoneNumber());
            f.setHeaderImage(friend.getHeaderImage());
            f.save();
        }
    }

    /**
     * 将新朋友添加到本地数据库,用于显示在新的朋友列表里
     *
     * @param friend
     */
    public void addNewFriend2DB(UserBean friend) {
        NewFriend nf = new NewFriend();
        nf.setObjectId(friend.getObjectId());
        nf.setUsername(friend.getUsername());
        nf.setNickName(friend.getNickName());
        nf.setEmail(friend.getEmail());
        nf.setGenderNumber(friend.getGenderNumber());
        nf.setHeaderImage(friend.getHeaderImage());
        nf.setTime(System.currentTimeMillis());
        nf.save();
    }

    /**
     * 查询好友
     */
    public List<Friend> queryFriends() {
//        UserBean user = BmobUser.getCurrentUser(UserBean.class);
//        List<String> listFromNet = user.getFriends();
        isFrist = true;
        return DataSupport.findAll(Friend.class);
//        if (listFromNet != null) {
//            return null;
//        }
//        //网络数据大小和本地相同时
//        if (listFromNet.size() == friends.size()) {
//            return friends;
//        } else {
//            DataSupport.deleteAll(Friend.class);
//            BmobQuery<UserBean> query = new BmobQuery<UserBean>("_User");
//            String friendObjectId;
//            for (int i = 0; i < listFromNet.size(); i++) {
//                friendObjectId = listFromNet.get(i);
//                query.getObject(friendObjectId, new QueryListener<UserBean>() {
//                    @Override
//                    public void done(UserBean userBean, BmobException e) {
//                        if (e == null) {
//                            saveFriend2db(userBean);
//                        } else {
//                            L.e("好友信息保存到本地失败");
//                        }
//                    }
//                });
//            }
//            listener.done(DataSupport.findAll(Friend.class), new Exception("查询出错"));
//            return null;
//        }
    }

    /**
     * 查询用户信息
     *
     * @param objectId
     * @param listener
     */
    public void queryUserInfo(String objectId, final QueryUserListener listener) {
        BmobQuery<UserBean> query = new BmobQuery<UserBean>("_User");
        query.getObject(objectId, new QueryListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                if (e == null) {
                    listener.done(userBean, null);
                } else {
                    listener.done(null, e);
                }
            }
        });
    }


    /**
     * 添加到好友表中...
     *
     * @param add
     * @param listener
     */
    public void agreeAdd(final Context context, final NewFriend add, final SaveListener<Object> listener) {
        UserBean user = new UserBean();
        user.setObjectId(add.getObjectId());
        agreeAddFriend(user, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    sendAgreeAddFriendMessage(context, add, listener);
                } else {
                    L.e(e.getMessage());
                    listener.done(null, e);
                }
            }
        });
    }

    /**
     * 发送同意添加好友的请求
     */
    public void sendAgreeAddFriendMessage(final Context context, final NewFriend add, final SaveListener<Object> listener) {
        BmobIMUserInfo info = new BmobIMUserInfo(add.getObjectId(), add.getUsername(), add.getHeaderImage());
        //如果为true,则表明为暂态会话，也就是说该会话仅执行发送消息的操作，不会保存会话和消息到本地数据库中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true, null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        //而AgreeAddFriendMessage的isTransient设置为false，表明我希望在对方的会话数据库中保存该类型的消息
        AgreeAddFriendMessage msg = new AgreeAddFriendMessage();
        final UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);
        msg.setContent("我通过了你的好友验证请求，我们可以开始 聊天了!");//---这句话是直接存储到对方的消息表中的
        Map<String, Object> map = new HashMap<>();
        map.put("msg", currentUser.getUsername() + "同意添加你为好友");//显示在通知栏上面的内容
        map.put("objectId", add.getObjectId());//发送者的uid-方便请求添加的发送方找到该条添加好友的请求
        map.put("time", add.getTime());//添加好友的请求时间
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    //修改本地的好友请求记录
                    NewFriendManager.getInstance(context).updateNewFriend(add, Constants.STATUS_VERIFIED);
                    listener.done(msg, e);
                } else {//发送失败
                    listener.done(msg, e);
                }
            }
        });
    }

    /**
     * 将消息转换
     */
    public ChatMessage msg2ChatMsg(BmobIMMessage msg) {
        ChatMessage cmsg = new ChatMessage();
        cmsg.setMessageType(getRealType(msg));
        cmsg.setHeaderImage(msg.getBmobIMUserInfo().getAvatar());
        cmsg.setMessageTime(String.valueOf(msg.getCreateTime()));
        int type = getRealType(msg);
        cmsg.setObjectId(type % 2 == 0 ? msg.getFromId() : msg.getToId());


        //若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
        BmobDownloadManager downloadTask = new BmobDownloadManager(getContext(), msg, new FileDownloadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void done(BmobException e) {

            }
        });

        if (type == 1 || type == 2) {
            cmsg.setMessage(msg.getContent());
        } else if (type == 3 || type == 4) {
            if (type == 3) {
                cmsg.setMessage(BmobIMImageMessage.buildFromDB(true, msg).getLocalPath());
            } else {
                cmsg.setMessage(BmobIMImageMessage.buildFromDB(true, msg).getRemoteUrl());
            }
        } else if (type == 7 || type == 8) {
            if (type == 7) {
                cmsg.setMessage(msg.getContent().split("&")[0]);
            } else {
                downloadTask.execute(msg.getContent());
                cmsg.setMessage(BmobDownloadManager.getDownLoadFilePath(msg));
            }
            cmsg.setDuration(BmobIMAudioMessage.buildFromDB(true, msg).getDuration() + "\''");
        }
        return cmsg;
//        String username;
//        if(getRealType(msg) % 2 == 0 ){
//            username = mfriend.getUsername();
//        }else{
//            username = BmobUser.getCurrentUser().getUsername();
//        }
//        cmsg.setUsername(getRealType(msg) % 2 == 0 ? mfriend.getUsername() : BmobUser.getCurrentUser().getUsername());
    }

    /**
     * 保存聊天记录
     *
     * @param cmsg
     */
    public boolean saveChatMsg2DB(ChatMessage cmsg) {
        ChatMessage mmm = DataSupport.findLast(ChatMessage.class);
        if(mmm.getMessageType() == cmsg.getMessageType() &&
                mmm.getMessage().equals(cmsg.getMessage())
                && mmm.getPrivateOrGroup() == cmsg.getPrivateOrGroup()
                && mmm.getMessageTime().equals(cmsg.getMessageTime())
                && mmm.getSendOrReceive() == cmsg.getSendOrReceive()
                && mmm.getObjectId().equals(cmsg.getObjectId())){
            return false;
        }
        cmsg.save();
        return true;
    }

    private int getRealType(BmobIMMessage msg) {
        if (msg.getMsgType().equals(BmobIMMessageType.IMAGE.getType())) {
            return msg.getFromId().equals(BmobUser.getCurrentUser().getObjectId()) ? 3 : 4;
        } else if (msg.getMsgType().equals(BmobIMMessageType.LOCATION.getType())) {
            return msg.getFromId().equals(BmobUser.getCurrentUser().getObjectId()) ? 5 : 6;
        } else if (msg.getMsgType().equals(BmobIMMessageType.VOICE.getType())) {
            return msg.getFromId().equals(BmobUser.getCurrentUser().getObjectId()) ? 7 : 8;
        } else if (msg.getMsgType().equals(BmobIMMessageType.TEXT.getType())) {
            return msg.getFromId().equals(BmobUser.getCurrentUser().getObjectId()) ? 1 : 2;
        } else if (msg.getMsgType().equals(BmobIMMessageType.VIDEO.getType())) {
            return msg.getFromId().equals(BmobUser.getCurrentUser().getObjectId()) ? 9 : 10;
        }
        return -1;
    }

    /**
     * 保存会话
     *
     * @param chatMessage
     */
    public void saveDialog(ChatMessage chatMessage) {
        if (chatMessage != null) {
            ChatDialog dialog = DataSupport.where("objectId = ? and dialogType = ?", chatMessage.getObjectId(), "0").findLast(ChatDialog.class);
            if (dialog == null) {
                dialog = new ChatDialog();
                dialog.setObjectId(chatMessage.getObjectId());
                dialog.setDialogType(0);
                dialog.setMsgType(chatMessage.getMessageType());
                dialog.setMsgContent(chatMessage.getMessage());
                dialog.setMsgTime(chatMessage.getMessageTime());
                dialog.save();
            } else {
                dialog.setMsgType(chatMessage.getMessageType());
                dialog.setMsgContent(chatMessage.getMessage());
                dialog.setMsgTime(chatMessage.getMessageTime());
                dialog.updateAll("objectId = ? and dialogType = ?", chatMessage.getObjectId(), "0");
            }
        }
    }


}
