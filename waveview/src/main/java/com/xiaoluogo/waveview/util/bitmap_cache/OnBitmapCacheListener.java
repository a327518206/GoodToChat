package com.xiaoluogo.waveview.util.bitmap_cache;

import android.graphics.Bitmap;

/**
 * Created by xiaoluogo on 2017/8/22.
 * Email: angel-lwl@126.com
 */
public interface OnBitmapCacheListener {
    void onSuccess(Bitmap bitmap);
    void onFailure(Exception e);
}
