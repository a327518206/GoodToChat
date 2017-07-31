package com.xiaoluogo.goodtochat.utils;

/**
 * 检查sd卡
 * Created by xiaoluogo on 2017/7/30.
 * Email: angel-lwl@126.com
 */
public class Utils {
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }
}
