package com.xiaoluogo.goodtochat.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoluogo.goodtochat.db.ChatMessage;

import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by xiaoluogo on 2017/7/28.
 * Email: angel-lwl@126.com
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public OnRecyclerViewListener onRecyclerViewListener;

    private SparseArray<View> mViews;
    private View mConvertView;
    protected Context mContext;

    public BaseViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }


    public static BaseViewHolder get(Context context, ViewGroup parent, int layoutId) {

        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        BaseViewHolder holder = new BaseViewHolder(context, itemView, parent);
        return holder;
    }


    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    @Override
    public void onClick(View v) {
        if (onRecyclerViewListener != null) {
            onRecyclerViewListener.onItemClick(getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (onRecyclerViewListener != null) {
            onRecyclerViewListener.onItemLongClick(getAdapterPosition());
        }
        return true;
    }

    public void onBindData(int itemViewType, ChatMessage data){
    }

    public void onBindData(int itemViewType, ChatMessage data,BmobIMConversation c) {
    }
}
