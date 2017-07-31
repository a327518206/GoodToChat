package com.xiaoluogo.goodtochat.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.xiaoluogo.goodtochat.event.RefreshEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xiaoluogo on 2017/7/24.
 * Email: angel-lwl@126.com
 */
public class BaseActivity extends AppCompatActivity {

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

    /**
     * 隐藏软键盘-一般是EditText.getWindowToken()
     *
     * @param token
     */
    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        EventBus.getDefault().post(new RefreshEvent("主页面发消息"));
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
