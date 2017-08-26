package com.xiaoluogo.goodtochat.pager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.activity.ChatActivity;
import com.xiaoluogo.goodtochat.adapter.MessageListPagerAdapter;
import com.xiaoluogo.goodtochat.adapter.base.OnRecyclerViewListener;
import com.xiaoluogo.goodtochat.base.BaseFragment;
import com.xiaoluogo.goodtochat.db.ChatDialog;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.event.RefreshEvent;
import com.xiaoluogo.goodtochat.utils.BmobUtils;
import com.xiaoluogo.goodtochat.utils.L;
import com.xiaoluogo.goodtochat.utils.i.QueryUserListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;

import java.util.List;


/**
 * 消息列表页面
 * Created by xiaoluogo on 2017/7/19.
 * Email: angel-lwl@126.com
 */
public class MessageListFragment extends BaseFragment {

    private RecyclerView rv_message_list;
    private LinearLayout ll_no_message;
    private MessageListPagerAdapter adapter;
    private List<ChatDialog> dialogs;

    private MyReceiver myReceiver;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.message_list, null);
        L.e("消息列表页面====初始化视图完成");
        rv_message_list = (RecyclerView) view.findViewById(R.id.rv_message_list);
        ll_no_message = (LinearLayout) view.findViewById(R.id.ll_no_message);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        L.e("消息列表页面====初始化数据完成");
        //数据库中找数据
        dialogs = DataSupport.findAll(ChatDialog.class);
        for(int i = 0; i < dialogs.size(); i++){

        }
        //设置适配器
        adapter = new MessageListPagerAdapter(context, dialogs);
        rv_message_list.setAdapter(adapter);
        rv_message_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //设置recyclerView的分割线
        rv_message_list.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        hideText();

        adapter.setOnItemClickListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(Object o) {
                BmobUtils.getInstance().queryUserInfo(((ChatDialog) o).getObjectId(), new QueryUserListener() {
                    @Override
                    public void done(UserBean s, BmobException e) {
                        Exception mE = null;
                        try {
                            Intent intent = new Intent(context, ChatActivity.class);
                            BmobIMUserInfo info = new BmobIMUserInfo(s.getObjectId(), s.getUsername(), s.getHeaderImage());
                            BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
                            intent.putExtra("friend_user", s);
                            intent.putExtra("c", c);
                            startActivity(intent);
                        } catch (NullPointerException ne) {
                            mE = ne;
                            e.printStackTrace();
                            L.e(e.getMessage());
                            if (mE != null) {
                                Toast.makeText(context, "请重新登录", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }

            @Override
            public boolean onItemLongClick(Object o) {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(context);


                return false;
            }
        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        dialogs = DataSupport.findAll(ChatDialog.class);
//        adapter.bindData(dialogs);
//        adapter.dataSort();
//        adapter.notifyDataSetChanged();
//        hideText();
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshEvent event) {
        L.e(event.event);
        dialogs = DataSupport.findAll(ChatDialog.class);
        adapter.bindData(dialogs);
        adapter.dataSort();
        adapter.notifyDataSetChanged();
        hideText();
    }

    private void onRefresh(){
        dialogs = DataSupport.findAll(ChatDialog.class);
        adapter.bindData(dialogs);
        adapter.dataSort();
        adapter.notifyDataSetChanged();
        hideText();
    }

    @Override
    public void onStart() {
        super.onStart();
        dialogs = DataSupport.findAll(ChatDialog.class);
        adapter.bindData(dialogs);
        adapter.dataSort();
        adapter.notifyDataSetChanged();
        hideText();

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xiaoluogo.goodtochat.REFRESH_MESSAGE");
        context.registerReceiver(myReceiver,intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        context.unregisterReceiver(myReceiver);
    }

    /**
     * 隐藏文本
     */
    private void hideText() {
        if (adapter.getItemCount() != 0) {
            ll_no_message.setVisibility(View.GONE);
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    }
}
