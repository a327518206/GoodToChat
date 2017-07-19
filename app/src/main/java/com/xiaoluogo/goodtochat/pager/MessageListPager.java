package com.xiaoluogo.goodtochat.pager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.adapter.MessageListPagerAdapter;
import com.xiaoluogo.goodtochat.base.BasePager;
import com.xiaoluogo.goodtochat.utils.L;


/**
 * 消息列表页面
 * Created by xiaoluogo on 2017/7/19.
 * Email: angel-lwl@126.com
 */
public class MessageListPager extends BasePager {

    private RecyclerView rv_message_list;
    private LinearLayout ll_no_message;
    private MessageListPagerAdapter adapter;

    public MessageListPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.message_list,null);
        L.e("消息列表页面====初始化视图完成");
        rv_message_list = (RecyclerView) view.findViewById(R.id.rv_message_list);
        ll_no_message = (LinearLayout) view.findViewById(R.id.ll_no_message);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        L.e("消息列表页面====初始化数据完成");
        //联网请求数据
        //设置适配器
        adapter = new MessageListPagerAdapter(context);
        rv_message_list.setAdapter(adapter);
        rv_message_list.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        //设置recyclerView的分割线
        rv_message_list.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
    }
}
