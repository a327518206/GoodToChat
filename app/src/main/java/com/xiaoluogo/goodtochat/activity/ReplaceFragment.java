package com.xiaoluogo.goodtochat.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoluogo.goodtochat.base.BasePager;

/**
 * 用于碎片获取要替换的视图
 * Created by xiaoluogo on 2017/7/19.
 * Email: angel-lwl@126.com
 */
public class ReplaceFragment extends Fragment {
    private BasePager basePager;

    public ReplaceFragment(BasePager basePager) {
        this.basePager = basePager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 碎片创建时初始化视图
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(basePager != null){
            return basePager.rootview;
        }
        return null;
    }
}
