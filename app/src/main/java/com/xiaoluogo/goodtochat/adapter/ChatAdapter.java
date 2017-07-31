package com.xiaoluogo.goodtochat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.adapter.base.BaseViewHolder;
import com.xiaoluogo.goodtochat.adapter.base.OnRecyclerViewListener;
import com.xiaoluogo.goodtochat.adapter.holder.ReceiveViewHolder;
import com.xiaoluogo.goodtochat.adapter.holder.SendViewHolder;
import com.xiaoluogo.goodtochat.db.ChatMessage;

import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by xiaoluogo on 2017/7/27.
 * Email: angel-lwl@126.com
 */
public class ChatAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final BmobIMConversation c;
    private Context mcontext;
    private List<ChatMessage> chatMessages;

    //文本
    private final int TYPE_SEND_TXT = 1;
    private final int TYPE_RECEIVER_TXT = 2;
    //图片
    private final int TYPE_SEND_IMAGE = 3;
    private final int TYPE_RECEIVER_IMAGE = 4;
    //位置
    private final int TYPE_SEND_LOCATION = 5;
    private final int TYPE_RECEIVER_LOCATION = 6;
    //语音
    private final int TYPE_SEND_VOICE = 7;
    private final int TYPE_RECEIVER_VOICE = 8;
    //视频
    private final int TYPE_SEND_VIDEO = 9;
    private final int TYPE_RECEIVER_VIDEO = 10;

    public ChatAdapter(Context context, List<ChatMessage> messages, BmobIMConversation c) {
        mcontext = context;
        chatMessages = messages;
        this.c = c;
    }

    @Override
    public int getItemViewType(int position) {
        return chatMessages.get(position).getMessageType();
    }

    public ChatMessage getData(int position) {
        return chatMessages.get(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType % 2 == 1) {
//            return BaseViewHolder.get(mcontext,parent,R.layout.base_item_send);
            View itemView = LayoutInflater.from(mcontext).inflate(R.layout.base_item_send, parent,
                    false);
//            View itemView = View.inflate(mcontext, R.layout.base_item_send, null);
            return new SendViewHolder(mcontext, parent, itemView, onRecyclerViewListener);
        } else if (viewType % 2 == 0) {
//            return BaseViewHolder.get(mcontext,parent,R.layout.base_item_receive);
            View itemView = LayoutInflater.from(mcontext).inflate(R.layout.base_item_receive, parent,
                    false);
//            View itemView = View.inflate(mcontext, R.layout.base_item_receive, null);
            return new ReceiveViewHolder(mcontext, parent, itemView, onRecyclerViewListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (getItemViewType(position) % 2 == 1) {
            ((SendViewHolder) holder).onBindData(getItemViewType(position), getData(position),c);
        } else if (getItemViewType(position) % 2 == 0) {
            ((ReceiveViewHolder) holder).onBindData(getItemViewType(position), getData(position));
        }
    }



    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    /**
     * 插入消息
     *
     * @param msg
     */
    public void addMessage(ChatMessage msg) {
        chatMessages.add(msg);
    }

    /**
     * 移除消息
     */
    public void removeMessage(int position) {
        chatMessages.remove(position);
    }


    public int findPosition(long id) {
        int index = getCount();
        int position = -1;
        while (index-- > 0) {
            if (this.getItemId(index) == id) {
                position = index;
                break;
            }
        }
        return position;
    }

    public int getCount() {
        return this.chatMessages == null ? 0 : this.chatMessages.size();
    }
}
