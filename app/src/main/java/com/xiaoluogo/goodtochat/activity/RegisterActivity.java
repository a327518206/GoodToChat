package com.xiaoluogo.goodtochat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.utils.L;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册页面
 * Created by xiaoluogo on 2017/7/21.
 * Email: angel-lwl@126.com
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 用户名是否通过验证
     */
    private boolean isUsernamePass = false;
    /**
     * 昵称是否通过验证
     */
    private boolean isNicknamePass = false;
    /**
     * 第一次密码是否通过验证
     */
    private boolean isPasswordOncePass = false;
    /**
     * 第二次密码是否通过验证
     */
    private boolean isPasswordTwoPass = false;
    /**
     * 邮箱是否通过验证
     */
    private boolean isEmailPass = false;
    private boolean isGetFocus = false;

    private Toolbar toolbarRegister;

    private EditText etUsernameRegister;
    private TextView tvRegisterUsernameVerify;
    private EditText etNicknameRegister;
    private TextView tvRegisterNicknameVerify;
    private EditText etPasswordOnce;
    private TextView tvRegisterPasswordOnce;
    private EditText etPasswordTwo;
    private TextView tvRegisterPasswordTwo;
    private EditText etEmail;
    private TextView tvRegisterEmail;
    private CheckBox checkboxRegister;
    private TextView tvRegistrationTerms;
    private Button btnRegister;
    private RelativeLayout rlInTheRegister;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();

    }

    /**
     * Find the Views in the layout
     */
    private void findViews() {
        setContentView(R.layout.activity_register);

        toolbarRegister = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbarRegister);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_home);
        }

        etUsernameRegister = (EditText) findViewById(R.id.et_username_register);
        tvRegisterUsernameVerify = (TextView) findViewById(R.id.tv_register_username_verify);
        etNicknameRegister = (EditText) findViewById(R.id.et_nickname_register);
        tvRegisterNicknameVerify = (TextView) findViewById(R.id.tv_register_nickname_verify);
        etPasswordOnce = (EditText) findViewById(R.id.et_password_once);
        tvRegisterPasswordOnce = (TextView) findViewById(R.id.tv_register_password_once);
        etPasswordTwo = (EditText) findViewById(R.id.et_password_two);
        tvRegisterPasswordTwo = (TextView) findViewById(R.id.tv_register_password_two);
        etEmail = (EditText) findViewById(R.id.et_email);
        tvRegisterEmail = (TextView) findViewById(R.id.tv_register_email);
        checkboxRegister = (CheckBox) findViewById(R.id.checkbox_register);
        tvRegistrationTerms = (TextView) findViewById(R.id.tv_registration_terms);
        btnRegister = (Button) findViewById(R.id.btn_register);
        rlInTheRegister = (RelativeLayout) findViewById(R.id.rl_in_the_register);
        //对注册内容的效验
        etUsernameRegister.setOnFocusChangeListener(new MyOnFocusChangeListener());
        etNicknameRegister.setOnFocusChangeListener(new MyOnFocusChangeListener());
        etPasswordOnce.setOnFocusChangeListener(new MyOnFocusChangeListener());
        etPasswordTwo.setOnFocusChangeListener(new MyOnFocusChangeListener());
        etEmail.setOnFocusChangeListener(new MyOnFocusChangeListener());

        btnRegister.setOnClickListener(this);
        tvRegistrationTerms.setOnClickListener(this);
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

    /**
     * Handle button click events
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.et_username_register://用户名
//                getFocus(etUsernameRegister);
//                break;
//            case R.id.et_nickname_register://昵称
//                getFocus(etNicknameRegister);
//                break;
//            case R.id.et_password_once://第一次输入密码
//                getFocus(etPasswordOnce);
//                break;
//            case R.id.et_password_two://第二次输入密码
//                getFocus(etPasswordTwo);
//                break;
//            case R.id.ed_email://邮箱地址
//                getFocus(etEmail);
//                break;
            case R.id.btn_register://注册按钮
                //1使所有的editText失去焦点
//                loseFocus();
                if (!isGetFocus) {
                    isGetFocus = true;
                    getFocus(btnRegister);
                    onClick(btnRegister);
                    break;
                } else {
                    isGetFocus = false;
                    //2判断所有的验证是否都通过了以及checkbox是否为勾选状态
                    if (isUsernamePass && isNicknamePass && isPasswordOncePass && isPasswordTwoPass && isEmailPass && checkboxRegister.isChecked()) {
                        rlInTheRegister.setVisibility(View.VISIBLE);
                        // 使用BmobSDK提供的注册功能
                        UserBean user = new UserBean();
                        user.setUsername(etUsernameRegister.getText().toString());
                        user.setPassword(etPasswordTwo.getText().toString());
                        user.setEmail(etEmail.getText().toString());
                        user.setNickName(etNicknameRegister.getText().toString());
//                        user.setUsername("123456");
//                        user.setPassword("123456");
//                        user.setEmail("12345678@qq.com");
                        //用户注册
                        user.signUp(new SaveListener<UserBean>() {
                            @Override
                            public void done(UserBean user, BmobException e) {
                                if (e == null) {
                                    //注册成功
                                    L.e("注册成功");
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                                    dialog.setTitle("注册成功");
                                    dialog.setMessage("请牢记账号密码\n"+"账号:"+etUsernameRegister.getText().toString()+"\n密码:"+etPasswordTwo.getText().toString());
                                    dialog.setCancelable(false);
                                    dialog.setPositiveButton("立即登录", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                            intent.putExtra("username",etUsernameRegister.getText().toString());
                                            intent.putExtra("password",etPasswordTwo.getText().toString());
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    dialog.show();
                                } else {
                                    //注册失败
                                    L.e("注册失败");
                                    e.printStackTrace();
                                    L.e(e.getMessage());
                                    if (e.getMessage().equals("username " + "'" + etUsernameRegister.getText().toString() + "'" + " already taken.")) {
                                        Toast.makeText(RegisterActivity.this, "注册失败"+"--用户名已存在", Toast.LENGTH_SHORT).show();
                                    }
                                    if (e.getMessage().equals("email " + "'" + etEmail.getText().toString() + "'" + " already taken.")) {
                                        Toast.makeText(RegisterActivity.this, "注册失败" + "--用户名已存在", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                rlInTheRegister.setVisibility(View.GONE);
                            }
                        });

                    } else {
                        //注册失败--
                        Toast.makeText(this, "注册失败,请检查输入的注册信息", Toast.LENGTH_SHORT).show();
                        rlInTheRegister.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.tv_registration_terms://注册条款
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 获得焦点
     *
     * @param view
     */

    private void getFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);//设置触摸聚焦
        view.requestFocus();//请求焦点
        view.findFocus();//获取焦点
    }


    /**
     * 失去焦点
     */
