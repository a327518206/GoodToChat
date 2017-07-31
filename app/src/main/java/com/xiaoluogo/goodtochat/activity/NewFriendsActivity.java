package com.xiaoluogo.goodtochat.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.adapter.NewFriendAdapter;
import com.xiaoluogo.goodtochat.db.NewFriendManager;

public class NewFriendsActivity extends AppCompatActivity {

    private Toolbar toolbar_newfriends;
    private RecyclerView rv_newfriends_list;
    private SwipeRefreshLayout new_friend_refresh;
    private NewFriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        //接收到好友添加请求在设置适配器
        adapter = new NewFriendAdapter(null);
        rv_newfriends_list.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_newfriends_list.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new_friend_refresh.setRefreshing(true);
        query();
    }

    private void initView() {
        setContentView(R.layout.activity_new_friends);
        toolbar_newfriends = (Toolbar) findViewById(R.id.toolbar_newfriends);
        setSupportActionBar(toolbar_newfriends);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_home);
        }
        rv_newfriends_list = (RecyclerView) findViewById(R.id.rv_newfriends_list);
        new_friend_refresh = (SwipeRefreshLayout) findViewById(R.id.new_friend_refresh);
        new_friend_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                query();
            }
        });
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
     查询本地会话
     */
    public void query(){
        adapter.bindDatas(NewFriendManager.getInstance(this).getAllNewFriend());
        adapter.notifyDataSetChanged();
        new_friend_refresh.setRefreshing(false);
    }
}
