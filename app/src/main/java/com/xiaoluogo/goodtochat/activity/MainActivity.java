package com.xiaoluogo.goodtochat.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.base.BasePager;
import com.xiaoluogo.goodtochat.pager.AddressListPager;
import com.xiaoluogo.goodtochat.pager.MessageListPager;
import com.xiaoluogo.goodtochat.pager.MySettingPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FrameLayout fl_main_content;
    private RadioGroup rg_bottom_tag;
    private Toolbar toolbar;
    private TextView tv_title;
    private ImageButton ib_add_to;

    private List<BasePager> basePagers;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();

        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //默认选中第一个页面
        rg_bottom_tag.check(R.id.rb_message);
    }

    private void initData() {
        basePagers = new ArrayList<>();
        basePagers.add(new MessageListPager(this));
        basePagers.add(new AddressListPager(this));
        basePagers.add(new MySettingPager(this));
    }

    private void initView() {
        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ib_add_to = (ImageButton) findViewById(R.id.ib_add_to);

        setSupportActionBar(toolbar);

        ib_add_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "添加好友", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch(checkedId){
                default://默认进入消息界面
                    position = 0;
                    tv_title.setText("好有消息");
                    break;
                case R.id.rb_address_list://联系人界面
                    position = 1;
                    tv_title.setText("联系人");
                    break;
                case R.id.rb_my_setting://我的设置界面
                    position = 2;
                    tv_title.setText("我");
                    break;
            }
            setFragment();
        }
    }

    /**
     * 设置碎片
     */
    private void setFragment() {
        //1.获得碎片管理者
        FragmentManager manager = getSupportFragmentManager();
        //2.创建事务
        FragmentTransaction ft = manager.beginTransaction();
        //3.替换碎片
        ft.replace(R.id.fl_main_content, new ReplaceFragment(getBasePager()));
        //4.提交事务
        ft.commit();
    }
    //根据position获取对应的页面
    private BasePager getBasePager() {
        BasePager basePager  = basePagers.get(position);
        if(basePager != null && !basePager.isInitData){
            //初始化过数据了
            basePager.isInitData = true;
            basePager.initData();
        }
        return basePager;
    }
}
