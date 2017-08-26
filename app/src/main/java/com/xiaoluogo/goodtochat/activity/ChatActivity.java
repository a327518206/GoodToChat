package com.xiaoluogo.goodtochat.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoluogo.goodtochat.BmobIMApplication;
import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.adapter.ChatAdapter;
import com.xiaoluogo.goodtochat.db.ChatMessage;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.utils.BmobUtils;
import com.xiaoluogo.goodtochat.utils.Constants;
import com.xiaoluogo.goodtochat.utils.L;
import com.xiaoluogo.goodtochat.utils.PhotoAndCamera;
import com.xiaoluogo.goodtochat.utils.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.BmobUser;

/**
 * 聊天页面
 * Created by xiaoluogo on 2017/7/27.
 * Email: angel-lwl@126.com
 */
public class ChatActivity extends AppCompatActivity implements View.OnClickListener, MessageListHandler {

    private static final int SHOW_MORE = 0;
    private static final int HIDE_MORE = 1;
    private Toolbar chatToolbar;
    private TextView tvChatTitle;
    private ImageButton ibTextOrVoice;
    private Button btnSendVoice;
    private EditText etMessage;
    private ImageButton ibMore;
    private ImageButton ibSendMessage;
    private RecyclerView recyclerview_message;
    //    private ScrollView scrollview_chat;
    private LinearLayout ll_more;
    private TextView tv_more_picture;
    private TextView tv_more_camera;
    private TextView tv_more_map;

    private boolean isVoice = false;

    private BmobIMConversation c;

    private LinearLayoutManager manager;
    //传递过来的聊天对象
    private UserBean mfriend;
    private ChatAdapter adapter;
    // 语音有关
    private RelativeLayout layout_record;
    private TextView tv_voice_tips;
    private ImageView iv_record;
    private Drawable[] drawable_Anims;// 话筒动画
    private BmobRecordManager recordManager;
    //更多菜单是否打开
    private boolean moreIsOpen;

    private PhotoAndCamera photoAndCamera;
    //当前的图片路径
    private String currentUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.IS_IN_CHAT_ACTIVITY = true;
//        List<BmobIMConversation> list= BmobIM.getInstance().loadAllConversation();
//        int i = 0;
//        while(i < list.size()){
//            if(list.get(i).equals(c)){
//                c = list.get(i);
//                break;
//            }
//            i++;
//        }
        photoAndCamera = new PhotoAndCamera(this);
        c = BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getIntent().getSerializableExtra("c"));
        mfriend = (UserBean) getIntent().getSerializableExtra("friend_user");
        findViews();
//        scrollview_chat.fullScroll(ScrollView.FOCUS_DOWN);
    }

    /**
     * Find the Views in the layout
     */
    private void findViews() {
        BmobIMApplication.addDestoryActivity(ChatActivity.this, "chatActivity");

        setContentView(R.layout.activity_chat);
        chatToolbar = (Toolbar) findViewById(R.id.chat_toolbar);

        setSupportActionBar(chatToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_home);
        }

        tvChatTitle = (TextView) findViewById(R.id.tv_chat_title);
        ibTextOrVoice = (ImageButton) findViewById(R.id.ib_text_or_voice);
        btnSendVoice = (Button) findViewById(R.id.btn_send_voice);
        etMessage = (EditText) findViewById(R.id.et_message);
        ibMore = (ImageButton) findViewById(R.id.ib_more);
        ibSendMessage = (ImageButton) findViewById(R.id.ib_send_message);
        recyclerview_message = (RecyclerView) findViewById(R.id.recyclerview_message);
        layout_record = (RelativeLayout) findViewById(R.id.layout_record);
        tv_voice_tips = (TextView) findViewById(R.id.tv_voice_tips);
        iv_record = (ImageView) findViewById(R.id.iv_record);
        ll_more = (LinearLayout) findViewById(R.id.ll_more);
        tv_more_picture = (TextView) findViewById(R.id.tv_more_picture);
        tv_more_camera = (TextView) findViewById(R.id.tv_more_camera);
        tv_more_map = (TextView) findViewById(R.id.tv_more_map);
