package com.xiaoluogo.waveview.util.bitmap_cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 网络缓存工具类
 * Created by Administrator on 2017/7/9.
 */

public class NetCacheUtils {
    /**
     * 请求成功
     */
    public static final int SUCCESS = 1;
    /**
     * 请求失败
     */
    public static final int FAIL = 2;
    /**
     * 线程服务
     */
    private final ExecutorService service;
    /**
     * 本地缓存工具类
     */
    private final LocalCacheUtils localCacheUtils;
    /**
     * 内存缓存工具类
     */
    private MemoryCacheUtils memoryCacheUtils;

    public NetCacheUtils( LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        //开启10个线程
        service = Executors.newFixedThreadPool(10);
        this.localCacheUtils = localCacheUtils;
        this.memoryCacheUtils = memoryCacheUtils;
    }

    public void getBitmpFromNet(String imageUrl, int position,OnBitmapCacheListener listener) {
        //在子线程执行联网请求
        service.execute(new MyRunnable(imageUrl, position,listener));
    }

    class MyRunnable implements Runnable {

        private String imageUrl;
        private int position;
        private OnBitmapCacheListener listener;

        public MyRunnable(String imageUrl, int position,OnBitmapCacheListener listener) {
            this.listener = listener;
            this.imageUrl = imageUrl;
            this.position = position;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");//必须大写
                connection.setConnectTimeout(4000);
                connection.setReadTimeout(4000);
                connection.connect();//可以不写
                int code = connection.getResponseCode();//获得请求码
                if (code == 200) {
                    //表示请求成功
                    InputStream in = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    //设置图片 Return a new Message instance from the global pool
//                    Message msg = Message.obtain();
//                    msg.what = SUCCESS;//请求成功
//                    msg.arg1 = position;
//                    msg.obj = bitmap;
//                    handler.sendMessage(msg);
                    listener.onSuccess(bitmap);
                    //内存缓存
                    memoryCacheUtils.putBitmap(imageUrl,bitmap);
                    //本地缓存
                    localCacheUtils.putBitmap(imageUrl,bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
//                Message msg = Message.obtain();
//                msg.what = FAIL;//请求失败
//                msg.arg1 = position;
//                handler.sendMessage(msg);
                listener.onFailure(e);
            }
        }
    }
}
