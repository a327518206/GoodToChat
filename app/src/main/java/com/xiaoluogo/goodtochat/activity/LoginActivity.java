package com.xiaoluogo.goodtochat.activity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.db.Friend;
import com.xiaoluogo.goodtochat.db.NewFriendManager;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.other.InfoActivity;
import com.xiaoluogo.goodtochat.utils.BmobUtils;
import com.xiaoluogo.goodtochat.utils.CacheUtils;
import com.xiaoluogo.goodtochat.utils.L;

import org.litepal.crud.DataSupport;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.QueryListener;

/**
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //用户名
    private EditText et_username;
    //密码
    private EditText et_password;
    //登录
    private Button btn_login;
    //注册
    private TextView tv_register;
    //忘记密码
    private TextView tv_forget_password;

    private RelativeLayout rl_msg;
    private RelativeLayout rl_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
//        if (getIntent() != null) {
//            et_username.setText(getIntent().getStringExtra("username"));
//            et_password.setText(getIntent().getStringExtra("password"));
//        }
    }

    private void initView() {
        //初始化视图控件
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        rl_msg = (RelativeLayout) findViewById(R.id.rl_msg);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        //监听点击事件
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login://登录
                hideSoftInputView();
                UserBean user = new UserBean();
                user.setUsername(et_username.getText().toString());
                user.setPassword(et_password.getText().toString());
                user.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            //1.如果账号密码与服务器端匹配,将缓存的isLogin设置为true,登录成功跳转到主界面
                            rl_loading.setVisibility(View.VISIBLE);
                            rl_msg.setVisibility(View.GONE);
                            NewFriendManager.getInstance(LoginActivity.this);
                            searchFriend();
                        } else {
                            //2.如果账号密码与服务器端不匹配,弹出吐司
                            Toast.makeText(LoginActivity.this, "账号密码错误或该账号不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.tv_register://注册
//                startActivity(new Intent(this,RegisterUser.class));
                Toast.makeText(this, "注册新用户", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_forget_password://忘记密码
//                startActivity(new Intent(this,ForgetPassword.class));
                Toast.makeText(this, "找回密码", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void searchFriend(){
        UserBean user = BmobUser.getCurrentUser(UserBean.class);
        final List<String> objectIds = user.getFriends();
        if (objectIds != null && objectIds.size() > 0) {
            String[] friendObjectId = new String[objectIds.size()];
            final StringBuilder sb = new StringBuilder();
            BmobQuery<UserBean> query;
            for (int i = 0; i < objectIds.size(); i++ ) {
                friendObjectId[i] = objectIds.get(i);
                query = new BmobQuery<UserBean>("_User");
                query.getObject(friendObjectId[i], new QueryListener<UserBean>() {
                    @Override
                    public void done(UserBean userBean, BmobException e) {
                        if (e == null) {
                            saveFriend2db(userBean);
                            if (DataSupport.findAll(Friend.class).size() == objectIds.size()) {
                                CacheUtils.putBoolean(LoginActivity.this, "isLogin", true);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }else{
                                searchFriend();
                            }
                        } else {
                            L.e("好友信息保存到本地失败");
                            rl_loading.setVisibility(View.GONE);
                            rl_msg.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "好友信息保存到本地失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            CacheUtils.putBoolean(LoginActivity.this, "isLogin", true);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    public void saveFriend2db(UserBean friend) {
        List<Friend> friends = DataSupport.findAll(Friend.class);
        boolean isExist = false;
        for (int i = 0; i < friends.size(); i++) {
            if (friend.getObjectId().equals(friends.get(i).getObjectId())) {
                isExist = true;
            }
        }
        if (!isExist) {
            Friend f = new Friend();
            f.setObjectId(friend.getObjectId());
            f.setUsername(friend.getUsername());
            f.setNickName(friend.getNickName());
            f.setEmail(friend.getEmail());
            f.setGenderNumber(friend.getGenderNumber());
            f.setMobilePhoneNumber(friend.getMobilePhoneNumber());
            f.setHeaderImage(friend.getHeaderImage());
            f.save();
        }
    }

//    /**
//     * 将用户输入的账号密码取出来跟服务器端的账号密码做对比
//     * @return 返回是否存在该账号
//     */
//    private boolean hasUser() {
//        return false;
//    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
