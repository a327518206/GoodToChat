package com.xiaoluogo.goodtochat.other;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xiaoluogo.goodtochat.BmobIMApplication;
import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.activity.ChatActivity;
import com.xiaoluogo.goodtochat.activity.LoginActivity;
import com.xiaoluogo.goodtochat.db.ChatDialog;
import com.xiaoluogo.goodtochat.db.ChatMessage;
import com.xiaoluogo.goodtochat.db.Friend;
import com.xiaoluogo.goodtochat.db.NewFriend;
import com.xiaoluogo.goodtochat.doman.AddFriendMessage;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.utils.BmobUtils;
import com.xiaoluogo.goodtochat.utils.CacheUtils;
import com.xiaoluogo.goodtochat.utils.Constants;
import com.xiaoluogo.goodtochat.utils.L;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 信息页面
 * Created by xiaoluogo on 2017/7/22.
 * Email: angel-lwl@126.com
 */
public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarInfo;
    private TextView tvInfoTitle;
    private ImageView ivInfoHeader;
    private TextView tvInfoNickname;
    private TextView tvInfoUsername;
    private TextView tvInfoEmail;
    private ImageView tvInfoPhotos;
    private RelativeLayout rlInfoBlackMenu;
    private Switch switchInfoBlackMenu;
    private Button btnLogout;
    private Button btnSendMessage;
    private Button btnDeleteFriend;
    private Button btn_add_friend;
    private Button btn_agree_add_friend;
    /**
     * 信息类型
     */
    private int infoPagerType;

    private BmobIMUserInfo info;
    private UserBean mfriend;
    private boolean isMyInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoPagerType = getIntent().getIntExtra(Constants.INFO_PAGER, 0);
        findViews();
        initData();
    }

    /**
     * Find the Views in the layout
     */
    private void findViews() {
        setContentView(R.layout.activity_info);
        toolbarInfo = (Toolbar) findViewById(R.id.toolbar_info);

        setSupportActionBar(toolbarInfo);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_home);
        }

        tvInfoTitle = (TextView) findViewById(R.id.tv_info_title);
        ivInfoHeader = (ImageView) findViewById(R.id.iv_info_header);
        tvInfoNickname = (TextView) findViewById(R.id.tv_info_nickname);
        tvInfoUsername = (TextView) findViewById(R.id.tv_info_username);
        tvInfoEmail = (TextView) findViewById(R.id.tv_info_email);
        tvInfoPhotos = (ImageView) findViewById(R.id.tv_info_photos);
        rlInfoBlackMenu = (RelativeLayout) findViewById(R.id.rl_info_black_menu);
        switchInfoBlackMenu = (Switch) findViewById(R.id.switch_info_black_menu);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnSendMessage = (Button) findViewById(R.id.btn_send_message);
        btnDeleteFriend = (Button) findViewById(R.id.btn_delete_friend);
        btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
        btn_agree_add_friend = (Button) findViewById(R.id.btn_agree_add_friend);

        btnLogout.setOnClickListener(this);
        btnSendMessage.setOnClickListener(this);
        btnDeleteFriend.setOnClickListener(this);
        btn_add_friend.setOnClickListener(this);
        btn_agree_add_friend.setOnClickListener(this);

        ivInfoHeader.setOnClickListener(this);
        tvInfoNickname.setOnClickListener(this);
        tvInfoPhotos.setOnClickListener(this);
    }

    /**
     * Handle button click events
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout://注销
                UserBean.logOut();
                CacheUtils.putBoolean(InfoActivity.this, "isLogin", false);
                startActivity(new Intent(InfoActivity.this, LoginActivity.class));
                BmobIMApplication.destoryAllActivity();
                break;
            case R.id.btn_send_message://发消息
                Exception mE = null;
                try {
                    Intent intent = new Intent(this, ChatActivity.class);
                    //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                    BmobIMUserInfo info = new BmobIMUserInfo(mfriend.getObjectId(), mfriend.getUsername(), mfriend.getHeaderImage());
                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
                    intent.putExtra("friend_user", mfriend);
                    intent.putExtra("c", c);
                    startActivity(intent);
                    finish();
                } catch (NullPointerException e) {
                    mE = e;
                    e.printStackTrace();
                    L.e(e.getMessage() + "userId" + BmobUser.getCurrentUser(UserBean.class).getObjectId());
                } finally {
                    if (mE != null) {
                        Toast.makeText(this, "请重新登录", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_delete_friend://删除好友
                deleteFriend();
                break;
            case R.id.btn_add_friend://添加好友
                L.e(mfriend.getNickName() + "," + mfriend.getObjectId());
                sendAddFriendMessage();
                break;
            case R.id.btn_agree_add_friend://同意添加
                break;
            case R.id.iv_info_header:
                //改变头像或者查看头像
//                UserBean user = BmobUser.getCurrentUser(UserBean.class);
//                ((UserBean)BmobUser.getCurrentUser());
                Intent intent = new Intent(this, PhotoActivity.class);
                intent.putExtra("isMyInfo", isMyInfo);
                if (isMyInfo) {
                    intent.putExtra("photo_title", "我的头像");
                    intent.putExtra("photoUrl", BmobUser.getCurrentUser(UserBean.class).getHeaderImage());
                } else {
                    intent.putExtra("photo_title", mfriend.getNickName() + "的头像");
                    intent.putExtra("photoUrl", mfriend.getHeaderImage());
                }
                startActivity(intent);
                break;
        }
    }

    private void deleteFriend() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除好友");
        builder.setMessage("是否删除好友,删除好友将会聊天内容一并删除");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fId = mfriend.getObjectId();
                DataSupport.deleteAll(Friend.class,"objectId = ?",fId);
                DataSupport.deleteAll(ChatMessage.class, "objectId = ?", fId);
                DataSupport.deleteAll(ChatDialog.class, "objectId = ?", fId);
                UserBean userBean = BmobUser.getCurrentUser(UserBean.class);
                List<String> fs = userBean.getFriends();
                fs.remove(fId);
                userBean.setFriends(fs);
                userBean.update(userBean.getObjectId(), new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(InfoActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        }else{
                        }
                    }

                });
//                List<String> sf = mfriend.getFriends();
//                sf.remove(userBean.getObjectId());
//                mfriend.update(userBean.getObjectId(), new UpdateListener() {
//
//                    @Override
//                    public void done(BmobException e) {
//                        if(e==null){
//                            Toast.makeText(InfoActivity.this, "好友列表中删除成功", Toast.LENGTH_SHORT).show();
//                        }else{
//                        }
//                    }
//
//                });
            }
        });
        builder.show();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    private void initData() {
        BmobIMApplication.addDestoryActivity(InfoActivity.this, "infoActivity");

        UserBean friend = (UserBean) getIntent().getSerializableExtra("newFriendInfo");//来自搜索页面
        NewFriend newFriend = (NewFriend) getIntent().getSerializableExtra("newFriend");//通知栏消息
        Friend now_friend = (Friend) getIntent().getSerializableExtra("now_friend");//来自通讯录
        if (newFriend != null) {
            BmobUtils.queryUsers(newFriend.getUsername(), new FindListener<UserBean>() {
                @Override
                public void done(List<UserBean> list, BmobException e) {
                    if (e == null) {
                        if (e == null) {
                            if (list != null && list.size() > 0) {
                                for (UserBean user : list) {
                                    friendInfo(user);
                                }
                            }
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        } else if (now_friend != null) {
            BmobUtils.queryUsers(now_friend.getUsername(), new FindListener<UserBean>() {
                @Override
                public void done(List<UserBean> list, BmobException e) {
                    if (e == null) {
                        if (e == null) {
                            if (list != null && list.size() > 0) {
                                for (UserBean user : list) {
                                    friendInfo(user);
                                }
                            }
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
        switch (infoPagerType) {
            case Constants.MYSELF_INFO_FROM_LEFTMENU://来自左侧菜单
                isMyInfo = true;
                myselfInfo();
                break;
            case Constants.MYSELF_INFO_FROM_MYHEADER://来自点击我的头像
                isMyInfo = true;
                myselfInfo();
                btnLogout.setVisibility(View.GONE);
                break;
            case Constants.FRIEND_INFO_FROM_SEARCH://来自搜索页面和群成员的点击进入信息页面
                isMyInfo = false;
                friendInfo(friend);
                rlInfoBlackMenu.setVisibility(View.GONE);
                btnSendMessage.setVisibility(View.GONE);
                btnDeleteFriend.setVisibility(View.GONE);
                btn_agree_add_friend.setVisibility(View.GONE);
                break;
            case Constants.FRIEND_INFO_FROM_HEADER://来自好有头像和通讯录的点击进入信息页面
                isMyInfo = false;
                friendInfo(now_friend);
                btn_add_friend.setVisibility(View.GONE);
                btn_agree_add_friend.setVisibility(View.GONE);
                rlInfoBlackMenu.setVisibility(View.VISIBLE);
                break;
            case Constants.FRIEND_INFO_FROM_NOTIFY://来自通知栏的点击进入信息页面
                isMyInfo = false;
                btn_agree_add_friend.setVisibility(View.VISIBLE);
                rlInfoBlackMenu.setVisibility(View.VISIBLE);
                btnSendMessage.setVisibility(View.GONE);
                btnDeleteFriend.setVisibility(View.GONE);
                btn_add_friend.setVisibility(View.GONE);
                btnLogout.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 根据不同的类型进行设置,防止网络延迟造成用户体验不好
     *
     * @param friend
     */
    private void friendInfo(Object friend) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.head)
                .error(R.drawable.default_head);
        if (friend instanceof UserBean) {
            this.mfriend = (UserBean) friend;
            btnLogout.setVisibility(View.GONE);
            String username = mfriend.getUsername();
            String nickname = mfriend.getNickName();
            String email = mfriend.getEmail();
            tvInfoUsername.setText(username);
            tvInfoNickname.setText(nickname);
            tvInfoEmail.setText(email);
            Glide.with(this).load(mfriend.getHeaderImage())
                    .apply(requestOptions)
                    .into(ivInfoHeader);
        } else if (friend instanceof Friend) {
            btnLogout.setVisibility(View.GONE);
            String username = ((Friend) friend).getUsername();
            String nickname = ((Friend) friend).getNickName();
            String email = ((Friend) friend).getEmail();
            tvInfoUsername.setText(username);
            tvInfoNickname.setText(nickname);
            tvInfoEmail.setText(email);
            Glide.with(this).load(((Friend) friend).getHeaderImage())
                    .apply(requestOptions)
                    .into(ivInfoHeader);
        }
    }

    private void myselfInfo() {
        btnSendMessage.setVisibility(View.GONE);
        btnDeleteFriend.setVisibility(View.GONE);
        rlInfoBlackMenu.setVisibility(View.GONE);
        btn_add_friend.setVisibility(View.GONE);
        btn_agree_add_friend.setVisibility(View.GONE);
        tvInfoTitle.setText("我的信息");
        UserBean uesr = BmobUser.getCurrentUser(UserBean.class);
        String username = uesr.getUsername();
        String nickname = uesr.getNickName();
        String email = uesr.getEmail();
        tvInfoUsername.setText(username);
        tvInfoNickname.setText(nickname);
        tvInfoEmail.setText(email);
        Glide.with(this).load(uesr.getHeaderImage()).into(ivInfoHeader);
    }

    /**
     * 发送添加好友的请求
     */
    private void sendAddFriendMessage() {
        info = new BmobIMUserInfo(mfriend.getObjectId(), mfriend.getUsername(), mfriend.getHeaderImage());
        try {
            //启动一个会话，如果isTransient设置为true,则不会创建在本地会话表中创建记录，
            //设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
            BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true, null);
            //这个obtain方法才是真正创建一个管理消息发送的会话
            BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
            AddFriendMessage msg = new AddFriendMessage();
            UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);
            msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
            Map<String, Object> map = new HashMap<>();
            map.put("username", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
            map.put("headerImage", currentUser.getHeaderImage());//发送者的头像
            map.put("nickName", currentUser.getNickName());//发送者的昵称
            map.put("objectId", currentUser.getObjectId());
            msg.setExtraMap(map);
            conversation.sendMessage(msg, new MessageSendListener() {
                @Override
                public void done(BmobIMMessage msg, BmobException e) {
                    if (e == null) {//发送成功
                        Toast.makeText(InfoActivity.this, "好友请求发送成功，等待验证", Toast.LENGTH_SHORT).show();

                    } else {//发送失败
                        Toast.makeText(InfoActivity.this, "发送失败...请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (NullPointerException e) {
            L.e(e.getMessage());
            e.printStackTrace();
        }
    }


}
