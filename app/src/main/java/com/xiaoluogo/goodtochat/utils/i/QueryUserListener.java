package com.xiaoluogo.goodtochat.utils.i;

import com.xiaoluogo.goodtochat.doman.UserBean;

import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;

/**
 * @author :smile
 * @project:QueryUserListener
 * @date :2016-02-01-16:23
 */
public abstract class QueryUserListener extends BmobListener1<UserBean> {

    public abstract void done(UserBean s, BmobException e);

    @Override
    protected void postDone(UserBean o, BmobException e) {
        done(o, e);
    }
}
