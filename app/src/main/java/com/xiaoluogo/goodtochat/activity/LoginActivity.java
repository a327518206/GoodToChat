package com.xiaoluogo.goodtochat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.utils.CacheUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        //初始化视图控件
        et_username = (EditText) findViewById(R.id.et_username);
        et_password= (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        //监听点击事件
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_login://登录
                if(hasUser()){
                    //1.如果账号密码与服务器端匹配,将缓存的isLogin设置为true,登录成功跳转到主界面
                    CacheUtils.putBoolean(this,"isLogin",true);

                    startActivity(new Intent(this,MainActivity.class));
                }else {
                    //2.如果账号密码与服务器端不匹配,弹出吐司
                    Toast.makeText(this, "账号密码错误或该账号不存在", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_register://注册
//                startActivity(new Intent(this,RegisterUser.class));
                Toast.makeText(this, "注册新用户", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_forget_password://忘记密码
//                startActivity(new Intent(this,ForgetPassword.class));
                Toast.makeText(this, "找回密码", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 将用户输入的账号密码取出来跟服务器端的账号密码做对比
     * @return 返回是否存在该账号
     */
    private boolean hasUser() {
        return false;
    }
}
