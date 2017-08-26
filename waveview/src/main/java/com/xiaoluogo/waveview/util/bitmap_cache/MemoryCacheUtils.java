package com.xiaoluogo.waveview.util.bitmap_cache;

import android.graphics.Bitmap;
import android.util.LruCache;


/**
 * 内存缓存工具类
 * Created by Administrator on 2017/7/10.
 */

public class MemoryCacheUtils {

    /**
     * 图片保存到这个集合中
     */
    private LruCache<String,Bitmap> lruCache;

    public MemoryCacheUtils(){
        //最大内存设置为系统分配给应用内存的1/8
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        lruCache = new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * 根据URL从内存中读取图片
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmapFromUrl(String imageUrl) {
        return lruCache.get(imageUrl);
    }

    /**
     * 根据url存储图片到内存
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        lruCache.put(imageUrl,bitmap);
    }
}
