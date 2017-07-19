package com.xiaoluogo.goodtochat.base;

import android.content.Context;
import android.view.View;

/**
 * 基础页面
 * Created by xiaoluogo on 2017/7/19.
 * Email: angel-lwl@126.com
 */
public abstract class BasePager {
    public final Context context;
    public View rootview;
    /**
     * 是否初始化过
     */
    public boolean isInitData;

    public BasePager(Context context) {
        this.context = context;
        rootview = initView();
    }

    /**
     * 子类必须重写该方法,用来初始化视图
     * @return
     */
    public abstract View initView();

    /**
     * 初始化数据
     */
    public void initData(){

    }
}
