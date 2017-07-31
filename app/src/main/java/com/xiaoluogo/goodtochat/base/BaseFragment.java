package com.xiaoluogo.goodtochat.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

/**
 * 基础页面
 * Created by xiaoluogo on 2017/7/19.
 * Email: angel-lwl@126.com
 */
public abstract class BaseFragment extends Fragment {

    public Context context;

    /**
     * 这个是第一个执行的
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
//        EventBus.getDefault().register(this);
    }

    /**
     * 创建视图是调用这个方法
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 初始化视图,子类必须覆写
     * @return
     */
    public abstract View initView() ;

    /**
     * 活动创建完成调用这个方法
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 活动创建成功初始化数据
     */
    public void initData() {
    }

//    @Override
//    public void onDestroy() {
//        EventBus.getDefault().unregister(this);
//        super.onDestroy();
//    }
}
