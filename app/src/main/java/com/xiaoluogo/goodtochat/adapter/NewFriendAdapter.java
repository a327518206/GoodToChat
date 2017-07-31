package com.xiaoluogo.goodtochat.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.db.NewFriend;
import com.xiaoluogo.goodtochat.db.NewFriendManager;
import com.xiaoluogo.goodtochat.doman.AgreeAddFriendMessage;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.utils.BmobUtils;
import com.xiaoluogo.goodtochat.utils.Constants;
import com.xiaoluogo.goodtochat.utils.L;

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
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author :smile
 * @project:NewFriendAdapter
 * @date :2016-04-27-14:18
 */
public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.ViewHolder> {
    private Context context;

    private List<NewFriend> mDatas;

    public NewFriendAdapter(List<NewFriend> datas) {
        this.mDatas = datas;
    }

//    @Override
//    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_friend_list_item, parent, false);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBind(RecyclerView.ViewHolder viewHolder, int realPosition, final NewFriend data) {
//        ((ViewHolder) viewHolder).iv_new_head_portrait.setImageBitmap(data == null ? null : data.getHeaderImage());
//        mViewHolder = (ViewHolder) viewHolder;
//        mViewHolder.tv_new_friend_name.setText(data.getUsername());
//        mViewHolder.tv_new_friend_msg.setText(data.getMsg());
//        int status = data.getStatus();
//        if ( status == Constants.STATUS_VERIFY_NONE || status == Constants.STATUS_VERIFY_READED) {//未添加/已读未添加
//            mViewHolder.btn_agree.setText("接受");
//            mViewHolder.btn_agree.setEnabled(true);
//            mViewHolder.btn_agree.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {//发送消息
//                    agreeAdd(data, new SaveListener<Object>() {
//                        @Override
//                        public void done(Object o, BmobException e) {
//                            if (e == null) {
//                                holder.setText(R.id.btn_aggree, "已添加");
//                                holder.setEnabled(R.id.btn_aggree, false);
//                                mViewHolder.btn_agree.setText("已添加");
//                                mViewHolder.btn_agree.setEnabled(true);
//                            } else {
//                                mViewHolder.btn_agree.setEnabled(true);
//                                L.e("添加好友失败:" + e.getMessage());
//                                Toast.makeText(context, "添加好友失败...", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//            });
//        } else {
//            mViewHolder.btn_agree.setText("已添加");
//            mViewHolder.btn_agree.setEnabled(true);
//        }
//    }


    public void bindDatas(List<NewFriend> allNewFriend) {
        this.mDatas = allNewFriend;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = View.inflate(context, R.layout.new_friend_list_item,null);
        if (context == null) {
            context = parent.getContext();
        }
        View itemView = LayoutInflater.from(context).inflate(R.layout.new_friend_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_new_friend_name.setText(mDatas.get(position).getUsername());
        holder.tv_new_friend_msg.setText(mDatas.get(position).getMsg());
        int status = mDatas.get(position).getStatus();
        if (status == Constants.STATUS_VERIFY_NONE || status == Constants.STATUS_VERIFY_READED) {//未添加/已读未添加
            holder.btn_agree.setText("接受");
            holder.btn_agree.setEnabled(true);
            holder.btn_agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//发送消息
                    BmobUtils.getInstance().agreeAdd(context,mDatas.get(position), new SaveListener<Object>() {
                        @Override
                        public void done(Object o, BmobException e) {
                            if (e == null) {
//                                holder.setText(R.id.btn_aggree, "已添加");
//                                holder.setEnabled(R.id.btn_aggree, false);
                                holder.btn_agree.setText("已添加");
                                holder.btn_agree.setEnabled(true);
                            } else {
                                holder.btn_agree.setEnabled(true);
                                L.e("添加好友失败:" + e.getMessage());
                                Toast.makeText(context, "添加好友失败...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } else {
            holder.btn_agree.setText("已添加");
            holder.btn_agree.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_new_head_portrait;
        private TextView tv_new_friend_name;
        private TextView tv_new_friend_msg;
        private Button btn_agree;
        private Button btn_ignore;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_new_head_portrait = (ImageView) itemView.findViewById(R.id.iv_new_head_portrait);
            tv_new_friend_name = (TextView) itemView.findViewById(R.id.tv_new_friend_name);
            tv_new_friend_msg = (TextView) itemView.findViewById(R.id.tv_new_friend_msg);
            btn_agree = (Button) itemView.findViewById(R.id.btn_agree);
            btn_ignore = (Button) itemView.findViewById(R.id.btn_ignore);
        }
    }


}
