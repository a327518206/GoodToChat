package com.xiaoluogo.goodtochat.utils.bitmap_cache;

import android.graphics.Bitmap;

import com.xiaoluogo.goodtochat.utils.L;


/**
 * 图片三级缓存工具类
 * Created by Administrator on 2017/7/9.
 */

public class BitmapCacheUtils {

    /**
     * 网络缓存工具类
     */
    private NetCacheUtils netCacheUtils;

    /**
     * 本地缓存工具类
     */
    private LocalCacheUtils localCacheUtils;

    /**
     * 内存缓存工具类
     */
    private MemoryCacheUtils memoryCacheUtils;

    public BitmapCacheUtils() {
        memoryCacheUtils = new MemoryCacheUtils();
        localCacheUtils = new LocalCacheUtils(memoryCacheUtils);
        netCacheUtils = new NetCacheUtils(localCacheUtils, memoryCacheUtils);
    }

    /**
     * 图片三级缓存
     * 内存中找
     * 本地中找
     * 同时保存到内存一份
     * 网络缓存
     * 本地保存一份
     * 内存保存一份
     *
     * @param imageUrl
     * @param position
     * @return
     */
    public void getBitmap(String imageUrl, int position, OnBitmapCacheListener listener) {
        //1.内存中查找
        if (memoryCacheUtils != null) {
            Bitmap bitmap = memoryCacheUtils.getBitmapFromUrl(imageUrl);
            if (bitmap != null) {
                L.d("内存缓存图片成功" + position);
                listener.onSuccess(bitmap);
            }
        }
        //2.本地中查找
        if (localCacheUtils != null) {
            Bitmap bitmap = localCacheUtils.getBitmapFromUrl(imageUrl);
            if (bitmap != null) {
                L.d("本地缓存图片成功" + position);
                listener.onSuccess(bitmap);
            }
        }
        //3.网络缓存
        netCacheUtils.getBitmpFromNet(imageUrl, position, listener);
    }
}
