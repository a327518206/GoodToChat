package com.xiaoluogo.goodtochat.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.utils.BmobUtils;
import com.xiaoluogo.goodtochat.utils.Constants;
import com.xiaoluogo.goodtochat.utils.L;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 搜索页面
 * Created by xiaoluogo on 2017/7/22.
 * Email: angel-lwl@126.com
 */
public class SearchUserActivity extends AppCompatActivity {
    private Toolbar toolbarSearchuser;
    private EditText etSearch;
    private TextView tvSearch;
    private RelativeLayout rlSearchFriendInfo;
    private ImageView ivFriendHeader;
    private TextView tvFriendName;
    private TextView tv_search_result;
    private ProgressBar progressbar_loading;

    private UserBean findUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
    }

    /**
     * Find the Views in the layout
     */
    private void findViews() {
        setContentView(R.layout.activity_searchuser);
        toolbarSearchuser = (Toolbar) findViewById(R.id.toolbar_searchuser);

        setSupportActionBar(toolbarSearchuser);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_home);
        }

        etSearch = (EditText) findViewById(R.id.et_search);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        rlSearchFriendInfo = (RelativeLayout) findViewById(R.id.rl_search_friend_info);
        ivFriendHeader = (ImageView) findViewById(R.id.iv_friend_header);
        tvFriendName = (TextView) findViewById(R.id.tv_friend_name);
        tv_search_result = (TextView) findViewById(R.id.tv_search_result);
        progressbar_loading = (ProgressBar) findViewById(R.id.progressbar_loading);

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlSearchFriendInfo.setVisibility(View.GONE);
                progressbar_loading.setVisibility(View.VISIBLE);
                tv_search_result.setVisibility(View.VISIBLE);
                tv_search_result.setText("搜索中...");
                String findUsername = etSearch.getText().toString();
                if (TextUtils.isEmpty(findUsername.trim())) {
                    progressbar_loading.setVisibility(View.GONE);
                    tv_search_result.setText("请输入正确的ID");
                    return;
                }
                final UserBean userBean = BmobUser.getCurrentUser(UserBean.class);
                List<String> usernameList = userBean.getFriends();
                if (usernameList != null && usernameList.size() > 0) {
                    for (String user : usernameList) {
                        if (user.equals(findUsername)) {
                            progressbar_loading.setVisibility(View.GONE);
                            tv_search_result.setText("你已经添加该好友");
                            return;
                        }
                    }
                }
                BmobUtils.queryUsers(findUsername, new FindListener<UserBean>() {
                    @Override
                    public void done(List<UserBean> list, BmobException e) {
                        if (e == null) {
                            if (list != null && list.size() > 0) {
                                for (UserBean user : list) {
                                    if (userBean.getUsername().equals(user.getUsername())) {
                                        tv_search_result.setText("您搜索的是自己的ID");
                                    } else {
                                        findUser = user;
                                        tv_search_result.setVisibility(View.GONE);
                                        rlSearchFriendInfo.setVisibility(View.VISIBLE);
                                        tvFriendName.setText(findUser.getNickName());
                                    }
                                }
                            }
                        } else {
                            tv_search_result.setText("未找到指定用户");
                        }
                        progressbar_loading.setVisibility(View.GONE);
                    }
                });
            }
        });
        rlSearchFriendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchUserActivity.this, InfoActivity.class);
                intent.putExtra("newFriendInfo", findUser);
                intent.putExtra(Constants.INFO_PAGER, Constants.FRIEND_INFO_FROM_SEARCH);
                startActivity(intent);
            }
        });
    }

    /**
     * 左上角返回按钮点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}