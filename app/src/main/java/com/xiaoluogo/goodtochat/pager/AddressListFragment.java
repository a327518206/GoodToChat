package com.xiaoluogo.goodtochat.pager;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.activity.ChatActivity;
import com.xiaoluogo.goodtochat.activity.LoginActivity;
import com.xiaoluogo.goodtochat.activity.MainActivity;
import com.xiaoluogo.goodtochat.activity.NewFriendsActivity;
import com.xiaoluogo.goodtochat.adapter.AddressListPagerAdapter;
import com.xiaoluogo.goodtochat.adapter.BaseRecyclerAdapter;
import com.xiaoluogo.goodtochat.base.BaseFragment;
import com.xiaoluogo.goodtochat.db.Friend;
import com.xiaoluogo.goodtochat.db.NewFriend;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.other.InfoActivity;
import com.xiaoluogo.goodtochat.utils.BmobUtils;
import com.xiaoluogo.goodtochat.utils.CacheUtils;
import com.xiaoluogo.goodtochat.utils.Constants;
import com.xiaoluogo.goodtochat.utils.L;

import org.litepal.crud.DataSupport;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.exception.BmobException;

import java.util.List;

/**
 * 联系人列表页面
 * Created by xiaoluogo on 2017/7/19.
 * Email: angel-lwl@126.com
 */
public class AddressListFragment extends BaseFragment implements View.OnClickListener {
    private AddressListPagerAdapter adapter;
    private RecyclerView rv_address_list;
    private SwipeRefreshLayout srl_addresslist;
    //头控件
    private View headerView;
    private LinearLayout ll_new_friend;
    private LinearLayout ll_group_to_chat;
    //底部控件
    private View footerView;
    private TextView friend_sum;


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.address_list, null);
        rv_address_list = (RecyclerView) view.findViewById(R.id.rv_address_list);
        srl_addresslist = (SwipeRefreshLayout) view.findViewById(R.id.srl_addresslist);

        headerView = View.inflate(context, R.layout.newfriend_group, null);
        ll_new_friend = (LinearLayout) headerView.findViewById(R.id.ll_new_friend);
        ll_group_to_chat = (LinearLayout) headerView.findViewById(R.id.ll_group_to_chat);

        footerView = View.inflate(context, R.layout.footer_friend_sum, null);
        friend_sum = (TextView) footerView.findViewById(R.id.friend_sum);
        L.e("联系人列表页面====初始化视图完成");
        ll_new_friend.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        srl_addresslist.setRefreshing(true);
        synchrodataFromNet();
    }

    @Override
    public void initData() {
        super.initData();
        L.e("联系人列表页面====初始化数据完成");
        List<Friend> datas = DataSupport.findAll(Friend.class);
//        Friend data ;
//        for(int i = 0; i < 10; i++){
//            data = new Friend();
//            data.setUsername("美女"+i);
//            datas.add(data);
//        }

        adapter = new AddressListPagerAdapter(context, datas);
        rv_address_list.setAdapter(adapter);
        friend_sum.setText("您共有" + datas.size() + "位好有");
        //设置头和底部
        adapter.setHeaderView(headerView);
        adapter.setFooterView(footerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_address_list.setLayoutManager(layoutManager);
        rv_address_list.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        L.e("recyclerView的总数" + adapter.getItemCount());

        srl_addresslist.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //核对网络和本地数据
                queryLocalData();
            }
        });

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                Toast.makeText(context, "数据" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, InfoActivity.class);
                intent.putExtra(Constants.INFO_PAGER,Constants.FRIEND_INFO_FROM_HEADER);
                intent.putExtra("now_friend", (Friend) data);
                startActivity(intent);
            }
        });
    }

    private void synchrodataFromNet() {
        if (!BmobUtils.isFrist) {
            queryLocalData();
        } else {
            UserBean user = BmobUser.getCurrentUser(UserBean.class);
            final List<String> objectIds = user.getFriends();
            if (objectIds != null && objectIds.size() > 0) {
                String[] friendObjectId = new String[objectIds.size()];
                final StringBuilder sb = new StringBuilder();
                BmobQuery<UserBean> query;
                for (int i = 0; i < objectIds.size(); i++ ) {
                    friendObjectId[i] = objectIds.get(i);
                    query = new BmobQuery<UserBean>("_User");
                    query.getObject(friendObjectId[i], new QueryListener<UserBean>() {
                        @Override
                        public void done(UserBean userBean, BmobException e) {
                            if (e == null) {
                                BmobUtils.getInstance().saveFriend2db(userBean);
                            } else {
                                L.e("好友信息保存到本地失败");
                            }
                        }
                    });
                }
                queryLocalData();
            }
        }
    }


    private void queryLocalData() {
        List<Friend> newDatas = BmobUtils.getInstance().queryFriends();
        if (newDatas.size() > adapter.getRealItemCount()) {
            adapter.bindDatas(newDatas);
            adapter.notifyDataSetChanged();
        }
        srl_addresslist.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_new_friend:
                Intent intent = new Intent(context, NewFriendsActivity.class);
                context.startActivity(intent);
                break;
        }
    }
}
