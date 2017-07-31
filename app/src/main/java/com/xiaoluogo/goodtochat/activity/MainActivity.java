package com.xiaoluogo.goodtochat.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xiaoluogo.goodtochat.BmobIMApplication;
import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.base.BaseActivity;
import com.xiaoluogo.goodtochat.base.BaseFragment;
import com.xiaoluogo.goodtochat.db.NewFriend;
import com.xiaoluogo.goodtochat.db.NewFriendManager;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.event.RefreshEvent;
import com.xiaoluogo.goodtochat.other.InfoActivity;
import com.xiaoluogo.goodtochat.other.SearchUserActivity;
import com.xiaoluogo.goodtochat.pager.AddressListFragment;
import com.xiaoluogo.goodtochat.pager.MessageListFragment;
import com.xiaoluogo.goodtochat.pager.MySettingFragment;
import com.xiaoluogo.goodtochat.utils.BmobUtils;
import com.xiaoluogo.goodtochat.utils.CacheUtils;
import com.xiaoluogo.goodtochat.utils.Constants;
import com.xiaoluogo.goodtochat.utils.IMMLeaks;
import com.xiaoluogo.goodtochat.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import de.hdodenhof.circleimageview.CircleImageView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends BaseActivity {
    private FrameLayout fl_main_content;
    private RadioGroup rg_bottom_tag;
    private Toolbar toolbar;
    private TextView tv_title;
    private ImageButton ib_add_to;
    private DrawerLayout drawer_layout;
    private NavigationView nav_view;
    private CircleImageView civ_header;
    private TextView nav_nickname;
    private ImageView iv_msg_red_point;
    private ImageView iv_addresslist_red_point;
    private ImageView iv_world_red_point;

    private List<BaseFragment> baseFragments;


    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "cbfb019d8feab863c2183c1e73de3eb0");
        setContentView(R.layout.activity_main);
        initView();

        initData();
        initdb();
    }

    //初始化数据库
    private void initdb() {
        NewFriendManager.getInstance(this);
    }


    private void initView() {
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ib_add_to = (ImageButton) findViewById(R.id.ib_add_to);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        View layout = nav_view.inflateHeaderView(R.layout.nav_header);
        civ_header = (CircleImageView) layout.findViewById(R.id.civ_header);
        nav_nickname = (TextView) layout.findViewById(R.id.nav_nickname);
//        iv_msg_red_point = (ImageView) layout.findViewById(R.id.iv_msg_red_point);
//        iv_addresslist_red_point = (ImageView) layout.findViewById(R.id.iv_addresslist_red_point);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_home);
        }

        nav_view.setNavigationItemSelectedListener(new MyOnNavigationItemSelectedListener());

        ib_add_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "添加好友", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SearchUserActivity.class));
            }
        });
    }

    class MyOnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            drawer_layout.closeDrawers();
            switch (item.getItemId()) {
                case R.id.nav_my_setting:
                    BmobIMApplication.addDestoryActivity(MainActivity.this, "mainActivity");
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    intent.putExtra(Constants.INFO_PAGER, Constants.MYSELF_INFO_FROM_LEFTMENU);
                    startActivity(intent);
                    break;
            }
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer_layout.openDrawer(Gravity.LEFT);
                break;
        }
        return true;
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                default://默认进入消息界面
                    position = 0;
                    tv_title.setText("好有消息");
                    break;
                case R.id.rb_address_list://联系人界面
                    position = 1;
                    tv_title.setText("联系人");
                    break;
                case R.id.rb_friend_of_world://我的设置界面
                    position = 2;
                    tv_title.setText("我");
                    break;
            }
            initTab();
        }
    }

    private void initData() {
        baseFragments = new ArrayList<>();
        baseFragments.add(new MessageListFragment());
        baseFragments.add(new AddressListFragment());
        baseFragments.add(new MySettingFragment());


        //connect server
        final UserBean user = UserBean.getCurrentUser(UserBean.class);
        /**
         * FIXME 连接前先判断uid收为空
         */
        if (!TextUtils.isEmpty(user.getObjectId())) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
//                        Logger.i("connect success");
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        /**
                         * FIXME 连接成功后再进行修改本地用户信息的操作，并查询本地用户信息
                         */
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                        user.getUsername(), user.getHeaderImage()));
                        BmobIMUserInfo bmobIMUserInfo = BmobIM.getInstance().
                                getUserInfo(UserBean.getCurrentUser().getObjectId());
//                        Logger.i(bmobIMUserInfo.getUserId() + "\n" + bmobIMUserInfo.getName());
                    } else {
                        L.e(e.getMessage());
                    }
                }
            });
            //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
//                    toast(status.getMsg());
                    if (status == ConnectionStatus.KICK_ASS) {
                        Toast.makeText(MainActivity.this, "账号在别处登录,如非本人操作,建议更改密码", Toast.LENGTH_SHORT).show();
                        user.logOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
//                    else if (status == ConnectionStatus.DISCONNECT) {
//                        Toast.makeText(MainActivity.this, "登录验证过期...请重新登陆", Toast.LENGTH_SHORT).show();
//                        user.logOut();
//                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                        finish();
//                    }
                    else if (status == ConnectionStatus.NETWORK_UNAVAILABLE) {
                        Toast.makeText(MainActivity.this, "登录失败...请检查网络", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    } else if (status == ConnectionStatus.DISCONNECT) {

                    }
                }
            });
        }
        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());
        try {
            nav_nickname.setText(user.getNickName());
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.head)
                    .error(R.drawable.default_head);
            Glide.with(this).load(user.getHeaderImage())
                    .apply(requestOptions)
                    .into(civ_header);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //默认选中第一个页面
        rg_bottom_tag.check(R.id.rb_message);
    }

    //    /**
//     * 设置碎片
//     */
//    private void setFragment() {
//        //1.获得碎片管理者
//        FragmentManager manager = getSupportFragmentManager();
//        //2.创建事务
//        FragmentTransaction ft = manager.beginTransaction();
//        //3.替换碎片
//        ft.replace(R.id.fl_main_content, new ContentFragment(), CONTENT_FRAGMENT);
//        ft.replace(R.id.fl_main_content, new LeftMenuFragment(), LEFT_MENU);
//        //4.提交事务
//        ft.commit();
//    }
    private void initTab() {
        //1.获得碎片管理者
        FragmentManager manager = getSupportFragmentManager();
        //2.创建事务
        FragmentTransaction ft = manager.beginTransaction();
        //3.替换碎片
        ft.replace(R.id.fl_main_content, getBasePager());
        //4.提交事务
        ft.commit();
    }

    //根据position获取对应的页面
    private Fragment getBasePager() {
        BaseFragment basePager = baseFragments.get(position);
        return basePager;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
        BmobUtils.isFrist = false;
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOfflineMessageEvent(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
//    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
//    public void onRefreshEvent(RefreshEvent event) {
////        log("---主页接收到自定义消息---");
////        checkRedPoint();
//        L.e("---主页接收到自定义消息---");
//    }

    private void checkRedPoint() {
        int count = (int) BmobIM.getInstance().getAllUnReadCount();
//        if (count > 0) {
//            iv_msg_red_point.setVisibility(View.VISIBLE);
//        } else {
//            iv_msg_red_point.setVisibility(View.GONE);
//        }
//        //是否有好友添加的请求
//        if (NewFriendManager.getInstance(this).hasNewFriendInvitation()) {
//            iv_addresslist_red_point.setVisibility(View.VISIBLE);
//        } else {
//            iv_addresslist_red_point.setVisibility(View.GONE);
//        }
    }
}
