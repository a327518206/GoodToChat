package com.xiaoluogo.goodtochat.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.activity.MainActivity;
import com.xiaoluogo.goodtochat.activity.NewFriendsActivity;
import com.xiaoluogo.goodtochat.db.ChatMessage;
import com.xiaoluogo.goodtochat.db.NewFriend;
import com.xiaoluogo.goodtochat.db.NewFriendManager;
import com.xiaoluogo.goodtochat.doman.AddFriendMessage;
import com.xiaoluogo.goodtochat.doman.AgreeAddFriendMessage;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.event.ChatEvent;
import com.xiaoluogo.goodtochat.event.RefreshEvent;
import com.xiaoluogo.goodtochat.other.InfoActivity;
import com.xiaoluogo.goodtochat.pager.MessageListFragment;
import com.xiaoluogo.goodtochat.utils.BmobUtils;
import com.xiaoluogo.goodtochat.utils.Constants;
import com.xiaoluogo.goodtochat.utils.L;
import com.xiaoluogo.goodtochat.utils.i.QueryUserListener;
import com.xiaoluogo.goodtochat.utils.i.UpdateCacheListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 自定义消息接收器处理在线消息和离线消息
 * Created by xiaoluogo on 2017/7/21.
 * Email: angel-lwl@126.com
 */
public class BmobMessageHandler extends BmobIMMessageHandler {

    private Context context;

    public BmobMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //当接收到服务器发来的消息时，此方法被调用
//        Logger.i(event.getConversation().getConversationTitle() + "," + event.getMessage().getMsgType() + "," + event.getMessage().getContent());
        excuteMessage(event);
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
        Map<String, List<MessageEvent>> map = event.getEventMap();
//        Logger.i("离线消息属于" + map.size() + "个用户");
        //挨个检测下离线消息所属的用户的信息是否需要更新
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                excuteMessage(list.get(i));
            }
        }
    }

    /**
     * 处理消息
     *
     * @param event
     */
    private void excuteMessage(final MessageEvent event) {
        //检测用户信息是否需要更新
        BmobUtils.getInstance().updateUserInfo(event, new UpdateCacheListener() {
            @Override
            public void done(BmobException e) {
                BmobIMMessage msg = event.getMessage();
                if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0) {//用户自定义的消息类型，其类型值均为0
                    processCustomMessage(msg, event.getFromUserInfo());
                } else {//SDK内部内部支持的消息类型
//                    if (BmobNotificationManager.getInstance(context).isShowNotification()) {//如果需要显示通知栏，SDK提供以下两种显示方式：
//                        Intent pendingIntent = new Intent(context, MainActivity.class);
//                        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        //1、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
////                        BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);
//                        //2、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
//                        BmobIMUserInfo info = event.getFromUserInfo();
//                        //这里可以是应用图标，也可以将聊天头像转成bitmap
//                        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//                        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
//                                info.getName(), msg.getContent(), "您有一条新消息", pendingIntent);
//                        break;
////                    } else {//直接发送消息事件
//////                        Logger.i("当前处于应用内，发送event");
//                    }else
                    if (!Constants.IS_IN_CHAT_ACTIVITY) {
                        Intent pendingIntent = new Intent(context, MainActivity.class);
                        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //1、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
//                        BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);
                        //2、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
                        BmobIMUserInfo info = event.getFromUserInfo();
                        //这里可以是应用图标，也可以将聊天头像转成bitmap
                        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
                                info.getName(), msg.getContent(), "您有一条新消息", pendingIntent);

                    }
                    ChatMessage message = BmobUtils.getInstance().msg2ChatMsg(msg);
                    message.setSendOrReceive(2);
                    BmobUtils.getInstance().saveChatMsg2DB(message);
                    BmobUtils.getInstance().saveDialog(message);
                    EventBus.getDefault().post(event);
                    EventBus.getDefault().post(new RefreshEvent("来消息了"));
//                    MyMessageReceiver receiver = new MyMessageReceiver();
                    Intent intent = new Intent("com.xiaoluogo.goodtochat.REFRESH_MESSAGE");
//                    intentFilter.addAction();
                    context.sendBroadcast(intent);
                }
            }
        });
    }

    /**
     * 处理自定义消息类型
     *
     * @param msg
     */
    private void processCustomMessage(BmobIMMessage msg, BmobIMUserInfo info) {
        //自行处理自定义消息类型
//        Logger.i(msg.getMsgType() + "," + msg.getContent() + "," + msg.getExtra());
        String type = msg.getMsgType();
        //发送页面刷新的广播
//        EventBus.getDefault().post(new RefreshEvent());
        //处理消息
        if (type.equals("add")) {//接收到的添加好友的请求
            NewFriend friend = AddFriendMessage.convert(msg);
            //本地好友请求表做下校验，本地没有的才允许显示通知栏--有可能离线消息会有些重复
            long id = NewFriendManager.getInstance(context).insertOrUpdateNewFriend(friend);
            if (id > 0) {
                showAddNotify(friend);
            }
        } else if (type.equals("agree")) {//接收到的对方同意添加自己为好友,此时需要做的事情：1、添加对方为好友，2、显示通知
            AgreeAddFriendMessage agree = AgreeAddFriendMessage.convert(msg);
            addFriend(agree.getFromId());//添加消息的发送方为好友
            //这里应该也需要做下校验--来检测下是否已经同意过该好友请求，我这里省略了
            showAgreeNotify(info, agree);
        } else {
            Toast.makeText(context, "接收到的自定义消息：" + msg.getMsgType() + "," + msg.getContent() + "," + msg.getExtra(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示对方添加自己为好友的通知
     *
     * @param friend
     */
    private void showAddNotify(NewFriend friend) {
        Intent pendingIntent = new Intent(context, NewFriendsActivity.class);
        pendingIntent.putExtra(Constants.INFO_PAGER, Constants.FRIEND_INFO_FROM_NOTIFY);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        pendingIntent.putExtra("newFriend", friend);
        //这里可以是应用图标，也可以将聊天头像转成bitmap
        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
                friend.getNickName(), friend.getMsg(), friend.getNickName() + "请求添加你为朋友", pendingIntent);
    }

    /**
     * 显示对方同意添加自己为好友的通知
     *
     * @param info
     * @param agree
     */
    private void showAgreeNotify(BmobIMUserInfo info, AgreeAddFriendMessage agree) {
        Intent pendingIntent = new Intent(context, MainActivity.class);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        BmobNotificationManager.getInstance(context).showNotification(largetIcon, info.getName(), agree.getMsg(), agree.getMsg(), pendingIntent);
    }

    /**
     * 添加对方为自己的好友
     *
     * @param uid
     */
    private void addFriend(String uid) {
        BmobUtils.getInstance().queryUserInfo(uid, new QueryUserListener() {
            @Override
            public void done(final UserBean s, BmobException e) {
                if (e == null) {
                    BmobUtils.getInstance().agreeAddFriend(s, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                L.e("更新数据成功");
                                BmobUtils.getInstance().saveFriend2db(s);
                            } else {
                                L.e(e.getMessage());
                            }
                        }
                    });
                }
            }
        });
//        UserBean user = new UserBean();
//        user.setObjectId(uid);
    }
}