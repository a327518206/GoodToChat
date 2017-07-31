package com.xiaoluogo.goodtochat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.db.Friend;

import java.util.List;

/**
 * 联系人页面适配器
 * Created by xiaoluogo on 2017/7/19.
 * Email: angel-lwl@126.com
 */
public class AddressListPagerAdapter extends BaseRecyclerAdapter<Friend> {

    public AddressListPagerAdapter(Context context, List<Friend> datas) {
        super(context, datas);
    }

    /**
     * 添加数据--添加好友,显示到联系人列表中
     */
    public void addData() {

    }

    /**
     * 删除数据--删除好友,在联系人列表中删除好友
     */
    public void deleteData() {

    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int realPosition, Friend data) {
        ((ViewHolder) viewHolder).tv_friend_name.setText(mDatas.get(realPosition).getUsername());
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.head)
                .error(R.drawable.default_head);
        Glide.with(context).load(mDatas.get(realPosition).getHeaderImage())
                .apply(requestOptions)
                .into(((ViewHolder) viewHolder).iv_head_portrait);
    }

    @Override
    public void bindDatas(List<Friend> allNewFriend) {
        mDatas = allNewFriend;
    }

    public class ViewHolder extends BaseRecyclerAdapter.ViewHolder {

        private ImageView iv_head_portrait;
        private TextView tv_friend_name;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_head_portrait = (ImageView) itemView.findViewById(R.id.iv_head_portrait);
            tv_friend_name = (TextView) itemView.findViewById(R.id.tv_friend_name);
        }
    }
}
