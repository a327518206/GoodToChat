package com.xiaoluogo.goodtochat.adapter.base;

/**
 * 为RecycleView添加点击事件
 */
public interface OnRecyclerViewListener<T> {
    void onItemClick(T t);
    boolean onItemLongClick(T t);
}