//        scrollview_chat = (ScrollView) findViewById(R.id.scrollview_chat);

        ll_more.setVisibility(View.GONE);
        moreIsOpen = false;

        recyclerview_message.setHasFixedSize(true);
        recyclerview_message.setNestedScrollingEnabled(false);

        ibTextOrVoice.setOnClickListener(this);
        btnSendVoice.setOnClickListener(this);
        ibMore.setOnClickListener(this);
        ibSendMessage.setOnClickListener(this);
        etMessage.setOnClickListener(this);
        tv_more_picture.setOnClickListener(this);
        tv_more_camera.setOnClickListener(this);
        tv_more_map.setOnClickListener(this);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview_message.setLayoutManager(manager);
        List<ChatMessage> datas = DataSupport.where("objectId = ?", mfriend.getObjectId()).find(ChatMessage.class);
        adapter = new ChatAdapter(this, datas, c);
        recyclerview_message.setAdapter(adapter);

        tvChatTitle.setText(mfriend.getNickName());
        //初始化语音布局
        initVoiceView();
    }

    /**
     * 左上角返回按钮点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle button click events
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_text_or_voice:
                //切换语音和文字,并将输入法隐藏
                switchBtn();
                break;
//            case R.id.btn_send_voice:
//                Toast.makeText(this, "发送语音", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.ib_more:
                Toast.makeText(this, "更多", Toast.LENGTH_SHORT).show();
                showOrHideMenu();
                break;
            case R.id.ib_send_message:
                Toast.makeText(this, "发送消息", Toast.LENGTH_SHORT).show();
                sendMessage();
                break;
            case R.id.et_message:
                Toast.makeText(this, "编辑消息", Toast.LENGTH_SHORT).show();
                scrollToBottom();
                moreIsOpen = false;
                ll_more.setVisibility(View.GONE);
                break;
            case R.id.tv_more_picture:
                Toast.makeText(this, "选择图片", Toast.LENGTH_SHORT).show();
                //检查运行时期权限
                if (ContextCompat.checkSelfPermission(ChatActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    photoAndCamera.openAlbum();
                }
                break;
            case R.id.tv_more_camera:
                Toast.makeText(this, "打开相机", Toast.LENGTH_SHORT).show();
                photoAndCamera.openCamera();
                break;
            case R.id.tv_more_map:
                Toast.makeText(this, "发送位置", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 运行时期权限检查返回请求权限结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    photoAndCamera.openAlbum();
                } else {
                    Toast.makeText(this, "请打开相册权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 取到活动返回的数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    currentUri = photoAndCamera.imageUri.getPath();
                }
                break;
            case Constants.CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4版本以上用这个
                        currentUri = photoAndCamera.handleImageOnKitKat(data);
                    } else {
                        currentUri = photoAndCamera.handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
        if(currentUri != null){
            sendImageMessage();
        }
    }

    /**
     * 发送图片消息
     */
    private void sendImageMessage() {
        BmobIMImageMessage image = new BmobIMImageMessage(currentUri);
        c.sendMessage(image,listener);
    }

    /**
     * 显示隐藏菜单
     */
    private void showOrHideMenu() {
        if (moreIsOpen) {
            moreIsOpen = false;
            ll_more.setVisibility(View.GONE);
        } else {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            moreIsOpen = true;
            ll_more.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 发送文本消息
     */
    private void sendMessage() {
        String text = etMessage.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        //可设置额外信息
        Map<String, Object> map = new HashMap<>();
        map.put("level", "1");//随意增加信息
        msg.setExtraMap(map);
        c.sendMessage(msg, listener);
    }

    /**
     * 切换语音和文字,并将输入法隐藏
     */
    private void switchBtn() {
        if (!isVoice) {
            isVoice = true;
            hideSoftInputView();
            ibTextOrVoice.setImageResource(R.drawable.ib_text);
            btnSendVoice.setVisibility(View.VISIBLE);
            ibSendMessage.setVisibility(View.GONE);
            etMessage.setVisibility(View.GONE);
            Toast.makeText(this, "当前为语音状态", Toast.LENGTH_SHORT).show();
        } else {
            isVoice = false;
            ibTextOrVoice.setImageResource(R.drawable.ib_voice);
            ibSendMessage.setVisibility(View.VISIBLE);
            etMessage.setVisibility(View.VISIBLE);
            btnSendVoice.setVisibility(View.GONE);
            Toast.makeText(this, "当前为文本状态", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInputView() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


//    /**
//     * 将消息转换
//     */
//    private ChatMessage msg2ChatMsg(BmobIMMessage msg) {
//        ChatMessage cmsg = new ChatMessage();
//        cmsg.setMessageType(getRealType(msg));
////        String username;
////        if(getRealType(msg) % 2 == 0 ){
////            username = mfriend.getUsername();
////        }else{
////            username = BmobUser.getCurrentUser().getUsername();
////        }
//        cmsg.setUsername(getRealType(msg) % 2 == 0 ? mfriend.getUsername() : BmobUser.getCurrentUser().getUsername());
//        cmsg.setObjectId(mfriend.getObjectId());
//        cmsg.setHeaderImage(msg.getBmobIMUserInfo().getAvatar());
//        cmsg.setMessage(msg.getContent());
//        cmsg.setMessageTime(String.valueOf(msg.getCreateTime()));
//        return cmsg;
//    }
//
//    /**
//     * 保存聊天记录
//     *
//     * @param cmsg
//     */
//    private void saveChatMsg2DB(ChatMessage cmsg) {
//        cmsg.save();
//    }
//
//    private int getRealType(BmobIMMessage msg) {
//        if (msg.getMsgType().equals(BmobIMMessageType.IMAGE.getType())) {
//            return msg.getFromId().equals(BmobUser.getCurrentUser().getObjectId()) ? 3 : 4;
//        } else if (msg.getMsgType().equals(BmobIMMessageType.LOCATION.getType())) {
//            return msg.getFromId().equals(BmobUser.getCurrentUser().getObjectId()) ? 5 : 6;
//        } else if (msg.getMsgType().equals(BmobIMMessageType.VOICE.getType())) {
//            return msg.getFromId().equals(BmobUser.getCurrentUser().getObjectId()) ? 7 : 8;
//        } else if (msg.getMsgType().equals(BmobIMMessageType.TEXT.getType())) {
//            return msg.getFromId().equals(BmobUser.getCurrentUser().getObjectId()) ? 1 : 2;
//        } else if (msg.getMsgType().equals(BmobIMMessageType.VIDEO.getType())) {
//            return msg.getFromId().equals(BmobUser.getCurrentUser().getObjectId()) ? 9 : 10;
//        }
//        return -1;
//    }

    /**
     * 消息发送监听器
     */
    public MessageSendListener listener = new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            L.e("onProgress：" + value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            ChatMessage cmsg = BmobUtils.getInstance().msg2ChatMsg(msg);
//            BmobUtils.getInstance().saveChatMsg2DB(cmsg);
            if (cmsg.getObjectId().equals(BmobUser.getCurrentUser(UserBean.class).getObjectId())) {
                return;
            }
            cmsg.save();
            adapter.addMessage(cmsg);//保存操作
            etMessage.setText("");
            adapter.notifyDataSetChanged();
            scrollToBottom();
            //显示progressBar
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
//            scrollToBottom();
            //完成之后隐藏progressBar
            ChatMessage chatMessage = new ChatMessage();
            if (e == null) {
                chatMessage.setSendOrReceive(2);
                chatMessage.updateAll("objectId = ? and messageTime = ?", msg.getToId(), String.valueOf(msg.getCreateTime()));
            } else {
                //显示重发
//              toast(e.getMessage());
                chatMessage.setSendOrReceive(3);
                chatMessage.updateAll("objectId = ? and messageTime = ?", msg.getToId(), String.valueOf(msg.getCreateTime()));
            }
        }
    };

    private void scrollToBottom() {
        manager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
//        scrollview_chat.fullScroll(ScrollView.FOCUS_DOWN);
    }


    /**
     * 添加消息到聊天界面中
     *
     * @param event
     */
    private void addMessage2Chat(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        if (c != null && event != null && c.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            ChatMessage cmsg = BmobUtils.getInstance().msg2ChatMsg(msg);//保存操作
            if (cmsg.getObjectId().equals(BmobUser.getCurrentUser(UserBean.class).getObjectId())) {
                return;
            }
            cmsg.setSendOrReceive(2);
//            boolean isOnce = BmobUtils.getInstance().saveChatMsg2DB(cmsg);
            if (adapter.findPosition(cmsg.getId()) < 0 ) {//如果未添加到界面中
                adapter.addMessage(cmsg);
                //更新该会话下面的已读状态
                c.updateReceiveStatus(msg);
            }
            scrollToBottom();
        } else {
            L.e("不是与当前聊天对象的消息");
        }
    }

    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0) {
            int size = cache.size();
            for (int i = 0; i < size; i++) {
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i = 0; i < list.size(); i++) {
            addMessage2Chat(list.get(i));
        }
    }

    /**
     * 接收到聊天消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(MessageEvent event) {
        addMessage2Chat(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOfflineMessage(OfflineMessageEvent event) {
        Map<String, List<MessageEvent>> map = event.getEventMap();
        if (map != null && map.size() > 0) {
            //只获取当前聊天对象的离线消息
            List<MessageEvent> list = map.get(c.getConversationId());
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Collections.sort(list, new Comparator<MessageEvent>() {
                        @Override
                        public int compare(MessageEvent o1, MessageEvent o2) {
                            if (Double.valueOf(o1.getConversation().getUpdateTime()) > Double.valueOf(o2.getConversation().getUpdateTime())) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    addMessage2Chat(list.get(i));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        Constants.IS_IN_CHAT_ACTIVITY = false;
        ChatMessage chatMessage = DataSupport.where("objectId = ?", mfriend.getObjectId()).findLast(ChatMessage.class);
        BmobUtils.getInstance().saveDialog(chatMessage);
        //清理资源
        if (recordManager != null) {
            recordManager.clear();
        }
        //更新此会话的所有消息为已读状态
        if (c != null) {
            c.updateLocalCache();
        }
        hideSoftInputView();
        //退出发送广播
        Intent intent = new Intent("com.xiaoluogo.goodtochat.REFRESH_MESSAGE");
        sendBroadcast(intent);

        super.onDestroy();
//        if (chatMessage != null) {
//            ChatDialog dialog = (ChatDialog) DataSupport.where("objectId = ? and dialogType = ?", chatMessage.getObjectId(), "0").find(ChatDialog.class);
//            if (dialog == null) {
//                dialog = new ChatDialog();
//                dialog.setObjectId(chatMessage.getObjectId());
//                dialog.setDialogType(0);
//                dialog.setMsgType(chatMessage.getMessageType());
//                dialog.setMsgContent(chatMessage.getMessage());
//                dialog.setMsgTime(chatMessage.getMessageTime());
//                dialog.save();
//            }else{
//                dialog.setMsgType(chatMessage.getMessageType());
//                dialog.setMsgContent(chatMessage.getMessage());
//                dialog.setMsgTime(chatMessage.getMessageTime());
//                dialog.updateAll("objectId = ? and dialogType = ?", mfriend.getObjectId(), "0");
//            }
//        }
    }

    /**
     * 初始化语音布局
     *
     * @param
     * @return void
     */
    private void initVoiceView() {
        btnSendVoice.setOnTouchListener(new VoiceTouchListener());
        initVoiceAnimRes();
        initRecordManager();
    }

    /**
     * 初始化语音动画资源
     *
     * @param
     * @return void
     * @Title: initVoiceAnimRes
     */
    private void initVoiceAnimRes() {
        drawable_Anims = new Drawable[]{
                getResources().getDrawable(R.drawable.chat_icon_voice2),
                getResources().getDrawable(R.drawable.chat_icon_voice3),
                getResources().getDrawable(R.drawable.chat_icon_voice4),
                getResources().getDrawable(R.drawable.chat_icon_voice5),
                getResources().getDrawable(R.drawable.chat_icon_voice6)};
    }

    private void initRecordManager() {
        // 语音相关管理器
        recordManager = BmobRecordManager.getInstance(this);
        // 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

            @Override
            public void onVolumnChanged(int value) {
                iv_record.setImageDrawable(drawable_Anims[value]);
            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                L.e("voice", "已录音长度:" + recordTime);
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
                    // 需要重置按钮
                    btnSendVoice.setPressed(false);
                    btnSendVoice.setClickable(false);
                    // 取消录音框
                    layout_record.setVisibility(View.INVISIBLE);
                    // 发送语音消息
                    sendVoiceMessage(localPath, recordTime);
                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            btnSendVoice.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    /**
     * 长按说话
     *
     * @author smile
     * @date 2014-7-1 下午6:10:16
     */
    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!Utils.checkSdCard()) {
                        Toast.makeText(ChatActivity.this, "发送语音需要sdcard支持！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        layout_record.setVisibility(View.VISIBLE);
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        // 开始录音
                        recordManager.startRecording(c.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        tv_voice_tips.setTextColor(Color.RED);
                    } else {
                        tv_voice_tips.setText(getString(R.string.voice_up_tips));
                        tv_voice_tips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    layout_record.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {// 放弃录音
                            recordManager.cancelRecording();
                            L.e("voice", "放弃发送语音");
                        } else {
                            int recordTime = recordManager.stopRecording();
                            if (recordTime > 1) {
                                // 发送语音文件
                                sendVoiceMessage(recordManager.getRecordFilePath(c.getConversationId()), recordTime);
                            } else {// 录音时间过短，则提示录音过短的提示
                                layout_record.setVisibility(View.GONE);
//                                showShortToast().show();
                                Toast.makeText(ChatActivity.this, "时间过短...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    /**
     * 发送语音消息
     *
     * @param local
     * @param length
     * @return void
     * @Title: sendVoiceMessage
     */
    private void sendVoiceMessage(String local, int length) {
        BmobIMAudioMessage audio = new BmobIMAudioMessage(local);
        //可设置额外信息-开发者设置的额外信息，需要开发者自己从extra中取出来
//        Map<String,Object> map =new HashMap<>();
//        map.put("from", "优酷");
//        audio.setExtraMap(map);
        //设置语音文件时长：可选
        audio.setDuration(length);
        c.sendMessage(audio, listener);
    }
}
