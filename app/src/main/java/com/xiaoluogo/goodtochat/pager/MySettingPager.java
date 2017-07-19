package com.xiaoluogo.goodtochat.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.xiaoluogo.goodtochat.base.BasePager;
import com.xiaoluogo.goodtochat.utils.L;

/**
 * 我的相关设置页面
 * Created by xiaoluogo on 2017/7/19.
 * Email: angel-lwl@126.com
 */
public class MySettingPager extends BasePager {

    private TextView textView;

    public MySettingPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(40);
        textView.setGravity(Gravity.CENTER);
        L.e("我的相关设置页面====初始化视图完成");
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("我页面");
        L.e("我的相关设置页面====初始化数据完成");
    }
}
