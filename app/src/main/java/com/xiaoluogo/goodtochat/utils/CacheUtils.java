package com.xiaoluogo.goodtochat.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 缓存工具类
 * Created by Administrator on 2017/7/18.
 */

public class CacheUtils {
    /**
     * 取出是否登录的状态
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("xiaoluogo",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }
    /**
     * 缓存是否登录的状态
     * @param context
     * @param key
     * @return
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("xiaoluogo",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    public static String getString(Context context,String key) {
        SharedPreferences sp = context.getSharedPreferences("xiaoluogo",Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("xiaoluogo",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }
    public static int getInt(Context context,String key) {
        SharedPreferences sp = context.getSharedPreferences("xiaoluogo",Context.MODE_PRIVATE);
        return sp.getInt(key,0);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences("xiaoluogo",Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }
}
