package com.xiaoluogo.goodtochat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.utils.CacheUtils;

public class WelcomeActivity extends AppCompatActivity {

    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        time = 2000;

        if (!isTaskRoot()) {
//            time = 0;
            finish();
            return;
        }

        final boolean islogin = CacheUtils.getBoolean(this,"isLogin");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(islogin){
                    startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                    finish();
                }
            }
        },time);
    }
}
