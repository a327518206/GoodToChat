package com.xiaoluogo.goodtochat.utils.i;

import java.util.List;

/**
 * Created by xiaoluogo on 2017/7/25.
 * Email: angel-lwl@126.com
 */
public interface QueryFriendListener<T> {
    void done(List<T> lists, Exception e);
}
