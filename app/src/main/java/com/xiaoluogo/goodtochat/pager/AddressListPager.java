package com.xiaoluogo.goodtochat.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.xiaoluogo.goodtochat.base.BasePager;
import com.xiaoluogo.goodtochat.utils.L;

/**
 * 联系人列表页面
 * Created by xiaoluogo on 2017/7/19.
 * Email: angel-lwl@126.com
 */
public class AddressListPager extends BasePager {

    private TextView textView;

    public AddressListPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
       textView = new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(40);
        textView.setGravity(Gravity.CENTER);
        L.e("联系人列表页面====初始化视图完成");
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("联系人页面");
        L.e("联系人列表页面====初始化数据完成");
    }
}
