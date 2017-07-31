package com.xiaoluogo.goodtochat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.activity.ChatActivity;
import com.xiaoluogo.goodtochat.adapter.base.OnRecyclerViewListener;
import com.xiaoluogo.goodtochat.db.ChatDialog;
import com.xiaoluogo.goodtochat.db.Friend;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * 消息页面适配器
 * Created by xiaoluogo on 2017/7/19.
 * Email: angel-lwl@126.com
 */
public class MessageListPagerAdapter extends RecyclerView.Adapter<MessageListPagerAdapter.ViewHolder> {
    private List<ChatDialog> mdatas;
    private Context context;

    private OnRecyclerViewListener onRecyclerViewListener;
    private View itemView;

    public MessageListPagerAdapter(Context context, List<ChatDialog> datas) {
        this.context = context;
        this.mdatas = datas;
        dataSort();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = View.inflate(context, R.layout.message_list_item, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.tv_message_time.setText(dateFormat.format(Double.parseDouble(mdatas.get(position).getMsgTime())));
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.head)
                .error(R.drawable.default_head);
        Exception mE = null;
        try {
            Friend friend = DataSupport.where("objectId = ? ", mdatas.get(position).getObjectId()).findLast(Friend.class);
            holder.tv_friend_name.setText(friend.getNickName());
            Glide.with(context).load(friend.getHeaderImage())
                    .apply(requestOptions)
                    .into(holder.iv_head_portrait);
        } catch (NullPointerException e) {
            e.printStackTrace();
            mE = e;
        } finally {
            if (mE != null) {
                holder.tv_friend_name.setText("未知");
            }
        }
        if (mdatas.get(position).getMsgType() == 1 || mdatas.get(position).getMsgType() == 2) {
            holder.tv_last_message_content.setText(mdatas.get(position).getMsgContent());
        } else if (mdatas.get(position).getMsgType() == 7 || mdatas.get(position).getMsgType() == 8) {
            holder.tv_last_message_content.setText("[语音]");
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerViewListener.onItemClick(mdatas.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_head_portrait;
        private TextView tv_message_time;
        private TextView tv_friend_name;
        private TextView tv_last_message_content;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_head_portrait = (ImageView) itemView.findViewById(R.id.iv_head_portrait);
            tv_message_time = (TextView) itemView.findViewById(R.id.tv_message_time);
            tv_friend_name = (TextView) itemView.findViewById(R.id.tv_friend_name);
            tv_last_message_content = (TextView) itemView.findViewById(R.id.tv_last_message_content);


        }
    }

    public void setOnItemClickListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public void dataSort() {
        if (mdatas.size() > 1) {
            Collections.sort(mdatas, new Comparator<ChatDialog>() {
                @Override
                public int compare(ChatDialog o1, ChatDialog o2) {
                    if (Double.valueOf(o1.getMsgTime()) > Double.valueOf(o2.getMsgTime())) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    public void bindData(List<ChatDialog> datas) {
        this.mdatas = datas;
    }
}