//    private void loseFocus(boolean loseFocus) {
//        etUsernameRegister.clearFocus();
//        etNicknameRegister.clearFocus();
//        etPasswordOnce.clearFocus();
//        etPasswordTwo.clearFocus();
//        etEmail.clearFocus();
//        etUsernameRegister.setFocusable(loseFocus);
//        etNicknameRegister.setFocusable(loseFocus);
//        etPasswordOnce.setFocusable(loseFocus);
//        etPasswordTwo.setFocusable(loseFocus);
//        etEmail.setFocusable(loseFocus);
//    }

    /**
     * 获得焦点和失去焦点的监听---用于验证所有注册信息
     */
    class MyOnFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.et_username_register://用户名
                    if (hasFocus) {
                        //得到焦点.隐藏tvRegisterUsernameVerify
                        tvRegisterUsernameVerify.setVisibility(View.GONE);
                    } else {
                        //失去焦点
                        //1判断内容是否为空,为空显示tvRegisterUsernameVerify
                        if ("".equals(etUsernameRegister.getText().toString().trim())) {
                            tvRegisterUsernameVerify.setVisibility(View.VISIBLE);
                            tvRegisterUsernameVerify.setText("用户名为空");
                            tvRegisterUsernameVerify.setTextColor(Color.RED);
                            isUsernamePass = false;
                        } else {
                            if (etUsernameRegister.getText().toString().contains("\\") ||
                                    //2不为空判断是否有"\" " " "?"
                                    etUsernameRegister.getText().toString().contains(" ") ||
                                    etUsernameRegister.getText().toString().contains("?")) {
                                tvRegisterUsernameVerify.setVisibility(View.VISIBLE);
                                tvRegisterUsernameVerify.setText("用户名不可以含有\" \"\"?\"\"\\\"");
                                tvRegisterUsernameVerify.setTextColor(Color.RED);
                                isUsernamePass = false;
                            }
//                            else if (queryData("username",etUsernameRegister.getText().toString())) {
//                                //3联网验证--网络中有同样的就
//                                tvRegisterUsernameVerify.setVisibility(View.VISIBLE);
//                                tvRegisterUsernameVerify.setText("用户名已存在");
//                                tvRegisterUsernameVerify.setTextColor(Color.RED);
//                                isUsernamePass = false;
//                            }
                            else {
                                tvRegisterUsernameVerify.setVisibility(View.VISIBLE);
                                tvRegisterUsernameVerify.setText("可以注册");
                                tvRegisterUsernameVerify.setTextColor(Color.GREEN);
                                isUsernamePass = true;
                            }
                        }
                    }
                    break;
                case R.id.et_nickname_register://昵称
                    if (hasFocus) {
                        //得到焦点.隐藏tvRegisterNicknameVerify
                        tvRegisterNicknameVerify.setVisibility(View.GONE);
                    } else {
                        //失去焦点
                        //1判断内容是否为空,为空显示tvRegisterNicknameVerify
                        if ("".equals(etNicknameRegister.getText().toString().trim())) {
                            tvRegisterNicknameVerify.setVisibility(View.VISIBLE);
                            tvRegisterNicknameVerify.setText("昵称为空");
                            tvRegisterNicknameVerify.setTextColor(Color.RED);
                            isNicknamePass = false;
                        } else {
                            //2不为空判断是否有"\" " " "?"
                            if (etUsernameRegister.getText().toString().contains("\\") ||
                                    etUsernameRegister.getText().toString().contains(" ") ||
                                    etUsernameRegister.getText().toString().contains("?")) {
                                tvRegisterNicknameVerify.setVisibility(View.VISIBLE);
                                tvRegisterNicknameVerify.setText("昵称不可以含有\" \"\"?\"\"\\\"");
                                tvRegisterNicknameVerify.setTextColor(Color.RED);
                                isNicknamePass = false;
                            } else {
                                tvRegisterNicknameVerify.setVisibility(View.VISIBLE);
                                tvRegisterNicknameVerify.setText("可以使用");
                                tvRegisterNicknameVerify.setTextColor(Color.GREEN);
                                isNicknamePass = true;
                            }
                        }
                    }
                    break;
                case R.id.et_password_once://第一次输入密码
                    if (hasFocus) {
                        //得到焦点.隐藏tvRegisterPasswordOnce
                        tvRegisterPasswordOnce.setVisibility(View.GONE);
                    } else {
                        //失去焦点
                        //1判断内容是否为空,为空显示tvRegisterPasswordOnce
                        if ("".equals(etPasswordOnce.getText().toString())) {
                            tvRegisterPasswordOnce.setVisibility(View.VISIBLE);
                            tvRegisterPasswordOnce.setText("密码为空");
                            tvRegisterPasswordOnce.setTextColor(Color.RED);
                            isPasswordOncePass = false;
                        } else {
                            //2判断长度是否小于6
                            if (etPasswordOnce.getText().toString().length() < 6) {
                                tvRegisterPasswordOnce.setVisibility(View.VISIBLE);
                                tvRegisterPasswordOnce.setText("密码必须为6位及以上");
                                tvRegisterPasswordOnce.setTextColor(Color.RED);
                                isPasswordOncePass = false;
                            } else {
                                tvRegisterPasswordOnce.setVisibility(View.VISIBLE);
                                tvRegisterPasswordOnce.setText("可以使用");
                                tvRegisterPasswordOnce.setTextColor(Color.GREEN);
                                isPasswordOncePass = true;
                            }
                        }
                    }
                    break;
                case R.id.et_password_two://第二次输入密码
                    if (hasFocus) {
                        //得到焦点.隐藏tvRegisterPasswordTwo
                        tvRegisterPasswordTwo.setVisibility(View.GONE);
                    } else {
                        //失去焦点
                        if ("".equals(etPasswordTwo.getText().toString())) {
                            tvRegisterPasswordTwo.setVisibility(View.VISIBLE);
                            tvRegisterPasswordTwo.setText("密码为空");
                            tvRegisterPasswordTwo.setTextColor(Color.RED);
                            isPasswordOncePass = false;
                        } else {
                            //2判断第二次的密码是否和第一次相同
                            if (!(etPasswordOnce.getText().toString()).equals(etPasswordTwo.getText().toString())) {
                                tvRegisterPasswordTwo.setVisibility(View.VISIBLE);
                                tvRegisterPasswordTwo.setText("两次输入的密码不一致");
                                tvRegisterPasswordTwo.setTextColor(Color.RED);
                                isPasswordTwoPass = false;
                            } else {
                                tvRegisterPasswordTwo.setVisibility(View.VISIBLE);
                                tvRegisterPasswordTwo.setText("可以使用");
                                tvRegisterPasswordTwo.setTextColor(Color.GREEN);
                                isPasswordTwoPass = true;
                            }
                        }
                    }
                    break;
                case R.id.et_email://邮箱地址
                    if (hasFocus) {
                        //得到焦点.隐藏tvRegisterEmail
                        tvRegisterEmail.setVisibility(View.GONE);
                    } else {
                        //失去焦点
                        //1判断内容是否为空,为空显示tvRegisterEmail
                        if ("".equals(etEmail.getText().toString().trim())) {
                            tvRegisterEmail.setVisibility(View.VISIBLE);
                            tvRegisterEmail.setText("邮箱地址为空");
                            tvRegisterEmail.setTextColor(Color.RED);
                            isEmailPass = false;
                        } else {
                            //2不为空判断是否有"\" " " "?"
                            if (etEmail.getText().toString().contains("\\") ||
                                    etUsernameRegister.getText().toString().contains(" ") ||
                                    etUsernameRegister.getText().toString().contains("?")) {
                                tvRegisterEmail.setVisibility(View.VISIBLE);
                                tvRegisterEmail.setText("输入不可以含有\" \"\"?\"\"\\\"");
                                tvRegisterEmail.setTextColor(Color.RED);
                                isEmailPass = false;
                            } else if (etEmail.getText().toString().contains("@") &&
                                    (etEmail.getText().toString().endsWith(".com") ||
                                            etEmail.getText().toString().endsWith(".com.cn") ||
                                            etEmail.getText().toString().endsWith(".net"))) {
                                //3判断是否有@和是否为.com结尾
//                            if () {
//                                //4联网验证--网络中有同样的就
//                                tvRegisterEmail.setVisibility(View.VISIBLE);
//                                tvRegisterEmail.setText("邮箱已存在");
//                                tvRegisterEmail.setTextColor(Color.RED);
//                                isEmailPass = false;
//                            }
//                            else{
                                tvRegisterEmail.setVisibility(View.VISIBLE);
                                tvRegisterEmail.setText("可以注册");
                                tvRegisterEmail.setTextColor(Color.GREEN);
                                isEmailPass = true;
                            } else {
                                tvRegisterEmail.setVisibility(View.VISIBLE);
                                tvRegisterEmail.setText("请输入正确的邮箱地址");
                                tvRegisterEmail.setTextColor(Color.RED);
                                isEmailPass = false;
                            }
                        }
                    }
                    break;
            }
        }
    }
}
