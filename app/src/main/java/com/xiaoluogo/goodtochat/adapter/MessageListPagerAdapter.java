package com.xiaoluogo.goodtochat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoluogo.goodtochat.R;

/**
 * Created by xiaoluogo on 2017/7/19.
 * Email: angel-lwl@126.com
 */
public class MessageListPagerAdapter extends RecyclerView.Adapter<MessageListPagerAdapter.ViewHolder> {
    private Context context;

    public MessageListPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.message_list_item,null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_head_portrait;
        private TextView tv_message_time;
        private TextView tv_friend_name;
        private TextView tv_last_message_content;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_head_portrait= (ImageView) itemView.findViewById(R.id.iv_head_portrait);
            tv_message_time= (TextView) itemView.findViewById(R.id.tv_message_time);
            tv_friend_name= (TextView) itemView.findViewById(R.id.tv_friend_name);
            tv_last_message_content= (TextView) itemView.findViewById(R.id.tv_last_message_content);

        }
    }
}
